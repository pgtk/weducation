/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.edu.pgtk.weducation.reports;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import ru.edu.pgtk.weducation.ejb.CourseWorkMarksEJB;
import ru.edu.pgtk.weducation.ejb.FinalMarksEJB;
import ru.edu.pgtk.weducation.ejb.FinalPracticMarksEJB;
import ru.edu.pgtk.weducation.ejb.GOSMarksEJB;
import ru.edu.pgtk.weducation.ejb.RenamingsEJB;
import ru.edu.pgtk.weducation.entity.CourseWorkMark;
import ru.edu.pgtk.weducation.entity.FinalMark;
import ru.edu.pgtk.weducation.entity.FinalPracticMark;
import ru.edu.pgtk.weducation.entity.GOSMark;
import ru.edu.pgtk.weducation.entity.Person;
import ru.edu.pgtk.weducation.entity.Renaming;
import ru.edu.pgtk.weducation.entity.School;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.utils.Utils;

/**
 *
 * @author user
 */
@Stateless
public class ReferenceBlanksEJB {

  // поток байт в котором будет "собираться" отчет.
  private final ByteArrayOutputStream stream;
  private BaseFont baseFont;
  private Font regularFont;
  private Font smallFont;
  private Font bigFont;
  private Font hugeFont;
  @EJB
  private FinalMarksEJB finalMarks;
  @EJB
  private FinalPracticMarksEJB practicMarks;
  @EJB
  private GOSMarksEJB gosMarks;
  @EJB
  private CourseWorkMarksEJB courseWorks;
  @EJB
  private RenamingsEJB renamings;

  /**
   * Преобразует миллиметры в пункты из расчета, что один пункт равен 1/72
   * дюйма.
   *
   * @param milimeters миллиметры
   * @return дробное число пунктов
   */
  private float getPt(float milimeters) {
    return milimeters * 72 / 25.4f;
  }

  /**
   * Изготавливает "обертку" для элемента, помещая его в таблицу с одной
   * ячейкой.
   *
   * @param element - элемент для обертывания
   * @param minHeight - минимальная высота таблицы-обертки
   * @return объект типа PdfPTable
   */
  private PdfPTable wrapElement(final Element element, final float minHeight) {
    PdfPTable wrapperTable = new PdfPTable(1);
    wrapperTable.setWidthPercentage(100.0f);
    PdfPCell wrapperCell = new PdfPCell();
    wrapperCell.setBorder(PdfPCell.NO_BORDER);
    wrapperCell.setMinimumHeight(minHeight);
    wrapperCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
    wrapperCell.addElement(element);
    wrapperTable.addCell(wrapperCell);
    return wrapperTable;
  }

  /**
   * Готовит параграф с требуемым выравниванием, текстом и шрифтом
   *
   * @param text текст
   * @param font шрифт
   * @param alignment выравнивание
   * @return объект типа paragraph
   */
  private Paragraph getParagraph(String text, Font font, int alignment) {
    Paragraph result = new Paragraph(text, font);
    result.setAlignment(alignment);
    result.setLeading(font.getSize() * 1.1f);
    return result;
  }

  /**
   * Выводит текст в заданные координаты относительно левого нижнего угла
   *
   * @param canvas канва документа
   * @param font используемый шрифт
   * @param text текст
   * @param x координата X в миллиметрах
   * @param y координата Y в миллиметрах
   */
  private void putText(final PdfContentByte canvas, final Font font,
          final String text, final float x, final float y) {
    canvas.saveState();
    canvas.beginText();
    canvas.moveText(getPt(x), getPt(y));
    canvas.setFontAndSize(font.getBaseFont(), font.getSize());
    canvas.showText(text);
    canvas.endText();
    canvas.restoreState();
  }

  /**
   * Подготавливает шрифты для использования в документе
   *
   * @param regularSize размер обычного шрифта
   * @param smallSize размер маленького шрифта
   * @throws IOException
   * @throws DocumentException
   */
  private void prepareFonts(final int regularSize, final int smallSize)
          throws IOException, DocumentException {
    baseFont = BaseFont.createFont("fonts/times.ttf", BaseFont.IDENTITY_H,
            BaseFont.EMBEDDED);
    regularFont = new Font(baseFont, regularSize);
    smallFont = new Font(baseFont, smallSize);
    bigFont = new Font(baseFont, 16);
    hugeFont = new Font(baseFont, 20);
  }

  /**
   * Готовит таблицу оценок за курсовые проекты
   *
   * @param marks
   * @return
   * @throws DocumentException
   */
  private PdfPTable prepareCourseWorkTable(final List<CourseWorkMark> marks)
          throws DocumentException {
    PdfPTable table = new PdfPTable(2);
    table.setWidthPercentage(100.0f);
    table.setWidths(new int[]{12, 2});
    PdfPCell nameCell;
    PdfPCell markCell;
    // Выводим заголовок таблицы
    nameCell = new PdfPCell(getParagraph("Курсовые проекты (работы)",
            smallFont, Paragraph.ALIGN_CENTER));
    nameCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//			nameCell.setPaddingRight(getPt(3));
//			nameCell.setLeading(0.5f, 0.6f);
    nameCell.setBorder(PdfPCell.NO_BORDER);
    markCell = new PdfPCell(getParagraph("Оценка",
            smallFont, Paragraph.ALIGN_CENTER));
    markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//			markCell.setLeading(0.5f, 0.6f);
    markCell.setBorder(PdfPCell.NO_BORDER);
    table.addCell(nameCell);
    table.addCell(markCell);
    // Выводим список курсовых проектов
    for (CourseWorkMark mark : marks) {
      nameCell = new PdfPCell(getParagraph(mark.getSubject().getFullName()
              + " (" + mark.getTheme() + ")",
              smallFont, Paragraph.ALIGN_LEFT));
      nameCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
//			nameCell.setPaddingRight(getPt(3));
//			nameCell.setLeading(0.5f, 0.6f);
      nameCell.setBorder(PdfPCell.NO_BORDER);
      markCell = new PdfPCell(getParagraph(Utils.getMarkString(mark.getMark()),
              smallFont, Paragraph.ALIGN_CENTER));
      markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//			markCell.setLeading(0.5f, 0.6f);
      markCell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(nameCell);
      table.addCell(markCell);
    }
    return table;
  }

  private void prepareMarkTable(final List<MarkItem> marks,
          PdfPTable table) throws DocumentException {

    boolean firstPage = true;
    PdfPCell nameCell;
    PdfPCell hoursCell;
    PdfPCell markCell;

    // Формируем заголовок
    nameCell = new PdfPCell(getParagraph("Наименование учебных предметов, курсов, дисциплин (модулей), практик",
            smallFont, Paragraph.ALIGN_CENTER));
    nameCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
    nameCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
    nameCell.setPaddingRight(getPt(3));
    nameCell.setLeading(0.5f, 0.7f);
    nameCell.setBorder(PdfPCell.NO_BORDER);
    hoursCell = new PdfPCell(getParagraph("Общее количество часов", smallFont, Paragraph.ALIGN_CENTER));
    hoursCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
    hoursCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
    hoursCell.setLeading(0.5f, 0.7f);
    hoursCell.setBorder(PdfPCell.NO_BORDER);
    markCell = new PdfPCell(getParagraph("Оценка", smallFont, Paragraph.ALIGN_CENTER));
    markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
    markCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
    markCell.setLeading(0.5f, 0.7f);
    markCell.setBorder(PdfPCell.NO_BORDER);
    table.addCell(nameCell);
    table.addCell(hoursCell);
    table.addCell(markCell);

    // выводим список оценок
    for (MarkItem mark : marks) {
      nameCell = new PdfPCell(getParagraph(mark.subject,
              smallFont, Paragraph.ALIGN_LEFT));
      nameCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
      nameCell.setPaddingRight(getPt(3));
      nameCell.setLeading(0.5f, 0.7f);
      nameCell.setBorder(PdfPCell.NO_BORDER);
      hoursCell = new PdfPCell(getParagraph(mark.load, smallFont, Paragraph.ALIGN_CENTER));
      hoursCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      hoursCell.setLeading(0.5f, 0.7f);
      hoursCell.setBorder(PdfPCell.NO_BORDER);
      markCell = new PdfPCell(getParagraph(mark.mark, smallFont, Paragraph.ALIGN_CENTER));
      markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      markCell.setLeading(0.5f, 0.7f);
      markCell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(nameCell);
      table.addCell(hoursCell);
      table.addCell(markCell);
    }
  }

  /**
   * Готовит таблицу переименований учебного заведения.
   *
   * @param renamingList
   * @return
   * @throws DocumentException
   */
  private PdfPTable prepareRenamingTable(List<Renaming> renamingList)
          throws DocumentException {
    PdfPTable table = new PdfPTable(1);
    table.setWidthPercentage(100.0f);
    PdfPCell cell;
    for (Renaming item : renamingList) {
      cell = new PdfPCell();
      cell.setBorder(PdfPCell.NO_BORDER);
      cell.addElement(getParagraph(
              "Образовательная организация переименована в "
              + Utils.getYear(item.getDate()) + " году;", smallFont,
              Paragraph.ALIGN_LEFT));
      cell.addElement(getParagraph(
              "старое полное наименование образовательной организации: "
              + item.getOldName(), smallFont,
              Paragraph.ALIGN_LEFT));
      table.addCell(cell);
    }
    return table;
  }

  /**
   * Готовит отчет в виде pdf документа.
   *
   * @param card Личная карточка, требуемая для отчета
   * @return массив байт (содержимое pdf-документа)
   */
  public byte[] getBlank(final StudyCard card) {
    try {
      prepareFonts(12, 8);
      Document document = new Document(PageSize.A4, 15f, 15f,
              75f, 15f);
      PdfWriter writer = PdfWriter.getInstance(document, stream);
      document.open();
      document.addTitle("Справка об успеваемости");
      document.addAuthor("weducation project");

      // Данные для вывода (возможно лучше тут считать всё с карточки)
      School scl = card.getSchool();
      Speciality spc = card.getSpeciality();
      Person psn = card.getPerson();
      String sclName = scl.getFullName() + "\n" + scl.getPlace() + "\n\n";
      String comissionDate = Utils.getDateString(card.getComissionDate()) + " года";
      String diplomeDate = Utils.getDateString(card.getDiplomeDate()) + " года";
      String birthDate = Utils.getDateString(psn.getBirthDate()) + " года";
      String schoolDirector = scl.getDirector();
      String speciality = spc.getFullName();
      String studyForm = card.getExtramuralString();
      String oldDocument = card.getDocumentName() + ", "
              + Utils.getYear(card.getDocumentDate()) + " год.";
      /**
       * Формируем таблицу оценок, выводимых в справку
       */
      List<MarkItem> marks = new ArrayList<>();
      // Добавляем итоговые оценки
      int aload = 0;
      int mload = 0;
      // Сначала добавляем дисциплины, не входящие в модули
      for (FinalMark fm : finalMarks.fetchOnlySubjects(card)) {
        marks.add(new MarkItem(fm));
        aload += fm.getAuditoryLoad();
        mload += fm.getMaximumLoad();
      }
      // Потом добавим модули с их содержимым
      for (FinalMark fm : finalMarks.fetchModules(card)) {
        marks.add(new MarkItem(fm));
        marks.add(new MarkItem("в том числе:", "", ""));
        // Дисциплины конкретного модуля
        for (FinalMark sfm : finalMarks.fetchModuleSubjects(card, fm.getModule())) {
          marks.add(new MarkItem(sfm));
          aload += fm.getAuditoryLoad();
          mload += fm.getMaximumLoad();
        }
      }
      marks.add(new MarkItem("ВСЕГО часов теоретического обучения:", mload, 0));
      marks.add(new MarkItem("в том числе аудиторных часов:", aload, 0));
      marks.add(new MarkItem("Практика", practicMarks.getSummaryLoad(card), 0));
      marks.add(new MarkItem("в том числе:", "", ""));
      // Добавляем оценки за практику
      for (FinalPracticMark pm : practicMarks.fetchAll(card)) {
        marks.add(new MarkItem(pm));
      }
      // TODO Проверить, надо ли добавлять эти данные?
//      marks.add(new MarkItem("Государственная итоговая аттестация", card.getDiplomeLength(), 0));
//      marks.add(new MarkItem("в том числе:", "", ""));
//      String title = (card.isGosExam()) ? "Итоговый междисциплинарный государственный экзамен"
//              : ("Дипломный проект на тему \"" + card.getDiplomeTheme() + "\"");
//      marks.add(new MarkItem(title, "x", Utils.getMarkString(card.getDiplomeMark())));
      marks.add(new MarkItem("Государственные экзамены", "", ""));
      marks.add(new MarkItem("в том числе:", "", ""));
      for (GOSMark gm : gosMarks.fetchAll(card)) {
        marks.add(new MarkItem(gm));
      }

      // ============================================================
      // Бланк справки
      // ============================================================
      // Основная таблица из двух столбцов. Так мы реализуем колонки
      PdfPTable mainTable = new PdfPTable(2);
      mainTable.setWidthPercentage(100.0f);
      mainTable.setWidths(new int[]{3, 6});
      mainTable.setSpacingBefore(20f);
      // Маленькая колонка (РОССИЙСКАЯ ФЕДЕРАЦИЯ...)
      PdfPCell innerCell1 = new PdfPCell();
      innerCell1.setBorder(PdfPCell.NO_BORDER);
      innerCell1.addElement(getParagraph("РОССИЙСКАЯ\nФЕДЕРАЦИЯ\n\n\n", regularFont, Paragraph.ALIGN_CENTER));
      innerCell1.addElement(getParagraph(sclName, regularFont, Paragraph.ALIGN_CENTER));

//      innerCell1.addElement(wrapElement(new Phrase("РОССИЙСКАЯ ФЕДЕРАЦИЯ", smallFont), 100));
//      innerCell1.addElement(wrapElement(getParagraph(sclName, regularFont, Paragraph.ALIGN_CENTER), 230));
//      innerCell1.addElement(wrapElement(
//              getParagraph(card.getRegistrationNumber(), regularFont,
//                      Paragraph.ALIGN_CENTER), 50));
//      innerCell1.addElement(wrapElement(
//              getParagraph(diplomeDate, regularFont,
//                      Paragraph.ALIGN_CENTER), 20));

      // Добавим данные о руководителе организации
      innerCell1.addElement(wrapElement(
              getParagraph(schoolDirector, regularFont,
                      Paragraph.ALIGN_RIGHT), 50.0f));

      // Большая колонка (СВЕДЕНИЯ О ЛИЧНОСТИ ОБЛАДАТЕЛЯ СПРАВКИ...)
      PdfPCell innerCell2 = new PdfPCell();
      innerCell2.setBorder(PdfPCell.NO_BORDER);
      innerCell2.addElement(wrapElement(new Phrase("", smallFont), 20));
      innerCell2
              .addElement(wrapElement(
                              getParagraph(psn.getFirstName(), regularFont,
                                      Paragraph.ALIGN_CENTER), 50));
      innerCell2.addElement(wrapElement(
              getParagraph(psn.getMiddleName(), regularFont, Paragraph.ALIGN_CENTER),
              50));
      innerCell2.addElement(wrapElement(
              getParagraph(psn.getLastName(), regularFont,
                      Paragraph.ALIGN_CENTER), 50));
      innerCell2.addElement(wrapElement(
              getParagraph(birthDate, regularFont,
                      Paragraph.ALIGN_CENTER), 50));
      innerCell2.addElement(wrapElement(
              getParagraph(oldDocument,
                      regularFont, Paragraph.ALIGN_LEFT), 130));
      innerCell2.addElement(wrapElement(
              getParagraph(spc.getKvalification(), regularFont,
                      Paragraph.ALIGN_CENTER), 40));
      innerCell2
              .addElement(wrapElement(
                              getParagraph(speciality, regularFont, Paragraph.ALIGN_CENTER), 30));

      // Добавим дополнительные сведения в колонку
      PdfPTable renamingTable = wrapElement(
              prepareRenamingTable(renamings.findByDates(card.getBeginDate(), card.getEndDate())), 80.0f);
      renamingTable.setSpacingBefore(20.0f);
      innerCell2.addElement(renamingTable);

      // Добавим таблицу курсовых в колонку
      PdfPTable courseWorkTable = wrapElement(
              prepareCourseWorkTable(courseWorks.fetchAll(card)), 290.0f);
      innerCell2.addElement(courseWorkTable);

      // Добавляем колонки
      mainTable.addCell(innerCell1);
      mainTable.addCell(innerCell2);
      document.add(mainTable);

      // Вторая страница
      document.newPage();

      PdfPTable marksTable = new PdfPTable(3);
      marksTable.setWidthPercentage(100.0f);
      marksTable.setTotalWidth(getPt(135));
      marksTable.setWidths(new int[]{10, 2, 2});
      marksTable.setSpacingBefore(35);
      // Заполняем данными страницы 2 и 3
      prepareMarkTable(marks, marksTable);
      document.add(marksTable);
      document.close();
    } catch (IOException | DocumentException e) {
      throw new EJBException(e.getMessage());
    }
    return stream.toByteArray();
  }

  public ReferenceBlanksEJB() {
    stream = new ByteArrayOutputStream();
  }
}
