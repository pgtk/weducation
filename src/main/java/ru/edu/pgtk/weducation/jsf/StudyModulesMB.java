package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.StudyModulesEJB;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.entity.ExamForm;

@ManagedBean(name = "studyModulesMB")
@ViewScoped
public class StudyModulesMB extends GenericBean<StudyModule> implements Serializable {

  @EJB
  private transient StudyModulesEJB ejb;

  private StudyPlan plan = null;
  private int planCode;

  public StudyPlan getPlan() {
    return plan;
  }

  public void setPlan(StudyPlan plan) {
    this.plan = plan;
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
        plan = ejb.getPlan(planCode);
      } else {
        addMessage("Wrond StudyPlan identifier " + planCode);
      }
    } catch (Exception e) {
      addMessage(e);
    }
  }
  
  public List<StudyModule> getStudyModules() {
    return ejb.findByPlan(plan);
  }
  
  public ExamForm[] getExamForms() {
    return ExamForm.values();
  }

  public void add() {
    item = new StudyModule();
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
