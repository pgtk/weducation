package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.GOSExam;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.entity.Subject;

@Stateless
@Named("gosexamsEJB")
public class GOSExamsEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  
  public GOSExam get(final int id) {
    GOSExam result = em.find(GOSExam.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("GOSExam not found with id " + id);
  }
  
  public List<GOSExam> fetchAll(final StudyPlan plan) {
    TypedQuery<GOSExam> q = em.createQuery(
            "SELECT g FROM GOSExam g WHERE (g.plan = :p) ORDER BY g.subject", GOSExam.class);
    q.setParameter("p", plan);
    return q.getResultList();
  }
  
  public List<Subject> fetchForCard(final StudyCard card) {
    TypedQuery<Subject> q = em.createQuery(
            "SELECT g.subject FROM GOSExam g WHERE (g.plan = :p)"
                    + "AND (g.subject.id NOT IN (SELECT gm.subject.id FROM GOSMark gm WHERE (gm.card = :c)))"
                    + "ORDER BY g.subject.fullName", Subject.class);
    q.setParameter("p", card.getPlan());
    q.setParameter("c", card);
    return q.getResultList();
  }
  
  public GOSExam save(GOSExam item) {
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
  
  public void delete(final GOSExam item) {
    GOSExam g = em.find(GOSExam.class, item.getId());
    if (null != g) {
      em.remove(g);
    }
  }
}
