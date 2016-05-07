package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.FinalPracticMark;
import ru.edu.pgtk.weducation.data.entity.StudyCard;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("finalPracticMarksEJB")
public class FinalPracticMarksEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  @EJB
  private FinalPracticsEJB practics;

  public FinalPracticMark get(final int id) {
    FinalPracticMark result = em.find(FinalPracticMark.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("FinalPracticMark not found with id " + id);
  }

  public List<FinalPracticMark> fetchAll(final StudyCard card) {
    TypedQuery<FinalPracticMark> q = em.createQuery(
            "SELECT fpm FROM FinalPracticMark fpm WHERE (fpm.card = :c) ORDER BY fpm.practic.number, fpm.practic.name", FinalPracticMark.class);
    q.setParameter("c", card);
    return q.getResultList();
  }

  public float getSummaryLoad(final StudyCard card) {
    try {
      TypedQuery<Double> q = em.createQuery("SELECT SUM(fpm.practic.length) FROM FinalPracticMark fpm WHERE (fpm.card = :c)", Double.class);
      q.setParameter("c", card);
      double result = q.getSingleResult();
      return (float) result; 
    } catch (Exception e) {
      return 0;
    }
  }

  public FinalPracticMark save(FinalPracticMark item) {
    item.setPractic(practics.get(item.getPracticCode()));
    if (0 == item.getId()) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(final FinalPracticMark item) {
    FinalPracticMark fpm = em.find(FinalPracticMark.class, item.getId());
    if (null != fpm) {
      em.remove(fpm);
    }
  }
}
