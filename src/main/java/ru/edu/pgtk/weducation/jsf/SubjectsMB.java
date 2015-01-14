package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import ru.edu.pgtk.weducation.ejb.StudyModulesEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.entity.Subject;

public class SubjectsMB extends GenericBean<Subject> implements Serializable {

  @EJB
  private SubjectsEJB ejb;
  @EJB
  private StudyModulesEJB mejb;

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
        plan = ejb.getPlan(planCode);
      }
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public List<Subject> getSubjects() {
    return ejb.findByPlan(plan);
  }

  public List<StudyModule> getStudyModules() {
    return mejb.findByPlan(plan);
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
