package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import ru.edu.pgtk.weducation.ejb.DepartmentProfilesEJB;
import ru.edu.pgtk.weducation.ejb.DepartmentsEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.entity.DepartmentProfile;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.StudyPlan;

public class StudyGroupsMB extends GenericBean<StudyGroup> implements Serializable {

  @EJB
  private StudyGroupsEJB ejb;
  @EJB
  private DepartmentsEJB dejb;
  @EJB
  private DepartmentProfilesEJB pejb;
  @EJB
  private StudyPlansEJB plansEJB;

  private Department department;
  private DepartmentProfile profile;
  private int departmentCode;
  private int profileCode;

  public int getDepartmentCode() {
    return departmentCode;
  }

  public void setDepartmentCode(int departmentCode) {
    this.departmentCode = departmentCode;
  }

  public int getProfileCode() {
    return profileCode;
  }

  public void setProfileCode(int profileCode) {
    this.profileCode = profileCode;
  }

  public void loadDepartment() {
    if (departmentCode > 0) {
      department = dejb.get(departmentCode);
    } else {
      department = null;
    }
  }
  
  public void loadProfile(ValueChangeEvent event) {
    int code = (Integer)event.getNewValue();
    if (code > 0) {
      profile = pejb.get(code);
    } else {
      profile = null;
    }
  }

  public void add() {
    item = new StudyGroup();
    if (null != department) {
      item.setDepartment(department);
    }
    edit = true;
  }

  public List<StudyGroup> getStudyGroups() {
    if (null != department) {
      return ejb.findByDepartment(department);
    } else {
      return ejb.fetchAll();
    }
  }
  
  public List<StudyPlan> getStudyPlans() {
    if (null != profile) {
      return plansEJB.findBySpeciality(profile.getSpeciality(), profile.isExtramural());
    } else {
      return plansEJB.fetchAll();
    }
  }
  
  public List<DepartmentProfile> getDepartmentProfiles() {
    if (null != department) {
      return pejb.findByDepartment(department);
    } else {
      return pejb.fetchAll();
    }
  }

  public void save() {
    try {
      if (profileCode > 0) {
        DepartmentProfile dp = pejb.get(profileCode);
        item.setDepartment(dp.getDepartment());
        item.setSpeciality(dp.getSpeciality());
        item.setExtramural(dp.isExtramural());
      }
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
}
