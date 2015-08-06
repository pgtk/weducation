package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ru.edu.pgtk.weducation.ejb.GroupSemestersEJB;
import ru.edu.pgtk.weducation.ejb.MonthMarksEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.GroupSemester;
import ru.edu.pgtk.weducation.entity.MonthMark;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.Subject;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

//@ViewScoped
//@ManagedBean(name = "monthMarksMB")
@Named("monthMarksMB")
@ViewScoped
public class MonthMarksMB implements Serializable {

  long serialVersionUID = 0L;

  @Inject
  private transient StudyGroupsEJB groups;
  @Inject
  private transient SubjectsEJB subjects;
  @Inject
  private transient GroupSemestersEJB semesters;
  @Inject
  private transient MonthMarksEJB marks;
  private int groupCode;
  private StudyGroup group;
  private int subjectCode;
  private Subject subject;
  private int semesterCode;
  private GroupSemester semester;
  private List<GroupSemester> semesterList;
  private List<Subject> subjectList;
  private List<MonthMark> markList;
  private int markDate;
  private String emptySheetLink;
  private String filledSheetLink;

  /**
   * Функция для построения списка оценок
   */
  private void makeList() {
    if ((group != null) && (markDate > 0)) {
      if (subject != null) {
        markList = marks.fetchAll(group, subject, markDate / 100, markDate % 100);
      }
      emptySheetLink = "reports/group/" + group.getId() + "/monthmarks/empty/"
        + markDate / 100 + "/" + markDate % 100;
      filledSheetLink = "reports/group/" + group.getId() + "/monthmarks/filled/"
        + markDate / 100 + "/" + markDate % 100;
    } else {
      // Если хоть один из параметров отсутствует - очищаем список
      markList = null;
    }
  }

//  private void getSheet(final boolean empty) {
//    // Get the FacesContext
//    FacesContext facesContext = FacesContext.getCurrentInstance();
//    // Get HTTP response
//    ExternalContext ec = facesContext.getExternalContext();
//    // Set response headers
//    ec.responseReset();   // Reset the response in the first place
//    ec.setResponseContentType("application/pdf");  // Set only the content type
//    try (OutputStream responseOutputStream = ec.getResponseOutputStream()) {
//      responseOutputStream.write(monthSheets.getMonthMarksSheet(group, markDate / 100, markDate % 100, empty));
//      responseOutputStream.flush();
//      responseOutputStream.close();
//    } catch (IOException e) {
//      addMessage(e);
//    }
//    facesContext.responseComplete();
//  }
  public boolean isAviableSheet() {
    return (group != null) && (markDate > 0);
  }

//  public void emptyMonthSheet() {
//    getSheet(true);
//  }
//
//  public void filledMonthSheet() {
//    getSheet(false);
//  }
  public void loadGroup() {
    try {
      if (groupCode > 0) {
        group = groups.get(groupCode);
        // Можно загрузить список групп
        semesterList = semesters.fetchAll(group);
      }
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void save() {
    try {
      if (markList != null) {
        for (MonthMark m : markList) {
          marks.save(m);
        }
      }
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public Map<String, Integer> getMonths() {
    Map<String, Integer> result = new TreeMap<>();
    if (semester != null) {
      int year = semester.getBeginYear();
      int month = semester.getBeginMonth();
      int edate = semester.getEndYear() * 100 + semester.getEndMonth();
      int date;
      do {
        date = year * 100 + month;
        result.put(String.format("%04d-%02d", year, month), date);
        month += 1;
        if (month == 13) {
          month = 1;
          year += 1;
        }
      } while (date <= edate);
    }
    return result;
  }

  public void changeSemester(ValueChangeEvent event) {
    try {
      int code = (Integer) event.getNewValue();
      if (code > 0) {
        semester = semesters.get(code);
        // Корректируем список дисциплин для этого семестра
        subjectList = subjects.fetch(group, semester.getCourse(), semester.getSemester());
        makeList();
      } else {
        semester = null;
      }
    } catch (Exception e) {
      semester = null;
      addMessage(e);
    }
  }

  public void changeSubject(ValueChangeEvent event) {
    try {
      int code = (Integer) event.getNewValue();
      if (code > 0) {
        subject = subjects.get(code);
        makeList();
      } else {
        subject = null;
      }
    } catch (Exception e) {
      subject = null;
      addMessage(e);
    }
  }

  public void changeDate(ValueChangeEvent event) {
    try {
      int newDate = (Integer) event.getNewValue();
      if (newDate > 0) {
        markDate = newDate;
        makeList();
      } else {
        markDate = 0;
      }
    } catch (Exception e) {
      markDate = 0;
      addMessage(e);
    }
  }

  public List<GroupSemester> getSemesterList() {
    if (semesterList != null) {
      return semesterList;
    } else {
      return new ArrayList<>();
    }
  }

  public List<Subject> getSubjectList() {
    if (subjectList == null) {
      subjectList = new ArrayList<>();
    }
    return subjectList;
  }

  public List<MonthMark> getMarkList() {
    if (markList == null) {
      markList = new ArrayList<>();
    }
    return markList;
  }

  public int getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(int groupCode) {
    this.groupCode = groupCode;
  }

  public StudyGroup getGroup() {
    return group;
  }

  public int getSemesterCode() {
    return semesterCode;
  }

  public void setSemesterCode(int semesterCode) {
    this.semesterCode = semesterCode;
  }

  public GroupSemester getSemester() {
    return semester;
  }

  public int getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(int subjectCode) {
    this.subjectCode = subjectCode;
  }

  public Subject getSubject() {
    return subject;
  }

  public int getMarkDate() {
    return markDate;
  }

  public void setMarkDate(int markDate) {
    this.markDate = markDate;
  }

  public String getEmptySheetLink() {
    return emptySheetLink;
  }

  public String getFilledSheetLink() {
    return filledSheetLink;
  }
}
