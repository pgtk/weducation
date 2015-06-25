package ru.edu.pgtk.weducation.jsf;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import ru.edu.pgtk.weducation.ejb.GroupSemestersEJB;
import ru.edu.pgtk.weducation.ejb.SemesterMarksEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.ejb.StudyModulesEJB;
import ru.edu.pgtk.weducation.entity.GroupSemester;
import ru.edu.pgtk.weducation.entity.SemesterMark;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.StudyModule;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@ViewScoped
@ManagedBean(name = "semesterModuleMarksMB")
public class SemesterModuleMarksMB {

  long serialVersionUID = 0L;

  @Inject
  private transient StudyGroupsEJB groups;
  @Inject
  private transient StudyModulesEJB modules;
  @Inject
  private transient GroupSemestersEJB semesters;
  @Inject
  private transient SemesterMarksEJB marks;
  private int groupCode;
  private StudyGroup group;
  private int moduleCode;
  private StudyModule module;
  private int semesterCode;
  private GroupSemester semester;
  private List<GroupSemester> semesterList;
  private List<StudyModule> moduleList;
  private List<SemesterMark> markList;

  /**
   * Функция для построения списка оценок
   */
  private void makeList() {
    if ((group != null) && (module != null) && (semester != null)) {
      markList = marks.fetchAll(group, module, semester.getCourse(), semester.getSemester());
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
        for (SemesterMark m : markList) {
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
        moduleList = modules.fetch(group, semester.getCourse(), semester.getSemester());
        makeList();
      } else {
        semester = null;
      }
    } catch (Exception e) {
      semester = null;
      addMessage(e);
    }
  }

  public void changeModule(ValueChangeEvent event) {
    try {
      int code = (Integer) event.getNewValue();
      if (code > 0) {
        module = modules.get(code);
        makeList();
      } else {
        module = null;
      }
    } catch (Exception e) {
      module = null;
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

  public List<StudyModule> getModuleList() {
    if (moduleList == null) {
      moduleList = new ArrayList<>();
    }
    return moduleList;
  }

  public List<SemesterMark> getMarkList() {
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

  public int getModuleCode() {
    return moduleCode;
  }

  public void setModuleCode(int moduleCode) {
    this.moduleCode = moduleCode;
  }

  public StudyModule getModule() {
    return module;
  }
}
