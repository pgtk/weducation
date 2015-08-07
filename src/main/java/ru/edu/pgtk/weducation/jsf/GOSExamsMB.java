package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ru.edu.pgtk.weducation.ejb.GOSExamsEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.GOSExam;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.entity.Subject;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@Named("gosexamsMB")
@ViewScoped
public class GOSExamsMB extends GenericBean<GOSExam> implements Serializable {

  long serialVersionUID = 0L;

  @Inject
  private transient GOSExamsEJB ejb;
  @Inject
  private transient StudyPlansEJB planEJB;
  @Inject
  private transient SubjectsEJB subjectEJB;
  private StudyPlan plan = null;
  private int planCode;

  public List<GOSExam> getGosexams() {
    if (null != plan) {
      return ejb.fetchAll(plan);
    }
    return new ArrayList<>();
  }

  public List<Subject> getSubjects() {
    if (null != plan) {
      return subjectEJB.fetchAll(plan);
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

  @Override
  public void newItem() {
    item = new GOSExam();
    item.setPlan(plan);
  }

  @Override
  public void deleteItem() {
    if (delete && (item != null)) {
      ejb.delete(item);
    }
  }

  @Override
  public void saveItem() {
    ejb.save(item);
  }
}
