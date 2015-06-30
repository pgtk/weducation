package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.entity.Person;
import ru.edu.pgtk.weducation.entity.Speciality;

/**
 * Корпоративный бин для специальностей
 *
 * @author Воронин Леонид
 */
@Stateless
@Named("specialitiesEJB")
public class SpecialitiesEJB {

  @PersistenceContext(unitName = "weducationPU")
  EntityManager em;

  public Speciality get(final int id) {
    Speciality item = em.find(Speciality.class, id);
    if (null != item) {
      return item;
    }
    throw new EJBException("Speciality not found with id " + id);
  }

  public List<Speciality> fetchAll() {
    TypedQuery<Speciality> query = em.createQuery("SELECT s FROM Speciality s ORDER BY s.key, s.fullName", Speciality.class);
    return query.getResultList();
  }

  public List<Speciality> fetchActual() {
    TypedQuery<Speciality> query = em.createQuery(
            "SELECT s FROM Speciality s WHERE (s.actual = true) ORDER BY s.key, s.fullName", Speciality.class);
    return query.getResultList();
  }

  public List<Speciality> fetchActual(final boolean extramural) {
    TypedQuery<Speciality> query = em.createQuery(
            "SELECT s FROM Speciality s WHERE (s.actual = true) AND "
              + "((SELECT COUNT(dp.id) FROM DepartmentProfile dp WHERE (dp.speciality = s) AND (dp.extramural = :em)) > 0)"
              + "ORDER BY s.key, s.fullName", Speciality.class);
    query.setParameter("em", extramural);
    return query.getResultList();
  }
  
  public List<Speciality> fetchSuggestions(final Person person, final boolean extramural, final int year) {
    TypedQuery<Speciality> query = em.createQuery(
            "SELECT s FROM Speciality s WHERE (s.actual = true) AND "
              + "((SELECT COUNT(dp.id) FROM DepartmentProfile dp WHERE (dp.speciality = s) AND (dp.extramural = :e1)) > 0)"
              + "AND (s.id NOT IN (SELECT r.speciality.id FROM Request r WHERE (r.extramural = :e2) AND (r.person = :p1) AND (r.year = :y)))"
              + "AND (s.id NOT IN (SELECT sc.speciality.id FROM StudyCard sc WHERE (sc.person = :p2) AND (sc.extramural = :e3)))"
              + "ORDER BY s.fullName", Speciality.class);
    query.setParameter("e1", extramural);
    query.setParameter("e2", extramural);
    query.setParameter("e3", extramural);
    query.setParameter("p1", person);
    query.setParameter("p2", person);
    query.setParameter("y", year);
    return query.getResultList();
  }
  
  public List<Speciality> findByDepartment(final Department department) {
    TypedQuery<Speciality> query = em.createQuery(
            "SELECT dp.speciality FROM DepartmentProfile dp WHERE (dp.speciality.actual = true) AND (dp.department = :dep) "
            + "ORDER BY dp.speciality.key, dp.speciality.fullName", Speciality.class);
    query.setParameter("dep", department);
    return query.getResultList();
  }

  public Speciality findByKey(final String key) {
    try {
      TypedQuery<Speciality> query = em.createQuery(
              "SELECT s FROM Speciality s WHERE (s.key = :k) ", Speciality.class);
      query.setParameter("k", key);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    } catch (Exception e) {
      throw new EJBException("Exception class " + e.getClass().getName()
              + " with message " + e.getMessage());
    }
  }

  public Speciality findLike(final Speciality sample) {
    try {
      TypedQuery<Speciality> query = em.createQuery(
              "SELECT s FROM Speciality s WHERE (s.key LIKE :k) AND (s.fullName LIKE :fn)", Speciality.class);
      query.setParameter("k", sample.getKey());
      query.setParameter("fn", sample.getFullName());
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public Speciality save(Speciality speciality) {
    if (speciality.getId() == 0) {
      em.persist(speciality);
      return speciality;
    } else {
      return em.merge(speciality);
    }
  }

  public void delete(final Speciality speciality) {
    Speciality item = em.find(Speciality.class, speciality.getId());
    if (null != item) {
      em.remove(item);
    }
  }
}
