package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Practic;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;

@Stateless
@Named("practicsEJB")
public class PracticsEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  public Practic get(final int id) {
    Practic result = em.find(Practic.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("Practic not found with id " + id);
  }

  public StudyPlan getPlan(final int id) {
    StudyPlan result = em.find(StudyPlan.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("Wrong StudyPlan id " + id);
  }

  public List<Practic> fetchAll() {
    TypedQuery<Practic> q = em.createQuery("SELECT p FROM Practic p ORDER BY p.name", Practic.class);
    return q.getResultList();
  }

  public List<Practic> findByPlan(final StudyPlan plan) {
    TypedQuery<Practic> q = em.createQuery(
            "SELECT p FROM Practic p WHERE (p.plan = :pln) ORDER BY p.name", Practic.class);
    q.setParameter("pln", plan);
    return q.getResultList();
  }

  public Practic save(Practic item) {
    if (item.getPlanCode() > 0) {
      StudyPlan sp = getPlan(item.getPlanCode());
      item.setPlan(sp);
    } else {
      throw new EJBException("Wrong StudyPlan id " + item.getPlanCode());
    }
    if (item.getModuleCode() > 0) {
      StudyModule sm = em.find(StudyModule.class, item.getModuleCode());
      if (null != sm) {
        item.setModule(sm);
      } else {
        throw new EJBException("Wrong StudyModule id " + item.getModuleCode());
      }
    } else {
      item.setModule(null);
    }
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(final Practic item) {
    Practic p = em.find(Practic.class, item.getId());
    if (null != p) {
      em.remove(p);
    }
  }
}
