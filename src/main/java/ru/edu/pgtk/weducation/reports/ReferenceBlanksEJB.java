package ru.edu.pgtk.weducation.reports;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
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
import static ru.edu.pgtk.weducation.reports.PDFUtils.getParagraph;
import static ru.edu.pgtk.weducation.reports.PDFUtils.getPt;
import static ru.edu.pgtk.weducation.reports.PDFUtils.wrapElement;
import ru.edu.pgtk.weducation.utils.Utils;

/**
 * Класс, формирующий pdf-документ справки об обучении
 *
 * @author Воронин Леонид
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
   * Подготавливает шрифты для использования в документе
   *
   * @throws IOException
   * @throws DocumentException
   */
  private void prepareFonts()
          throws IOException, DocumentException {
    baseFont = BaseFont.createFont("fonts/times.ttf", BaseFont.IDENTITY_H,
            BaseFont.EMBEDDED);
    smallFont = new Font(baseFont, 8);
    regularFont = new Font(baseFont, 12);
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
    table.setSpacingBefore(10f);
    PdfPCell nameCell;
    PdfPCell markCell;
    // Выводим заголовок таблицы
    nameCell = new PdfPCell(getParagraph("Курсовые проекты (работы)",
            smallFont, Paragraph.ALIGN_CENTER));
    nameCell.setMinimumHeight(20f);
    nameCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
    nameCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
//			nameCell.setPaddingRight(getPt(3));
//			nameCell.setLeading(0.5f, 0.6f);
//    nameCell.setBorder(PdfPCell.NO_BORDER);
    nameCell.setBorderColor(BaseColor.BLACK);
    nameCell.setBorderWidth(.5f);
    markCell = new PdfPCell(getParagraph("Оценка",
            smallFont, Paragraph.ALIGN_CENTER));
    markCell.setMinimumHeight(20f);
    markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
    markCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
//			markCell.setLeading(0.5f, 0.6f);
    markCell.setBorderColor(BaseColor.BLACK);
    markCell.setBorderWidth(.5f);
//    markCell.setBorder(PdfPCell.NO_BORDER);
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
      nameCell.setBorderColor(BaseColor.BLACK);
      nameCell.setBorderWidth(.5f);
      markCell = new PdfPCell(getParagraph(Utils.getMarkString(mark.getMark()),
              smallFont, Paragraph.ALIGN_CENTER));
      markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//			markCell.setLeading(0.5f, 0.6f);
      markCell.setBorderColor(BaseColor.BLACK);
      markCell.setBorderWidth(.5f);
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
    nameCell.setMinimumHeight(30f);
    nameCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
    nameCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
    nameCell.setBorderColor(BaseColor.BLACK);
    nameCell.setBorderWidth(.5f);
    hoursCell = new PdfPCell(getParagraph("Общее количество часов", smallFont, Paragraph.ALIGN_CENTER));
    hoursCell.setMinimumHeight(30f);
    hoursCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
    hoursCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
    hoursCell.setBorderColor(BaseColor.BLACK);
    hoursCell.setBorderWidth(.5f);
    markCell = new PdfPCell(getParagraph("Оценка", smallFont, Paragraph.ALIGN_CENTER));
    markCell.setMinimumHeight(30f);
    markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
    markCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
    markCell.setBorderColor(BaseColor.BLACK);
    markCell.setBorderWidth(.5f);
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
      nameCell.setBorderColor(BaseColor.BLACK);
      nameCell.setBorderWidth(.5f);
      hoursCell = new PdfPCell(getParagraph(mark.load, smallFont, Paragraph.ALIGN_CENTER));
      hoursCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      hoursCell.setLeading(0.5f, 0.7f);
      hoursCell.setBorderColor(BaseColor.BLACK);
      hoursCell.setBorderWidth(.5f);
      markCell = new PdfPCell(getParagraph(mark.mark, smallFont, Paragraph.ALIGN_CENTER));
      markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      markCell.setLeading(0.5f, 0.7f);
      markCell.setBorderColor(BaseColor.BLACK);
      markCell.setBorderWidth(.5f);
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
      prepareFonts();
      Document document = new Document(PageSize.A4, getPt(5), getPt(5),
              getPt(10), getPt(5));
      document.open();
      document.addTitle("Справка об успеваемости");
      document.addAuthor("weducation project");

      // Данные для вывода (возможно лучше тут считать всё с карточки)
      School scl = card.getSchool();
      Speciality spc = card.getSpeciality();
      Person psn = card.getPerson();
      String sclName = scl.getFullName() + "\n" + scl.getPlace();
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
//      mainTable.setSpacingBefore(20f);
      // Маленькая колонка (РОССИЙСКАЯ ФЕДЕРАЦИЯ...)
      PdfPCell innerCell1 = new PdfPCell();
      innerCell1.setBorder(PdfPCell.NO_BORDER);
      innerCell1.addElement(getParagraph("РОССИЙСКАЯ\nФЕДЕРАЦИЯ", regularFont, Paragraph.ALIGN_CENTER));
      innerCell1.addElement(wrapElement(
              getParagraph(sclName, regularFont, Paragraph.ALIGN_CENTER),
              150, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell1.addElement(getParagraph("\nСПРАВКА", hugeFont, Paragraph.ALIGN_CENTER));
      innerCell1.addElement(getParagraph("ОБ УСПЕВАЕМОСТИ", bigFont, Paragraph.ALIGN_CENTER));
      innerCell1.addElement(wrapElement(
              getParagraph(card.getRegistrationNumber(), regularFont, Paragraph.ALIGN_CENTER),
              50, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell1.addElement(getParagraph("(регистрационный номер)", smallFont, Paragraph.ALIGN_CENTER));
      innerCell1.addElement(wrapElement(
              getParagraph(diplomeDate, regularFont, Paragraph.ALIGN_CENTER),
              30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell1.addElement(getParagraph("(дата выдачи)", smallFont, Paragraph.ALIGN_CENTER));
      innerCell1.addElement(wrapElement(
              getParagraph(card.getRemandCommand(), regularFont, Paragraph.ALIGN_CENTER),
              30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell1.addElement(getParagraph("(номер приказа)", smallFont, Paragraph.ALIGN_CENTER));
      innerCell1.addElement(wrapElement(
              getParagraph(comissionDate, regularFont, Paragraph.ALIGN_CENTER),
              30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell1.addElement(getParagraph("(дата приказа)", smallFont, Paragraph.ALIGN_CENTER));
      innerCell1.addElement(wrapElement(
              getParagraph(card.getRemandReason(), regularFont, Paragraph.ALIGN_CENTER),
              150, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell1.addElement(getParagraph("(причина отчисления)", smallFont, Paragraph.ALIGN_CENTER));
      // Добавим данные о руководителе организации
      innerCell1.addElement(wrapElement(
              getParagraph("Руководитель образовательной организации", regularFont, Paragraph.ALIGN_CENTER),
              130, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell1.addElement(wrapElement(
              getParagraph(schoolDirector, regularFont, Paragraph.ALIGN_CENTER),
              70, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));

      // Большая колонка (СВЕДЕНИЯ О ЛИЧНОСТИ ОБЛАДАТЕЛЯ СПРАВКИ...)
      PdfPCell innerCell2 = new PdfPCell();
      innerCell2.setBorder(PdfPCell.NO_BORDER);
      innerCell2.addElement(getParagraph("СВЕДЕНИЯ О ЛИЧНОСТИ ОБЛАДАТЕЛЯ СПРАВКИ", regularFont, Paragraph.ALIGN_CENTER));
      innerCell2.addElement(wrapElement(
              getParagraph(psn.getFullName(), regularFont, Paragraph.ALIGN_CENTER),
              30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell2.addElement(getParagraph("(фамилия, имя, отчество)", smallFont, Paragraph.ALIGN_CENTER));
      innerCell2.addElement(wrapElement(
              getParagraph(birthDate, regularFont, Paragraph.ALIGN_CENTER),
              30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell2.addElement(getParagraph("(дата рождения)", smallFont, Paragraph.ALIGN_CENTER));
      innerCell2.addElement(wrapElement(
              getParagraph(oldDocument, regularFont, Paragraph.ALIGN_CENTER),
              30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell2.addElement(getParagraph("(предыдущий документ об образовании)", smallFont, Paragraph.ALIGN_CENTER));
      innerCell2.addElement(wrapElement(
              getParagraph("СВЕДЕНИЯ ОБ ОБРАЗОВАТЕЛЬНОЙ ПРОГРАММЕ "
                      + "СРЕДНЕГО ПРОФЕССИОНАЛЬНОГО ОБРАЗОВАНИЯ И О КВАЛИФИКАЦИИ", regularFont, Paragraph.ALIGN_CENTER),
              50, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell2.addElement(wrapElement(
              getParagraph(speciality, regularFont, Paragraph.ALIGN_CENTER),
              50, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell2.addElement(getParagraph("(специальность)", smallFont, Paragraph.ALIGN_CENTER));
      innerCell2.addElement(wrapElement(
              getParagraph(spc.getSpecialization(), regularFont, Paragraph.ALIGN_CENTER),
              30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell2.addElement(getParagraph("(специализация)", smallFont, Paragraph.ALIGN_CENTER));
      innerCell2.addElement(wrapElement(
              getParagraph(studyForm, regularFont, Paragraph.ALIGN_CENTER),
              30, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell2.addElement(getParagraph("(форма обучения)", smallFont, Paragraph.ALIGN_CENTER));
      // Добавим дополнительные сведения в колонку
      innerCell2.addElement(wrapElement(
              getParagraph("ДОПОЛНИТЕЛЬНЫЕ СВЕДЕНИЯ", regularFont, Paragraph.ALIGN_CENTER),
              50, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      innerCell2.addElement(wrapElement(prepareRenamingTable(
              renamings.findByDates(card.getBeginDate(), card.getEndDate())), 150.0f));
      // Добавим таблицу курсовых в колонку
      innerCell2.addElement(wrapElement(
              getParagraph("СВЕДЕНИЯ О СОДЕРЖАНИИ И РЕЗУЛЬТАТАХ ОСВОЕНИЯ "
                      + "ОБРАЗОВАТЕЛЬНОЙ ПРОГРАММЫ СРЕДНЕГО ПРОФЕССИОНАЛЬНОГО ОБРАЗОВАНИЯ",
                      regularFont, Paragraph.ALIGN_CENTER), 60, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_BOTTOM));
      PdfPTable courseWorkTable = wrapElement(
              prepareCourseWorkTable(courseWorks.fetchAll(card)), 150.0f);
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
