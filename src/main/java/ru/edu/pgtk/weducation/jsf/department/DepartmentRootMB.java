package ru.edu.pgtk.weducation.jsf.department;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import ru.edu.pgtk.weducation.ejb.DepartmentsEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.entity.Account;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@ManagedBean(name = "departmentRootMB")
@ViewScoped
public class DepartmentRootMB {

  @ManagedProperty(value = "#{sessionMB.user}")
  private transient Account account;
  @EJB
  private transient DepartmentsEJB departments;
  @EJB
  private transient StudyGroupsEJB groups;
  @EJB
  private transient StudyCardsEJB cards;
  private transient Department department;
  private boolean edit = false;
  private StudyGroup group;
  private int groupCode;
  private List<StudyGroup> groupList;

  @PostConstruct
  private void initBean() {
    groupList = new ArrayList<>();
    if (null != account) {
      int depCode = account.getCode();
      if (depCode > 0) {
        try {
          department = departments.get(depCode);
          groupList = groups.findByDepartment(department);
        } catch (Exception e) {
          addMessage(e);
        }
      }
    }
  }
  
  public void changeGroup(ValueChangeEvent event) {
    try {
      int code = (Integer) event.getNewValue();
      if (code > 0) {
        group = groups.get(code);
      } else {
        group = null;
      }
    } catch (Exception e) {
      group = null;
      addMessage(e);
    }
  }

  public void toggleEdit() {
    try {
      if ((edit) && (null != department)) {
        departments.save(department);
      }
      edit = !edit;
    } catch (Exception e) {
      addMessage(e);
    }
  }
  
  public List<StudyCard> getStudents() {
    if (null != group) {
      return cards.findByGroup(group);
    }
    return new ArrayList<>();
  }
  
  public String getStudentClass(final boolean enabled) {
    return (enabled)? "enabled" : "disabled";
  } 

  public String getEditLabel() {
    return edit ? "Сохранить" : "Редактировать";
  }

  public boolean isValid() {
    return department != null;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public boolean isEdit() {
    return edit;
  }

  public Department getDepartment() {
    return department;
  }

  public StudyGroup getGroup() {
    return group;
  }

  public List<StudyGroup> getGroupList() {
    return groupList;
  }

  public int getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(int groupCode) {
    this.groupCode = groupCode;
  }
}
