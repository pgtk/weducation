package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import ru.edu.pgtk.weducation.ejb.DepartmentsEJB;
import ru.edu.pgtk.weducation.ejb.SpecialitiesEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@ManagedBean(name = "studyPlansMB")
@ViewScoped
public class StudyPlansMB extends GenericBean<StudyPlan> implements Serializable {

  long serialVersionUID = 0L;

  @Inject
  private transient StudyPlansEJB ejb;
  @Inject
  private transient DepartmentsEJB departments;
  @Inject
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

  @Override
  public void newItem() {
    item = new StudyPlan();
  }

  @Override
  public void deleteItem() {
    if (delete && (null != item)) {
      ejb.delete(item);
      prepareList();
    }
  }

  @Override
  public void saveItem() {
    ejb.save(item);
    prepareList();
  }
}
