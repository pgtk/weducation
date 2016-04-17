package ru.edu.pgtk.weducation.ejb;

import ru.edu.pgtk.weducation.entity.GOSMark;
import ru.edu.pgtk.weducation.entity.StudyCard;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("gosMarksEJB")
public class GOSMarksEJB {
  
  @EJB
  private SubjectsDAO subjects;
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  
  public GOSMark get(final int id) {
    GOSMark result = em.find(GOSMark.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("GOSMark not found with id " + id);
  }
  
  public List<GOSMark> fetchAll(final StudyCard card) {
    TypedQuery<GOSMark> q = em.createQuery(
            "SELECT gm FROM GOSMark gm WHERE (gm.card = :c) ORDER BY gm.subject.fullName", GOSMark.class);
    q.setParameter("c", card);
    return q.getResultList();
  }
  
  public GOSMark save(GOSMark item) {
    item.setSubject(subjects.get(item.getSubjectCode()));
    if (0 == item.getId()) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }
  
  public void delete(GOSMark item) {
    GOSMark gm = em.find(GOSMark.class, item.getId());
    if (null != gm) {
      em.remove(gm);
    }
  }
}
