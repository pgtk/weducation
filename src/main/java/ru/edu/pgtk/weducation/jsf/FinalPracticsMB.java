package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.FinalPracticsEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.entity.FinalPractic;
import ru.edu.pgtk.weducation.entity.StudyPlan;

@ManagedBean(name = "finalPracticsMB")
@ViewScoped
public class FinalPracticsMB extends GenericBean<FinalPractic> implements Serializable {

  @EJB
  private FinalPracticsEJB ejb;
  @EJB
  private StudyPlansEJB planEJB;
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
        plan = planEJB.get(planCode);
      } else {
        addMessage("Wrond StudyPlan identifier " + planCode);
      }
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public List<FinalPractic> getFinalPractics() {
    return ejb.findByPlan(plan);
  }

  public void add() {
    item = new FinalPractic();
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
