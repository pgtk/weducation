package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.SubjectLoadEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.ExamForm;
import ru.edu.pgtk.weducation.entity.Subject;
import ru.edu.pgtk.weducation.entity.SubjectLoad;

@ManagedBean(name = "subjectLoadMB")
@ViewScoped
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
    return ejb.fetchAll(subject);
  }

  public ExamForm[] getExamForms() {
    return ExamForm.values();
  }

  @Override
  public void newItem() {
    item = new SubjectLoad();
    item.setSubject(subject);
  }

  @Override
  public void deleteItem() {
    if ((null != item) && delete) {
      ejb.delete(item);
    }
  }

  @Override
  public void saveItem() {
    ejb.save(item);
  }
}
