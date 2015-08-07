package ru.edu.pgtk.weducation.jsf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ru.edu.pgtk.weducation.ejb.GroupSemestersEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.ejb.MissingsEJB;
import ru.edu.pgtk.weducation.entity.GroupSemester;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.Missing;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@ViewScoped
@Named("weekMissingsMB")
public class WeekMissingsMB {

  long serialVersionUID = 0L;

  @Inject
  private transient StudyGroupsEJB groups;
  @Inject
  private transient GroupSemestersEJB semesters;
  @Inject
  private transient MissingsEJB ejb;
  private int groupCode;
  private StudyGroup group;
  private int semesterCode;
  private GroupSemester semester;
  private List<GroupSemester> semesterList;
  private List<Missing> missingList;
  private int missingDate;

  /**
   * Функция для построения списка пропусков
   */
  private void makeList() {
    if ((group != null) && (missingDate > 0)) {
      int year = missingDate / 1000;
      int tail = missingDate % 1000;
      int month = tail / 10;
      int week = tail % 10;
      missingList = ejb.fetchAll(group, year, month, week);
    } else {
      // Если хоть один из параметров отсутствует - очищаем список
      missingList = null;
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
      if (missingList != null) {
        for (Missing m : missingList) {
          ejb.save(m);
        }
      }
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public Map<String, Integer> getDates() {
    Map<String, Integer> result = new TreeMap<>();
    if (semester != null) {
      int year = semester.getBeginYear();
      int month = semester.getBeginMonth();
      int week = semester.getBeginWeek();
      int date;
      do {
        date = year * 1000 + month * 10 + week;
        result.put(String.format("%4d-%02d, %d-я неделя", year, month, week), date);
        week += 1;
        if (week > 4) {
          week = 1;
          month += 1;
          if (month > 12) {
            month = 1;
            year += 1;
          }
        }
      } while (date <= semester.getEndDate());
    }
    return result;
  }

  public void changeSemester(ValueChangeEvent event) {
    try {
      int code = (Integer) event.getNewValue();
      if (code > 0) {
        semester = semesters.get(code);
        makeList();
      } else {
        semester = null;
      }
    } catch (Exception e) {
      semester = null;
      addMessage(e);
    }
  }

  public void changeDate(ValueChangeEvent event) {
    try {
      int newDate = (Integer) event.getNewValue();
      if (newDate > 0) {
        missingDate = newDate;
        makeList();
      } else {
        missingDate = 0;
      }
    } catch (Exception e) {
      missingDate = 0;
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

  public int getMissingDate() {
    return missingDate;
  }

  public void setMissingDate(int missingDate) {
    this.missingDate = missingDate;
  }

  public List<Missing> getMissingList() {
    return missingList;
  }
}
