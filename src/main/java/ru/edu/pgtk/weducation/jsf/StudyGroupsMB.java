package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import ru.edu.pgtk.weducation.ejb.DepartmentsEJB;
import ru.edu.pgtk.weducation.ejb.SpecialitiesEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.StudyPlan;

@ManagedBean(name = "studyGroupsMB")
@ViewScoped
public class StudyGroupsMB extends GenericBean<StudyGroup> implements Serializable {

  @EJB
  private transient StudyGroupsEJB ejb;
  @EJB
  private transient DepartmentsEJB depejb;
  @EJB
  private transient StudyPlansEJB plansEJB;
  @EJB
  private transient SpecialitiesEJB spcejb;

  private Department department;
  private Speciality speciality;
  private int departmentCode;

  public int getDepartmentCode() {
    return departmentCode;
  }

  public void setDepartmentCode(int departmentCode) {
    this.departmentCode = departmentCode;
  }

  public Department getDepartment() {
    return department;
  }

  public void loadDepartment() {
    // Если в кукисах есть код отделения, то мы его оттуда возьмем!
    if (departmentCode == 0) {
      departmentCode = (int) Utils.getLongFromCookie("departmentId");
    }
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
