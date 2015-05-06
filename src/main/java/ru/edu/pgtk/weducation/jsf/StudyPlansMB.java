package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.entity.Account;
import ru.edu.pgtk.weducation.entity.StudyPlan;

@ManagedBean(name = "studyPlansMB")
@ViewScoped
public class StudyPlansMB extends GenericBean<StudyPlan> implements Serializable {

  @EJB
  private transient StudyPlansEJB ejb;
  @ManagedProperty(value = "#{sessionMB.user}")
  private transient Account user;
  private int planCode;

  @PostConstruct
  private void checkAccount() {
    resetState();
    // Если пользователь неавторизован, то выдаем ошибку и запрещаем работу!
    if (null == user) {
      error = true;
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

  public void add() {
    item = new StudyPlan();
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
      if (delete && (null != item)) {
        ejb.delete(item);
      }
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public Account getUser() {
    return user;
  }

  public void setUser(Account user) {
    this.user = user;
  }
}
