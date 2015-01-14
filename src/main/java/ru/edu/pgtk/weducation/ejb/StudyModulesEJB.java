package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;

@Stateless
@Named("studyModulesEJB")
public class StudyModulesEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  public StudyModule get(final int id) {
    StudyModule result = em.find(StudyModule.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("StudyModule not found with id " + id);
  }
  
  public StudyPlan getPlan(final int id) {
    StudyPlan result = em.find(StudyPlan.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("StudyPlan not found with id " + id);
  }

  public List<StudyModule> fetchAll() {
    TypedQuery<StudyModule> q = em.createQuery("SELECT sm FROM StudyModule sm ORDER BY sm.name", StudyModule.class);
    return q.getResultList();
  }

  public List<StudyModule> findByPlan(final StudyPlan plan) {
    TypedQuery<StudyModule> q = em.createQuery(
            "SELECT sm FROM StudyModule sm WHERE (sm.plan = :pln) ORDER BY sm.name", StudyModule.class);
    q.setParameter("pln", plan);
    return q.getResultList();
  }

  public StudyModule save(StudyModule item) {
    if (item.getPlanCode() > 0) {
      StudyPlan pln = em.find(StudyPlan.class, item.getPlanCode());
      if (null != pln) {
        item.setPlan(pln);
      } else {
        throw new EJBException("Wrong StudyPlan code " + item.getPlanCode());
      }
    }
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(StudyModule item) {
    StudyModule sm = em.find(StudyModule.class, item.getId());
    if (null != sm) {
      em.remove(item);
    }
  }
}
