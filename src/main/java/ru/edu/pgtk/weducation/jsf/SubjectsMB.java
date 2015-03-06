package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.StudyModulesEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.entity.Subject;

@ManagedBean(name = "subjectsMB")
@ViewScoped
public class SubjectsMB extends GenericBean<Subject> implements Serializable {

  @EJB
  private transient SubjectsEJB ejb;
  @EJB
  private transient StudyModulesEJB mejb;
  @EJB
  private transient StudyPlansEJB plans;

  private StudyPlan plan = null;
  private int planCode;

  public int getPlanCode() {
    return planCode;
  }

  public void setPlanCode(int planCode) {
    this.planCode = planCode;
  }

  public StudyPlan getPlan() {
    return plan;
  }

  public void setPlan(StudyPlan plan) {
    this.plan = plan;
  }

  public void loadPlan() {
    try {
      if (planCode > 0) {
        plan = plans.get(planCode);
      }
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public List<Subject> getSubjects() {
    return ejb.fetchAll(plan);
  }

  public List<StudyModule> getStudyModules() {
    return mejb.fetchAll(plan);
  }

  public void add() {
    item = new Subject();
    item.setPlan(plan);
    edit = true;
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
