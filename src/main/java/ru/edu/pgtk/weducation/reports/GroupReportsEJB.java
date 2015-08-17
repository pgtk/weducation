package ru.edu.pgtk.weducation.reports;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import ru.edu.pgtk.weducation.ejb.CourseWorkMarksEJB;
import ru.edu.pgtk.weducation.ejb.GroupSemestersEJB;
import ru.edu.pgtk.weducation.ejb.MissingsEJB;
import ru.edu.pgtk.weducation.ejb.MonthMarksEJB;
import ru.edu.pgtk.weducation.ejb.SemesterMarksEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.CourseWorkMark;
import ru.edu.pgtk.weducation.entity.GroupSemester;
import ru.edu.pgtk.weducation.entity.Missing;
import ru.edu.pgtk.weducation.entity.MonthMark;
import ru.edu.pgtk.weducation.entity.School;
import ru.edu.pgtk.weducation.entity.SemesterMark;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.Subject;
import static ru.edu.pgtk.weducation.reports.PDFUtils.getParagraph;
import static ru.edu.pgtk.weducation.reports.PDFUtils.getPt;
import static ru.edu.pgtk.weducation.reports.Utils.getMonthString;

/**
 * EJB компонент для генерации различных отчетов по группе.
 */
@Path("/group")
@Stateless
@RequestScoped
public class GroupReportsEJB {

  @Inject
  private transient SubjectsEJB subjects;
  @Inject
  private transient StudyGroupsEJB groups;
  @Inject
  private transient StudyCardsEJB cards;
  @Inject
  private transient MonthMarksEJB marks;
  @Inject
  private transient MissingsEJB missings;
  @Inject
  private transient CourseWorkMarksEJB cmarks;
  @Inject
  private transient SemesterMarksEJB smarks;
  @Inject
  private transient GroupSemestersEJB groupSemesters;
  @Inject
  private School school;
  private String schoolName;
  private boolean error;
  private String errorMessage;
  private BaseFont baseFont;
  private Font bigFont;
  private Font regularFont;
  private Font smallFont;

  @PostConstruct
  private void initBean() {
    try {
      baseFont = BaseFont.createFont("fonts/times.ttf", BaseFont.IDENTITY_H,
        BaseFont.EMBEDDED);
      smallFont = new Font(baseFont, 7);
      regularFont = new Font(baseFont, 10);
      bigFont = new Font(baseFont, 16);
      if ((null == school) || (null == school.getFullName())) {
        errorMessage = "School name or school is null!";
        error = true;
      } else {
        schoolName = school.getFullName().replace("Прокопьевский", "\nПрокопьевский");
      }
    } catch (DocumentException | IOException e) {
      error = true;
      errorMessage = e.getClass().getSimpleName() + " with message " + e.getMessage();
    }
  }
  
  /**
   * Экзаменационная ведомость.
   * @param groupCode код группы
   * @param course курс
   * @param semester семестр
   * @param subjectCode код дисциплины
   * @return экзаменационная ведомость в виде PDF документа
   */
  @GET
  @Path("{groupId: \\d+}/exam/{course: \\d+}/{semester: \\d+}/{subjectId: \\d+}")
  @Produces("application/pdf")
  public Response examReport(@PathParam("groupId") int groupCode, @PathParam("course") int course,
    @PathParam("semester") int semester, @PathParam("subjectId") int subjectCode) {
    try {
      StudyGroup grp = groups.get(groupCode);
      Subject sub = subjects.get(subjectCode);
      ResponseBuilder response = Response.ok(exam(grp, sub, course, semester));
      return response.build();
    } catch (EJBException e) {
      throw new NotFoundException();
    }
  }

  /**
   * Ведомость сдачи курсовых проектов
   * @param groupCode код группы
   * @param course курс
   * @param semester семестр
   * @param subjectCode код дисциплины
   * @return ведомость сдачи курсовых проектов в виде PDF документа.
   */
  @GET
  @Path("{groupId: \\d+}/cproject/{course: \\d+}/{semester: \\d+}/{subjectId: \\d+}")
  @Produces("application/pdf")
  public Response courseWorkReport(@PathParam("groupId") int groupCode, @PathParam("course") int course,
    @PathParam("semester") int semester, @PathParam("subjectId") int subjectCode) {
    try {
      StudyGroup grp = groups.get(groupCode);
      Subject sub = subjects.get(subjectCode);
      ResponseBuilder response = Response.ok(courseWork(grp, sub, course, semester));
      return response.build();
    } catch (EJBException e) {
      return Response.status(404).build();
    }
  }

  /**
   * Ведомость аттестации за месяц.
   * @param groupCode код группы
   * @param year год
   * @param month месяц
   * @return ведомость аттестации за месяц в виде PDF документа
   */
  @GET
  @Path("{groupId: \\d+}/monthmarks/empty/{year: \\d+}/{month: \\d+}")
  @Produces("application/pdf")
  public Response emptyMonthReport(@PathParam("groupId") int groupCode,
    @PathParam("year") int year, @PathParam("month") int month) {
    try {
      StudyGroup grp = groups.get(groupCode);
      ResponseBuilder response = Response.ok(monthMarks(grp, year, month, true));
      return response.build();
    } catch (EJBException e) {
      return Response.status(404).build();
    }
  }

  /**
   * Ведомость успеваемсти за месяц.
   * @param groupCode код группы
   * @param year год
   * @param month месяц
   * @return ведомость успеваемости в виде PDF документа
   */
  @GET
  @Path("{groupId: \\d+}/monthmarks/filled/{year: \\d+}/{month: \\d+}")
  @Produces("application/pdf")
  public Response filledMonthReport(@PathParam("groupId") int groupCode,
    @PathParam("year") int year, @PathParam("month") int month) {
    try {
      StudyGroup grp = groups.get(groupCode);
      ResponseBuilder response = Response.ok(monthMarks(grp, year, month, false));
      return response.build();
    } catch (Exception e) {
      throw new NotFoundException();
    }
  }

  /**
   * Зачетная ведомость.
   * @param groupCode код группы
   * @param course курс
   * @param semester семестр
   * @return зачетная ведомость в виде PDF документа.
   */
  @GET
  @Path("{groupId: \\d+}/semestermarks/{course: \\d+}/{semester: \\d+}")
  @Produces("application/pdf")
  public Response semesterMarksReport(@PathParam("groupId") int groupCode,
    @PathParam("course") int course, @PathParam("semester") int semester) {
    try {
      StudyGroup grp = groups.get(groupCode);
      return Response.ok(semesterMarks(grp, course, semester, true)).build();
    } catch (Exception e) {
      throw new NotFoundException();
    }
  }
  
  /**
   * Формирует ведомость сдачи курсовых проектов для группы
   *
   * @param group группы
   * @param subject дисциплина
   * @param course курс
   * @param semester семестр
   * @return
   */
  public byte[] courseWork(final StudyGroup group, final Subject subject, final int course, final int semester) {
    try {
      // создадим новый лист с размерами A4 и отступами слева и справа по 5 мм, а сверху и снизу - по 10
      Report document = PDFReport.create(Report.Orientation.PORTRET, "Ведомость сдачи курсовых проектов");
      document.add(getParagraph(schoolName, regularFont, Paragraph.ALIGN_CENTER));
      document.add(getParagraph("\nВедомость сдачи курсовых проектов", bigFont, Paragraph.ALIGN_CENTER));
      String description = "группы " + group.getName() + " " + group.getCourse() + "-го курса "
        + (group.isExtramural() ? "заочной" : "очной") + " формы обучения за " + semester + "-й семестр.";
      document.add(getParagraph(description, regularFont, Paragraph.ALIGN_CENTER));
      String speciality = "Специальность: " + group.getSpeciality().getFullName();
      document.add(getParagraph(speciality, regularFont, Paragraph.ALIGN_CENTER));
      document.add(getParagraph("Дисциплина: " + subject.getFullName(), regularFont, Paragraph.ALIGN_CENTER));
      // Теперь можно создавать таблицу
      PdfPTable table = new PdfPTable(5);
      table.setWidthPercentage(100.0f);
      table.setSpacingBefore(getPt(5));
      table.setSpacingAfter(getPt(5));
      // Создадим массив целых чисел для указания размерности столбцов
      // Установим размер столбца ФИО в пять раз больше размером!
      table.setWidths(new int[]{1, 6, 14, 4, 4});
      // Добавляем строки таблицы
      PdfPCell numberCell = new PdfPCell(getParagraph("№", regularFont, Paragraph.ALIGN_CENTER));
      numberCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      numberCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      table.addCell(numberCell);
      PdfPCell nameCell = new PdfPCell(getParagraph("Фамилия имя отчество", regularFont, Paragraph.ALIGN_CENTER));
      nameCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      nameCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      table.addCell(nameCell);
      PdfPCell themeCell = new PdfPCell(getParagraph("Тема курсового проекта", regularFont, Paragraph.ALIGN_CENTER));
      themeCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      themeCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      table.addCell(themeCell);
      PdfPCell markCell = new PdfPCell(getParagraph("Оценка", regularFont, Paragraph.ALIGN_CENTER));
      markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      table.addCell(markCell);
      PdfPCell signCell = new PdfPCell(getParagraph("Подпись руководителя", regularFont, Paragraph.ALIGN_CENTER));
      signCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      signCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      table.addCell(signCell);
      int row = 1;
      PdfPCell cell = new PdfPCell();
      for (CourseWorkMark cm : cmarks.fetchAll(group, subject, course, semester)) {
        StudyCard sc = cm.getCard();
        if (!sc.isRemanded() && sc.isActive()) {
          numberCell = new PdfPCell(getParagraph("" + row++, regularFont, Paragraph.ALIGN_CENTER));
          table.addCell(numberCell);
          nameCell = new PdfPCell(getParagraph(sc.getPerson().getShortName(),
            regularFont, Paragraph.ALIGN_LEFT));
          table.addCell(nameCell);
          themeCell = new PdfPCell(getParagraph(cm.getTheme(), regularFont, Paragraph.ALIGN_CENTER));
          table.addCell(themeCell);
          for (int i = 0; i < 2; i++) {
            table.addCell(cell);
          }
        }
      }
      document.add(table);
      document.add(getParagraph("\nРуководитель ____________________  ________________________________________",
        regularFont, Paragraph.ALIGN_LEFT));
      document.add(getParagraph("                                                     (подпись)"
        + "                                                                               (ФИО)",
        smallFont, Paragraph.ALIGN_LEFT));
      return document.getData();
    } catch (DocumentException e) {
      throw new InternalServerErrorException("DocumentException: " + e.getMessage());
    }
  }

  /**
   * Формирует экзаменационную ведомость для группы студентов.
   *
   * @param group группа студентов
   * @param subject дисциплина
   * @param course курс
   * @param semester семестр
   * @return
   */
  public byte[] exam(final StudyGroup group, final Subject subject, final int course, final int semester) {
    if (error) {
      throw new InternalServerErrorException(errorMessage);
    }
    try {
      Report report = PDFReport.create(Report.Orientation.PORTRET, "Экзаменационная ведомость");
      report.add(getParagraph(schoolName, regularFont, Paragraph.ALIGN_CENTER));
      report.add(getParagraph("\nЭкзаменационная ведомость", bigFont, Paragraph.ALIGN_CENTER));
      report.add(getParagraph("(для семестровых экзаменов)", regularFont, Paragraph.ALIGN_CENTER));
      String description = "группы " + group.getName() + " " + group.getCourse() + "-го курса "
        + (group.isExtramural() ? "заочной" : "очной") + " формы обучения за " + semester + "-й семестр.";
      report.add(getParagraph(description, regularFont, Paragraph.ALIGN_CENTER));
      String speciality = "Специальность: " + group.getSpeciality().getFullName();
      report.add(getParagraph(speciality, regularFont, Paragraph.ALIGN_CENTER));
      report.add(getParagraph("Дисциплина: " + subject.getFullName(), regularFont, Paragraph.ALIGN_CENTER));
      // Теперь можно создавать таблицу
      PdfPTable table = new PdfPTable(7);
      table.setWidthPercentage(100.0f);
      table.setSpacingBefore(getPt(5));
      table.setSpacingAfter(getPt(5));
      // Создадим массив целых чисел для указания размерности столбцов
      // Установим размер столбца ФИО в пять раз больше размером!
      table.setWidths(new int[]{1, 5, 2, 3, 3, 3, 4});
      // Добавляем строки таблицы
      PdfPCell numberCell = new PdfPCell(getParagraph("№", regularFont, Paragraph.ALIGN_CENTER));
      numberCell.setRowspan(2);
      numberCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      numberCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      table.addCell(numberCell);
      PdfPCell nameCell = new PdfPCell(getParagraph("Фамилия имя отчество", regularFont, Paragraph.ALIGN_CENTER));
      nameCell.setRowspan(2);
      nameCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      nameCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      table.addCell(nameCell);
      PdfPCell biletCell = new PdfPCell(getParagraph("№ билета", regularFont, Paragraph.ALIGN_CENTER));
      biletCell.setRowspan(2);
      biletCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      biletCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      table.addCell(biletCell);
      PdfPCell markCell = new PdfPCell(getParagraph("Оценка за экзамен", regularFont, Paragraph.ALIGN_CENTER));
      markCell.setColspan(3);
      markCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      table.addCell(markCell);
      PdfPCell signCell = new PdfPCell(getParagraph("Подпись экзаменатора", regularFont, Paragraph.ALIGN_CENTER));
      signCell.setRowspan(2);
      signCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      signCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      table.addCell(signCell);
      PdfPCell cell = new PdfPCell(getParagraph("письменно", regularFont, Paragraph.ALIGN_CENTER));
      cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      table.addCell(cell);
      cell = new PdfPCell(getParagraph("устно", regularFont, Paragraph.ALIGN_CENTER));
      cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      table.addCell(cell);
      cell = new PdfPCell(getParagraph("общая", regularFont, Paragraph.ALIGN_CENTER));
      cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      table.addCell(cell);
      int row = 1;
      cell = new PdfPCell();
      for (StudyCard sc : cards.findByGroup(group)) {
        if (!sc.isRemanded() && sc.isActive()) {
          numberCell = new PdfPCell(getParagraph(String.valueOf(row++), regularFont, Paragraph.ALIGN_CENTER));
          table.addCell(numberCell);
          nameCell = new PdfPCell(getParagraph(sc.getPerson().getShortName(),
            regularFont, Paragraph.ALIGN_LEFT));
          table.addCell(nameCell);
          for (int i = 0; i < 5; i++) {
            table.addCell(cell);
          }
        }
      }
      report.add(table);
      report.add(getParagraph("Дата проведения экзамена \"_____\" _________________  ____________г.",
        regularFont, Paragraph.ALIGN_LEFT));
      report.add(getParagraph("\nПисьменного ____________________   начало ____________________   окончание ____________________",
        regularFont, Paragraph.ALIGN_LEFT));
      report.add(getParagraph("\nУстного         ____________________   начало ____________________   окончание ____________________",
        regularFont, Paragraph.ALIGN_LEFT));
      report.add(getParagraph("\nВсего на проведение экзамена: __________________ час ________________ мин",
        regularFont, Paragraph.ALIGN_LEFT));
      report.add(getParagraph("\nЭкзаменатор(ы) ____________________  ________________________________________",
        regularFont, Paragraph.ALIGN_LEFT));
      report.add(getParagraph("                                                     (подпись)"
        + "                                                                               (ФИО)",
        smallFont, Paragraph.ALIGN_LEFT));
      return report.getData();
    } catch (DocumentException e) {
      throw new InternalServerErrorException("DocumentException: " + e.getMessage());
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
   * @Throws InternalServerErrorException при невозможности определить семестр и
   * курс по указанному году и месяцу, а также если вместо группы null
   */
  public byte[] monthMarks(final StudyGroup group,
    final int year, final int month, final boolean empty) {
    if (null == group) {
      throw new InternalServerErrorException("Группа не должна быть NULL!");
    }
    // Получим по месяцу и году семестр и курс
    int course = 0;
    int semester = 0;
    int date = year * 1000 + month * 10 + 3;
    for (GroupSemester gs : groupSemesters.fetchAll(group)) {
      // Ищем первый подходящий семестр
      if ((gs.getBeginDate() <= date) && (gs.getEndDate() >= date)) {
        // Если нашли - запоминаем курс, семестр и прерываем цикл
        course = gs.getCourse();
        semester = gs.getSemester();
        break;
      }
    }
    if ((0 == course) || (0 == semester)) {
      throw new InternalServerErrorException("Такой комбинации месяца и года не соответствует ни один семестр!");
    }
    try {
      String title = "Ведомость " + ((empty) ? "аттестации" : "успеваемости");
      Report document = PDFReport.create(Report.Orientation.PORTRET, title);
      document.add(getParagraph(schoolName, regularFont, Paragraph.ALIGN_CENTER));
      document.add(getParagraph(title, bigFont, Paragraph.ALIGN_CENTER));
      String description = "группы " + group.getName() + " " + group.getCourse() + "-го курса "
        + (group.isExtramural() ? "заочной" : "очной") + " формы обучения за " + getMonthString(month) + " " + year + "-го года.";
      document.add(getParagraph(description, regularFont, Paragraph.ALIGN_CENTER));
      String speciality = "Специальность: " + group.getSpeciality().getFullName();
      document.add(getParagraph(speciality, regularFont, Paragraph.ALIGN_CENTER));
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
      // Кол-во пропусков
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
      int legal = 0;
      int illegal = 0;
      for (StudyCard sc : cards.findByGroup(group)) {
        if (!sc.isRemanded() && sc.isActive()) {
          numberCell = new PdfPCell(getParagraph(String.valueOf(row++), regularFont, Paragraph.ALIGN_CENTER));
          table.addCell(numberCell);
          nameCell = new PdfPCell(getParagraph(sc.getPerson().getShortName(),
            regularFont, Paragraph.ALIGN_LEFT));
          table.addCell(nameCell);
          for (Subject sb : subjectList) {
            PdfPCell cell = new PdfPCell();
            if (!empty) {
              try {
                MonthMark mark = marks.get(sc, sb, year, month);
                cell.addElement(getParagraph(String.valueOf(mark.getMark()), regularFont, Paragraph.ALIGN_CENTER));
              } catch (Exception e) {
                // Ничего не делаем, ячейка просто будет пустой
              }
            }
            table.addCell(cell);
          }
          // Кол-во пропусков
          legalCell = new PdfPCell();
          illegalCell = new PdfPCell();
          allCell = new PdfPCell();
          if (!empty) {
            Missing m = missings.get(sc, year, month);
            legalCell.addElement(getParagraph(String.valueOf(m.getLegal()), regularFont, Paragraph.ALIGN_CENTER));
            illegalCell.addElement(getParagraph(String.valueOf(m.getIllegal()), regularFont, Paragraph.ALIGN_CENTER));
            allCell.addElement(getParagraph(String.valueOf(m.getAll()), regularFont, Paragraph.ALIGN_CENTER));
            legal += m.getLegal();
            illegal = m.getIllegal();
          }
          table.addCell(legalCell);
          table.addCell(illegalCell);
          table.addCell(allCell);
        }
      }
      numberCell = new PdfPCell(getParagraph("Подписи преподавателей", regularFont, Paragraph.ALIGN_CENTER));
      numberCell.setColspan(2);
      numberCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      numberCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      numberCell.setMinimumHeight(getPt(20));
      table.addCell(numberCell);
      for (int i = 0; i < subjectList.size(); i++) {
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(getPt(20));
        table.addCell(cell);
      }
      // Кол-во пропусков итоговое
      legalCell = new PdfPCell();
      legalCell.setMinimumHeight(getPt(20));
      legalCell.setRotation(90);
      legalCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      legalCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      illegalCell = new PdfPCell();
      illegalCell.setMinimumHeight(getPt(20));
      illegalCell.setRotation(90);
      illegalCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      illegalCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      allCell = new PdfPCell();
      allCell.setMinimumHeight(getPt(20));
      allCell.setRotation(90);
      allCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      allCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      if (!empty) {
        legalCell.addElement(getParagraph(String.valueOf(legal), regularFont, Paragraph.ALIGN_CENTER));
        illegalCell.addElement(getParagraph(String.valueOf(illegal), regularFont, Paragraph.ALIGN_CENTER));
        allCell.addElement(getParagraph(String.valueOf(legal + illegal), regularFont, Paragraph.ALIGN_CENTER));
      }
      table.addCell(legalCell);
      table.addCell(illegalCell);
      table.addCell(allCell);
      document.add(table);
      document.add(getParagraph("\n                Зав. отделением ________________________________",
        regularFont, Paragraph.ALIGN_LEFT));
      return document.getData();
    } catch (DocumentException e) {
      throw new InternalServerErrorException("DocumentException: " + e.getMessage());
    }
  }
  
    /**
   * Формирует зачетную ведомость для группы.
   *
   * @param group собственно, группа
   * @param course курс, за который формируем ведомость
   * @param semester семестр, за который формируем ведомость
   * @param empty ведомость будет без оценок (для заполнения)
   * @return массив байт, представляющий собой PDF сгенерированный документ.
   * @Throws InternalServerErrorException если вместо группы null
   */
  public byte[] semesterMarks(final StudyGroup group,
    final int course, final int semester, final boolean empty) {
    if (null == group) {
      throw new InternalServerErrorException("Группа не должна быть NULL!");
    }
    if ((0 >= course) || (0 >= semester)) {
      //TODO Переделать проверку. Либо перейти на объект GroupSemester
      throw new InternalServerErrorException("Некорректный курс или семестр!");
    }
    try {
      String title = "Зачетная ведомость";
      Report document = PDFReport.create(Report.Orientation.PORTRET, title);
      document.add(getParagraph(schoolName, regularFont, Paragraph.ALIGN_CENTER));
      document.add(getParagraph(title, bigFont, Paragraph.ALIGN_CENTER));
      String description = "группы " + group.getName() + " " + group.getCourse() + "-го курса "
        + (group.isExtramural() ? "заочной" : "очной") + " формы обучения за " + semester + "-й семестр.";
      document.add(getParagraph(description, regularFont, Paragraph.ALIGN_CENTER));
      String speciality = "Специальность: " + group.getSpeciality().getFullName();
      document.add(getParagraph(speciality, regularFont, Paragraph.ALIGN_CENTER));
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
      // Кол-во пропусков
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
      int legal = 0;
      int illegal = 0;
      for (StudyCard sc : cards.findByGroup(group)) {
        if (!sc.isRemanded() && sc.isActive()) {
          numberCell = new PdfPCell(getParagraph(String.valueOf(row++), regularFont, Paragraph.ALIGN_CENTER));
          table.addCell(numberCell);
          nameCell = new PdfPCell(getParagraph(sc.getPerson().getShortName(),
            regularFont, Paragraph.ALIGN_LEFT));
          table.addCell(nameCell);
          for (Subject sb : subjectList) {
            PdfPCell cell = new PdfPCell();
            if (!empty) {
              try {
                SemesterMark mark = smarks.get(sc, sb, course, semester);
                cell.addElement(getParagraph(String.valueOf(mark.getMark()), regularFont, Paragraph.ALIGN_CENTER));
              } catch (Exception e) {
                // Ничего не делаем, ячейка просто будет пустой
              }
            }
            table.addCell(cell);
          }
          // Кол-во пропусков
          legalCell = new PdfPCell();
          illegalCell = new PdfPCell();
          allCell = new PdfPCell();
          // TODO вывести количество пропусков в заполненной ведомости
//          if (!empty) {
//            Missing m = missings.get(sc, year, month);
//            legalCell.addElement(getParagraph(String.valueOf(m.getLegal()), regularFont, Paragraph.ALIGN_CENTER));
//            illegalCell.addElement(getParagraph(String.valueOf(m.getIllegal()), regularFont, Paragraph.ALIGN_CENTER));
//            allCell.addElement(getParagraph(String.valueOf(m.getAll()), regularFont, Paragraph.ALIGN_CENTER));
//            legal += m.getLegal();
//            illegal = m.getIllegal();
//          }
          table.addCell(legalCell);
          table.addCell(illegalCell);
          table.addCell(allCell);
        }
      }
      numberCell = new PdfPCell(getParagraph("Подписи преподавателей", regularFont, Paragraph.ALIGN_CENTER));
      numberCell.setColspan(2);
      numberCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      numberCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      numberCell.setMinimumHeight(getPt(20));
      table.addCell(numberCell);
      for (int i = 0; i < subjectList.size(); i++) {
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(getPt(20));
        table.addCell(cell);
      }
      // Кол-во пропусков итоговое
      legalCell = new PdfPCell();
      legalCell.setMinimumHeight(getPt(20));
      legalCell.setRotation(90);
      legalCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      legalCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      illegalCell = new PdfPCell();
      illegalCell.setMinimumHeight(getPt(20));
      illegalCell.setRotation(90);
      illegalCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      illegalCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      allCell = new PdfPCell();
      allCell.setMinimumHeight(getPt(20));
      allCell.setRotation(90);
      allCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      allCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      if (!empty) {
        legalCell.addElement(getParagraph(String.valueOf(legal), regularFont, Paragraph.ALIGN_CENTER));
        illegalCell.addElement(getParagraph(String.valueOf(illegal), regularFont, Paragraph.ALIGN_CENTER));
        allCell.addElement(getParagraph(String.valueOf(legal + illegal), regularFont, Paragraph.ALIGN_CENTER));
      }
      table.addCell(legalCell);
      table.addCell(illegalCell);
      table.addCell(allCell);
      document.add(table);
      document.add(getParagraph("\n                Зав. отделением ________________________________",
        regularFont, Paragraph.ALIGN_LEFT));
      return document.getData();
    } catch (DocumentException e) {
      throw new InternalServerErrorException("DocumentException: " + e.getMessage());
    }
  }

}
