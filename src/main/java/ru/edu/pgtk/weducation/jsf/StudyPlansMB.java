package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.DepartmentsEJB;
import ru.edu.pgtk.weducation.ejb.SpecialitiesEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyPlan;

@ManagedBean(name = "studyPlansMB")
@ViewScoped
public class StudyPlansMB extends GenericBean<StudyPlan> implements Serializable {

  @EJB
  private transient StudyPlansEJB ejb;
  @EJB
  private transient DepartmentsEJB departments;
  @EJB
  private transient SpecialitiesEJB specialities;
  private int planCode;
  private List<StudyPlan> plansList;
  private List<Speciality> specialitiesList;
  private Department dep;

  @PostConstruct
  private void loadDepartment() {
    try {
      if ((user != null) && user.isDepartment()) {
        dep = departments.get(user.getCode());
      } else {
        dep = null;
      }
    } catch (Exception e) {
      dep = null;
    }
  }

  private void prepareList() {
    try {
      if (dep != null) {
        plansList = ejb.findByDepartment(dep);
        specialitiesList = specialities.findByDepartment(dep);
      } else {
        plansList = ejb.fetchAll();
        specialitiesList = specialities.fetchAll();
      }
    } catch (Exception e) {
      // Получить отделение не удалось, выводим все планы
      plansList = ejb.fetchAll();
      specialitiesList = specialities.fetchAll();
    }
  }

  public int getPlanCode() {
    return planCode;
  }

  public void setPlanCode(int planCode) {
    this.planCode = planCode;
  }

  public void loadPlan() {
    try {
      if (planCode > 0) {
        item = ejb.get(planCode);
        details = true;
      }
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public List<StudyPlan> getPlans() {
    if (plansList == null) {
      prepareList();
    }
    return plansList;
  }
  
  public List<Speciality> getSpecialities() {
    if (specialitiesList == null) {
      prepareList();
    }
    return specialitiesList;
  }

  public void add() {
    item = new StudyPlan();
    edit = true;
  }

  public void save() {
    try {
      ejb.save(item);
      resetState();
      prepareList();
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void confirmDelete() {
    try {
      if (delete && (null != item)) {
        ejb.delete(item);
      }
      resetState();
      prepareList();
    } catch (Exception e) {
      addMessage(e);
    }
  }
}
