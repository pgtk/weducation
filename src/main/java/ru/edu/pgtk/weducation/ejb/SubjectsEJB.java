package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.entity.Subject;

@Stateless
@Named("subjectsEJB")
public class SubjectsEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  public Subject get(final int id) {
    Subject result = em.find(Subject.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("Subject not found with id " + id);
  }

  public int getMaxLoad(final Subject subject) {
    TypedQuery<Long> q = em.createQuery(
            "SELECT SUM(sl.maximumLoad) FROM SubjectLoad sl WHERE (sl.subject = :s)", Long.class);
    q.setParameter("s", subject);
    return q.getSingleResult().intValue();
  }

  public int getAudLoad(final Subject subject) {
    TypedQuery<Long> q = em.createQuery(
            "SELECT SUM(sl.auditoryLoad) FROM SubjectLoad sl WHERE (sl.subject = :s)", Long.class);
    q.setParameter("s", subject);
    return q.getSingleResult().intValue();
  }

  public List<Subject> fetchAll(final StudyPlan plan) {
    TypedQuery<Subject> q = em.createQuery(
            "SELECT s FROM Subject s WHERE (s.plan = :pln) ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", plan);
    return q.getResultList();
  }

  public List<Subject> fetchForCard(final StudyCard card) {
    TypedQuery<Subject> q = em.createQuery(
            "SELECT s FROM Subject s WHERE (s.plan = :pln) AND "
            + "(s.id NOT IN (SELECT fm.subject.id FROM FinalMark fm WHERE (fm.card = :c))) ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", card.getPlan());
    q.setParameter("c", card);
    return q.getResultList();
  }

  public List<Subject> fetchCourseWorksForCard(final StudyCard card) {
    TypedQuery<Subject> q = em.createQuery(
            "SELECT s FROM Subject s WHERE (s.plan = :pln) AND "
            + "((SELECT COUNT(sl) FROM SubjectLoad sl WHERE (sl.subject = s) AND (sl.courseProjectLoad > 0)) > 0 )"
            + " ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", card.getPlan());
    return q.getResultList();
  }
  
  public List<Subject> fetch(final StudyGroup group, final int course, final int semester) {
    TypedQuery<Subject> q = em.createQuery(
            "SELECT s FROM Subject s WHERE (s.plan = :pln) AND "
            + "((SELECT COUNT(sl) FROM SubjectLoad sl WHERE (sl.subject = s) AND (sl.course = :c) AND (sl.semester = :s)) > 0 )"
            + " ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", group.getPlan());
    q.setParameter("c", course);
    q.setParameter("s", semester);
    return q.getResultList();
  }

  public Subject save(Subject item) {
    if (item.getModuleCode() > 0) {
      StudyModule m = em.find(StudyModule.class, item.getModuleCode());
      if (null != m) {
        item.setModule(m);
      }
    } else {
      item.setModule(null);
    }
    if (item.getPlanCode() > 0) {
      StudyPlan p = em.find(StudyPlan.class, item.getPlanCode());
      if (null != p) {
        item.setPlan(p);
      }
    }
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(Subject item) {
    Subject s = em.find(Subject.class, item.getId());
    if (null != s) {
      em.remove(s);
    }
  }
}
