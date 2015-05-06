package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.GroupSemestersEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.GroupSemester;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@ViewScoped
@ManagedBean(name = "marksMB")
public class MarksMB implements Serializable {

  @EJB
  private transient StudyGroupsEJB groups;
  @EJB
  private transient SubjectsEJB subjects;
  @EJB
  private transient GroupSemestersEJB semesters;
  @EJB
  private transient StudyCardsEJB cards;
  private int groupCode;
  private StudyGroup group;
  private int semesterCode;
  private GroupSemester semester;
  private List<GroupSemester> semesterList;

  public void loadGroup() {
    try {
      if (groupCode > 0) {
        group = groups.get(groupCode);
        // Можно загрузить список групп
      }
    } catch (Exception e) {
      addMessage(e);
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
}
