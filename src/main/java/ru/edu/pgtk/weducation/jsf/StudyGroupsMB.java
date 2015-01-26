package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import ru.edu.pgtk.weducation.ejb.DepartmentsEJB;
import ru.edu.pgtk.weducation.ejb.SpecialitiesEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.StudyPlan;

public class StudyGroupsMB extends GenericBean<StudyGroup> implements Serializable {

  @EJB
  private StudyGroupsEJB ejb;
  @EJB
  private DepartmentsEJB depejb;
  @EJB
  private StudyPlansEJB plansEJB;
  @EJB
  private SpecialitiesEJB spcejb;

  private Department department;
  private Speciality speciality;
  private int departmentCode;
  private int profileCode;

  public int getDepartmentCode() {
    return departmentCode;
  }

  public void setDepartmentCode(int departmentCode) {
    this.departmentCode = departmentCode;
  }

  public Department getDepartment() {
    return department;
  }

  public int getProfileCode() {
    return profileCode;
  }

  public void setProfileCode(int profileCode) {
    this.profileCode = profileCode;
  }

  public void loadDepartment() {
    if (departmentCode > 0) {
      department = depejb.get(departmentCode);
    } else {
      department = null;
    }
  }

  public void changeDepartment(ValueChangeEvent event) {
    try {
      int code = (Integer) event.getNewValue();
      if (code > 0) {
        department = depejb.get(code);
      } else {
        department = null;
      }
    } catch (Exception e) {
      department = null;
      addMessage(e);
    }
  }

  public void changeSpeciality(ValueChangeEvent event) {
    try {
      int code = (Integer) event.getNewValue();
      if (code > 0) {
        speciality = spcejb.get(code);
      } else {
        speciality = null;
      }
    } catch (Exception e) {
      speciality = null;
      addMessage(e);
    }
  }

  public void add() {
    item = new StudyGroup();
    edit = true;
  }

  public List<StudyGroup> getStudyGroups() {
    if (null != department) {
      return ejb.findByDepartment(department);
    } else {
      return ejb.fetchAll();
    }
  }

  public List<Speciality> getSpecialities() {
    if (null != department) {
      return spcejb.findByDepartment(department);
    } else {
      return spcejb.fetchAll();
    }
  }

  public List<StudyPlan> getStudyPlans() {
    if (null != speciality) {
      return plansEJB.findBySpeciality(speciality, item.isExtramural());
    } else {
      return plansEJB.fetchAll();
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
}
