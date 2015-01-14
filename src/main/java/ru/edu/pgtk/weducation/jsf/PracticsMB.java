package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import ru.edu.pgtk.weducation.ejb.PracticsEJB;
import ru.edu.pgtk.weducation.ejb.StudyModulesEJB;
import ru.edu.pgtk.weducation.entity.Practic;
import ru.edu.pgtk.weducation.entity.StudyPlan;

public class PracticsMB extends GenericBean<Practic> implements Serializable {

  @EJB
  private PracticsEJB ejb;
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
  
}
