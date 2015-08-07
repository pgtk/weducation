package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ru.edu.pgtk.weducation.ejb.GroupSemestersEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.entity.GroupSemester;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@Named("groupSemestersMB")
@ViewScoped
public class GroupSemestersMB extends GenericBean<GroupSemester> implements Serializable {

  long serialVersionUID = 0L;
  
  @Inject
  private transient GroupSemestersEJB ejb;
  @Inject
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

  public int getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(int groupCode) {
    this.groupCode = groupCode;
  }

  public StudyGroup getGroup() {
    return group;
  }

  @Override
  public void newItem() {
    if (group != null) {
      item = new GroupSemester();
      item.setGroup(group);
    } else {
      addMessage("You can't add semesters for unknown group!");
    }
  }

  @Override
  public void deleteItem() {
    if ((null != item) && delete) {
      ejb.delete(item);
    }
  }

  @Override
  public void saveItem() {
    ejb.save(item);
  }
}
