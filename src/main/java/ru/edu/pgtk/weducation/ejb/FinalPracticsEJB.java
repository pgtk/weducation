package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.FinalPractic;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyPlan;

@Stateless
@Named("finalPracticsEJB")
public class FinalPracticsEJB {
  
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  
  public FinalPractic get(final int id) {
    FinalPractic result = em.find(FinalPractic.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("FinalPractic not found with id " + id);
  }
  
  public List<FinalPractic> fetchAll(final StudyPlan plan) {
    TypedQuery<FinalPractic> q = em.createQuery(
            "SELECT fp FROM FinalPractic fp WHERE (fp.plan = :pln) ORDER BY fp.number, fp.name", FinalPractic.class);
    q.setParameter("pln", plan);
    return q.getResultList();
  }

  public List<FinalPractic> fetchForCard(final StudyCard card) {
    TypedQuery<FinalPractic> q = em.createQuery(
            "SELECT fp FROM FinalPractic fp WHERE (fp.plan = :pln) AND "
                    + "(fp.id NOT IN (SELECT fpm.practic.id FROM FinalPracticMark"
                    + " fpm WHERE (fpm.card = :c))) ORDER BY fp.number, fp.name", FinalPractic.class);
    q.setParameter("pln", card.getPlan());
    q.setParameter("c", card);
    return q.getResultList();
  }
  
  public FinalPractic save(FinalPractic item) {
    if (item.getPlanCode() > 0) {
      StudyPlan sp = em.find(StudyPlan.class, item.getPlanCode());
      if (null != sp) {
        item.setPlan(sp);
      } else {
        throw new EJBException("StudyPlan not found with id " + item.getPlanCode());
      }
    } else {
      throw new EJBException("Wrong StudyPlan code " + item.getPlanCode());
    }
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }
  
  public void delete(final FinalPractic item) {
    FinalPractic fp = em.find(FinalPractic.class, item.getId());
    if (null != fp) {
      em.remove(item);
    }
  }
}
