package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ru.edu.pgtk.weducation.ejb.GroupSemestersEJB;
import ru.edu.pgtk.weducation.ejb.PracticMarksEJB;
import ru.edu.pgtk.weducation.ejb.PracticsEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.entity.GroupSemester;
import ru.edu.pgtk.weducation.entity.Practic;
import ru.edu.pgtk.weducation.entity.PracticMark;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

/**
 * Компонент для хранения данных страницы выставления оценок по практике.
 * @author Воронин Леонид
 */
@Named("practicMarksMB")
@ViewScoped
public class PracticMarksMB implements Serializable {
  
  long serialVersionUID = 0L;

  @Inject
  private transient StudyGroupsEJB groups;
  @Inject
  private transient PracticsEJB practics;
  @Inject
  private transient GroupSemestersEJB semesters;
  @Inject
  private transient PracticMarksEJB marks;
  private int groupCode;
  private StudyGroup group;
  private int practicCode;
  private Practic practic;
  private int semesterCode;
  private GroupSemester semester;
  private List<GroupSemester> semesterList;
  private List<PracticMark> markList;
  private List<Practic> practicList;
  
  /**
   * Функция для построения списка оценок
   */
  private void makeList() {
    if ((group != null) && (practic != null) && (semester != null)) {
      markList = marks.fetchAll(group, practic);
    } else {
      // Если хоть один из параметров отсутствует - очищаем список
      markList = null;
    }
  }

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
        for (PracticMark m : markList) {
          marks.save(m);
        }
      }
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void changeSemester(ValueChangeEvent event) {
    try {
      int code = (Integer) event.getNewValue();
      if (code > 0) {
        semester = semesters.get(code);
        // Корректируем список дисциплин для этого семестра
        practicList = practics.fetch(group, semester.getCourse(), semester.getSemester());
        makeList();
      } else {
        semester = null;
      }
    } catch (Exception e) {
      semester = null;
      addMessage(e);
    }
  }

  public void changePractic(ValueChangeEvent event) {
    try {
      int code = (Integer) event.getNewValue();
      if (code > 0) {
        practic = practics.get(code);
        makeList();
      } else {
        practic = null;
      }
    } catch (Exception e) {
      practic = null;
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

  public List<Practic> getPracticList() {
    if (practicList == null) {
      practicList = new ArrayList<>();
    }
    return practicList;
  }

  public List<PracticMark> getMarkList() {
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

  public int getPracticCode() {
    return practicCode;
  }

  public void setPracticCode(int practicCode) {
    this.practicCode = practicCode;
  }

  public Practic getPractic() {
    return practic;
  }
}
