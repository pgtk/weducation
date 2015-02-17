package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.GOSExamsEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.GOSExam;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.entity.Subject;

@ManagedBean(name = "gosexamsMB")
@ViewScoped
public class GOSExamsMB extends GenericBean<GOSExam> implements Serializable {

  @EJB
  private transient GOSExamsEJB ejb;
  @EJB
  private transient StudyPlansEJB planEJB;
  @EJB
  private transient SubjectsEJB subjectEJB;
  private StudyPlan plan = null;
  private int planCode;

  public List<GOSExam> getGosexams() {
    if (null != plan) {
      return ejb.findByPlan(plan);
    }
    return new ArrayList<>();
  }

  public List<Subject> getSubjects() {
    if (null != plan) {
      return subjectEJB.findByPlan(plan);
    }
    return new ArrayList<>();
  }

  public StudyPlan getPlan() {
    return plan;
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

  public void add() {
    item = new GOSExam();
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
      if (delete && (item != null)) {
        ejb.delete(item);
      }
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }
}
