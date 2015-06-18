package ru.edu.pgtk.weducation.reports;

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
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import ru.edu.pgtk.weducation.ejb.GroupSemestersEJB;
import ru.edu.pgtk.weducation.ejb.MonthMarksEJB;
import ru.edu.pgtk.weducation.ejb.SchoolsEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.GroupSemester;
import ru.edu.pgtk.weducation.entity.MonthMark;
import ru.edu.pgtk.weducation.entity.School;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.Subject;
import static ru.edu.pgtk.weducation.reports.PDFUtils.getParagraph;
import static ru.edu.pgtk.weducation.reports.PDFUtils.getPt;
import static ru.edu.pgtk.weducation.reports.Utils.getMonthString;

@Stateless
@Named("monthMarksSheetEJB")
public class MonthMarksSheetEJB {

  private final ByteArrayOutputStream stream;
  private BaseFont baseFont;
  private Font bigFont;
  private Font regularFont;
  private Font smallFont;
  @EJB
  private transient SchoolsEJB schools;
  @EJB
  private transient StudyCardsEJB cards;
  @EJB
  private transient GroupSemestersEJB groupSemesters;
  @EJB
  private transient SubjectsEJB subjects;
  @EJB
  private transient MonthMarksEJB marks;

  public MonthMarksSheetEJB() {
    try {
      stream = new ByteArrayOutputStream();
      baseFont = BaseFont.createFont("fonts/times.ttf", BaseFont.IDENTITY_H,
        BaseFont.EMBEDDED);
      smallFont = new Font(baseFont, 7);
      regularFont = new Font(baseFont, 10);
      bigFont = new Font(baseFont, 16);
    } catch (IOException | DocumentException e) {
      throw new EJBException("Exception with message " + e.getMessage());
    }
  }

  /**
   * Формирует ведомость успеваемости за месяц для группы.
   *
   * @param group собственно, группа
   * @param year год, за который формируем ведомость
   * @param month месяц, за который формируем ведомость
   * @param empty ведомость будет без оценок (для заполнения)
   * @return массив байт, представляющий собой PDF сгенерированный документ.
   */
  public byte[] getReport(final StudyGroup group, final int year, final int month, final boolean empty) {
    try {
      // создадим новый лист с размерами A4 и отступами слева и справа по 5 мм, а сверху и снизу - по 10
      Document document = new Document(PageSize.A4, getPt(5), getPt(5), getPt(10), getPt(10));
      PdfWriter writer = PdfWriter.getInstance(document, stream);
      document.open();
      document.addTitle("Ведомость " + ((empty) ? "аттестации" : "успеваемости") + " за месяц");
      document.addAuthor("weducation project");
      School scl = schools.getCurrent();
      document.add(getParagraph(scl.getFullName(), regularFont, Paragraph.ALIGN_CENTER));
      document.add(getParagraph("Ведомость " + ((empty) ? "аттестации" : "успеваемости"),
        bigFont, Paragraph.ALIGN_CENTER));
      String description = "группы " + group.getName() + " " + group.getCourse() + "-го курса "
        + (group.isExtramural() ? "заочной" : "очной") + " формы обучения за " + getMonthString(month) + " " + year + "-го года.";
      document.add(getParagraph(description, regularFont, Paragraph.ALIGN_CENTER));
      String speciality = "Специальность: " + group.getSpeciality().getFullName();
      document.add(getParagraph(speciality, regularFont, Paragraph.ALIGN_CENTER));
      int date = year * 1000 + month * 10 + 3;
      int course = 1;
      int semester = 1;
      for (GroupSemester gs : groupSemesters.fetchAll(group)) {
        // Ищем первый подходящий семестр
        if ((gs.getBeginDate() <= date) && (gs.getEndDate() >= date)) {
          // Если нашли - запоминаем курс, семестр и прерываем цикл
          course = gs.getCourse();
          semester = gs.getSemester();
          break;
        }
      }
      // Тут мы уже должны знать курс и семестр!
      // Настало время узнать список дисциплин
      List<Subject> subjectList = subjects.fetch(group, course, semester);
      // Теперь можно создавать таблицу
      // Количество полей будет на 5 больше, чем кол-во дисциплин
      PdfPTable table = new PdfPTable(subjectList.size() + 5);
      table.setWidthPercentage(100.0f);
      table.setSpacingBefore(getPt(5));
      table.setSpacingAfter(getPt(5));
      // Создадим массив целых чисел для указания размерности столбцов
      // Установим размер столбца ФИО в пять раз больше размером!
      int[] sizes = new int[subjectList.size() + 5];
      for (int i = 0; i < sizes.length; i++) {
        sizes[i] = (i == 1) ? 5 : 1;
      }
      table.setWidths(sizes);
      // Добавляем строки таблицы
      PdfPCell numberCell = new PdfPCell(getParagraph("№", regularFont, Paragraph.ALIGN_CENTER));
      numberCell.setRowspan(2);
      numberCell.setMinimumHeight(getPt(20));
      numberCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      table.addCell(numberCell);
      PdfPCell nameCell = new PdfPCell(getParagraph("Фамилия имя отчество", regularFont, Paragraph.ALIGN_CENTER));
      nameCell.setRowspan(2);
      nameCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      nameCell.setMinimumHeight(getPt(20));
      nameCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      table.addCell(nameCell);
      for (Subject sb : subjectList) {
        PdfPCell cell = new PdfPCell(getParagraph(sb.getShortName(), smallFont, Paragraph.ALIGN_CENTER));
        cell.setRowspan(2);
        cell.setRotation(90);
        cell.setMinimumHeight(getPt(20));
        table.addCell(cell);
      }
      // Кол-во пропусков пока не реализовано
      PdfPCell missingCell = new PdfPCell(getParagraph("Пропуски", regularFont, Paragraph.ALIGN_CENTER));
      missingCell.setColspan(3);
      missingCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      table.addCell(missingCell);
      PdfPCell legalCell = new PdfPCell(getParagraph("Уваж.", smallFont, Paragraph.ALIGN_CENTER));
      legalCell.setMinimumHeight(getPt(20));
      legalCell.setRotation(90);
      table.addCell(legalCell);
      PdfPCell illegalCell = new PdfPCell(getParagraph("Неув.", smallFont, Paragraph.ALIGN_CENTER));
      illegalCell.setMinimumHeight(getPt(20));
      illegalCell.setRotation(90);
      table.addCell(illegalCell);
      PdfPCell allCell = new PdfPCell(getParagraph("Всего", smallFont, Paragraph.ALIGN_CENTER));
      allCell.setMinimumHeight(getPt(20));
      allCell.setRotation(90);
      table.addCell(allCell);
      int row = 1;
      for (StudyCard sc : cards.findByGroup(group)) {
        if (!sc.isRemanded() && sc.isActive()) {
          numberCell = new PdfPCell(getParagraph("" + row++, regularFont, Paragraph.ALIGN_CENTER));
          table.addCell(numberCell);
          nameCell = new PdfPCell(getParagraph(sc.getPerson().getShortName(),
            regularFont, Paragraph.ALIGN_LEFT));
          table.addCell(nameCell);
          for (Subject sb : subjectList) {
            PdfPCell cell = new PdfPCell();
            if (!empty) {
              try {
                MonthMark mark = marks.get(sc, sb, year, month);
                cell.addElement(getParagraph(mark.getMark() + "", regularFont, Paragraph.ALIGN_CENTER));
              } catch (Exception e) {
                // Ничего не делаем, ячейка просто будет пустой
              }
            }
            table.addCell(cell);
          }
          // Кол-во пропусков пока не реализовано
          legalCell = new PdfPCell();
          table.addCell(legalCell);
          illegalCell = new PdfPCell();
          table.addCell(illegalCell);
          allCell = new PdfPCell();
          table.addCell(allCell);
        }
      }
      numberCell = new PdfPCell(getParagraph("Подписи преподавателей", regularFont, Paragraph.ALIGN_CENTER));
      numberCell.setColspan(2);
      numberCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      numberCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      numberCell.setMinimumHeight(getPt(20));
      table.addCell(numberCell);
      for (Subject sb : subjectList) {
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(getPt(20));
        table.addCell(cell);
      }
      // Кол-во пропусков итоговое
      legalCell = new PdfPCell();
      legalCell.setMinimumHeight(getPt(20));
      table.addCell(legalCell);
      illegalCell = new PdfPCell();
      illegalCell.setMinimumHeight(getPt(20));
      table.addCell(illegalCell);
      allCell = new PdfPCell();
      allCell.setMinimumHeight(getPt(20));
      table.addCell(allCell);
//      table.completeRow();
//      table.setComplete(true);
      document.add(table);

      document.add(getParagraph("\n                Зав. отделением ________________________________",
        regularFont, Paragraph.ALIGN_LEFT));
      document.close();
      return stream.toByteArray();
    } catch (Exception e) {
      throw new EJBException("Exception class " + e.getClass().getName() + " with message " + e.getMessage());
    }
  }
}
