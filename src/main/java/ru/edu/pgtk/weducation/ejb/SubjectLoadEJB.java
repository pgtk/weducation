package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.entity.Subject;
import ru.edu.pgtk.weducation.entity.SubjectLoad;

@Stateless
@Named("subjectLoadEJB")
public class SubjectLoadEJB {
  
  @PersistenceContext
  private EntityManager em;
  
  public SubjectLoad get(final int id) {
    SubjectLoad result = em.find(SubjectLoad.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("SubjectLoad not found with id " + id);
  }
  
  public List<SubjectLoad> fetchAll() {
    TypedQuery<SubjectLoad> q = em.createQuery(
            "SELECT sl FROM SubjectLoad sl ORDER BY sl.course, sl.semester", SubjectLoad.class);
    return q.getResultList();
  }
  
  public List<SubjectLoad> findBySubject(final Subject subject) {
    TypedQuery<SubjectLoad> q = em.createQuery(
            "SELECT sl FROM SubjectLoad sl WHERE (sl.subject = :subj) ORDER BY sl.course, sl.semester", SubjectLoad.class);
    q.setParameter("subj", subject);
    return q.getResultList();
  }
  
  public List<SubjectLoad> finaBySemester(final StudyPlan plan, final int semester) {
    TypedQuery<SubjectLoad> q = em.createQuery(
            "SELECT sl FROM SubjectLoad sl WHERE (sl.subject IN (SELECT s FROM Subject s WHERE (s.plan = :pln))) "
            + "AND (sl.semester = :sm) ORDER BY sl.course, sl.semester", SubjectLoad.class);
    q.setParameter("pln", plan);
    q.setParameter("sm", semester);
    return q.getResultList();
  }
  
  public SubjectLoad save(SubjectLoad item) {
    if (item.getSubjectCode() > 0) {
      Subject s = em.find(Subject.class, item.getSubjectCode());
      if (null != s) {
        item.setSubject(s);
      } else {
        throw new EJBException("Subject not found with id " + item.getSubjectCode());
      }
    } else {
      throw new EJBException("Wrong Subject code " + item.getSubjectCode());
    }
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }
  
  public void delete(final SubjectLoad item) {
    SubjectLoad sl = em.find(SubjectLoad.class, item.getId());
    if (null != sl) {
      em.remove(sl);
    }
  }
}
