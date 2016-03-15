package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Practic;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.StudyModule;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

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

  public List<Practic> findByPlan(final StudyPlan plan) {
    TypedQuery<Practic> q = em.createQuery(
      "SELECT p FROM Practic p WHERE (p.plan = :pln) ORDER BY p.fullName", Practic.class);
    q.setParameter("pln", plan);
    return q.getResultList();
  }
  
  public List<Practic> fetch(final StudyModule module) {
    TypedQuery<Practic> q = em.createQuery("SELECT p FROM Practic p WHERE (p.module = :mod) ORDER BY p.fullName", Practic.class);
    q.setParameter("mod", module);
    return q.getResultList();
  }

  /**
   * Получает из СУБД список практик для определенной группы за указанный период
   * обучения.
   *
   * @param group группа
   * @param course курс
   * @param semester семестр
   * @return Список практик для указанной группы за указанный период обучения
   */
  public List<Practic> fetch(final StudyGroup group, final int course, final int semester) {
    TypedQuery<Practic> q = em.createQuery(
      "SELECT p FROM Practic p WHERE (p.plan = :pln) AND (p.course = :c) AND (p.semester = :s)", Practic.class);
    q.setParameter("pln", group.getPlan());
    q.setParameter("c", course);
    q.setParameter("s", semester);
    return q.getResultList();
  }

  public Practic save(Practic item) {
    if (item.getPlanCode() > 0) {
      StudyPlan sp = em.find(StudyPlan.class, item.getPlanCode());
      if (null != sp) {
        item.setPlan(sp);
      } else {
        throw new EJBException("StudyPlan not found with id " + item.getPlanCode());
      }
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
