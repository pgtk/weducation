package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import ru.edu.pgtk.weducation.ejb.SubjectLoadEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.Subject;
import ru.edu.pgtk.weducation.entity.SubjectLoad;

public class SubjectLoadMB extends GenericBean<SubjectLoad> implements Serializable {

  @EJB
  private SubjectLoadEJB ejb;
  @EJB
  private SubjectsEJB sejb;

  private Subject subject = null;
  private int subjectCode;

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public int getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(int subjectCode) {
    this.subjectCode = subjectCode;
  }

  public void loadSubject() {
    subject = sejb.get(subjectCode);
  }

  public List<SubjectLoad> getSubjectLoad() {
    return ejb.findBySubject(subject);
  }

  public void add() {
    item = new SubjectLoad();
    item.setSubject(subject);
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
