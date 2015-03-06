package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;

@Stateless
@Named("studyModulesEJB")
public class StudyModulesEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  @EJB
  private StudyPlansEJB plans;

  public StudyModule get(final int id) {
    StudyModule result = em.find(StudyModule.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("StudyModule not found with id " + id);
  }

  public List<StudyModule> fetchAll(final StudyPlan plan) {
    TypedQuery<StudyModule> q = em.createQuery(
            "SELECT sm FROM StudyModule sm WHERE (sm.plan = :pln) ORDER BY sm.name", StudyModule.class);
    q.setParameter("pln", plan);
    return q.getResultList();
  }

  public List<StudyModule> fetchForCard(final StudyCard card) {
    TypedQuery<StudyModule> q = em.createQuery(
            "SELECT sp FROM StudyModule sp WHERE (sp.plan = :pln) AND "
            + "(sp.id NOT IN (SELECT fm.module.id FROM FinalMark fm WHERE (fm.card = :c))) ORDER BY sp.name", StudyModule.class);
    q.setParameter("pln", card.getPlan());
    q.setParameter("c", card);
    return q.getResultList();
  }

  public StudyModule save(StudyModule item) {
    item.setPlan(plans.get(item.getPlanCode()));
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(final StudyModule item) {
    StudyModule sm = em.find(StudyModule.class, item.getId());
    if (null != sm) {
      em.remove(sm);
    }
  }
}
