package ru.edu.pgtk.weducation.jsf;

import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.GroupSemestersEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.entity.GroupSemester;
import ru.edu.pgtk.weducation.entity.StudyGroup;

@ManagedBean(name = "groupSemestersMB")
@ViewScoped
public class GroupSemestersMB extends GenericBean<GroupSemester> {

  @EJB
  private transient GroupSemestersEJB ejb;
  @EJB
  private transient StudyGroupsEJB groups;
  private int groupCode;
  private StudyGroup group;

  public void loadGroup() {
    try {
      if (groupCode > 0) {
        group = groups.get(groupCode);
      } else {
        group = null;
      }
    } catch (Exception e) {
      addMessage(e);
    }
  }
  
  public List<GroupSemester> getSemesters() {
    return ejb.fetchAll(group);
  }

  public void add() {
    if (group != null) {
      item = new GroupSemester();
      item.setGroup(group);
      edit = true;
    } else {
      addMessage("You can't add semesters for unknown group!");
    }
  }

  public void save() {
    try {
      ejb.save(item);
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void confirmDelete() {
    try {
      if ((null != item) && delete) {
        ejb.delete(item);
      }
      resetState();
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
}
