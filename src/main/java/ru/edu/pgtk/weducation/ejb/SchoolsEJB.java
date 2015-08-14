package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.School;

@Stateless
@Named("schoolsEJB")
public class SchoolsEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  public School get(final int id) {
    School result = em.find(School.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("School not found with id " + id);
  }
  
  @Produces
  public School getCurrent() {
    TypedQuery<School> q = em.createQuery(
            "SELECT s FROM School s WHERE s.current = true", School.class);
    List<School> result = q.getResultList();
    if (!result.isEmpty()) {
      return result.get(0);
    }
    q = em.createQuery(
            "SELECT s FROM School s ORDER BY s.shortName", School.class);
    result = q.getResultList();
    if (!result.isEmpty()) {
      return result.get(0);
    }
    throw new EJBException("Schools table is empty!");
  }

  public List<School> fetchAll() {
    TypedQuery<School> q = em.createQuery(
            "SELECT s FROM School s ORDER BY s.shortName", School.class);
    return q.getResultList();
  }

  public School findLike(final School sample) {
    try {
      TypedQuery<School> q = em.createQuery(
              "SELECT s FROM School s WHERE (s.fullName LIKE :fn) AND (s.place LIKE :pl)", School.class);
      q.setParameter("fn", sample.getFullName());
      q.setParameter("pl", sample.getPlace());
      return q.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public School save(School item) {
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(final School item) {
    School s = em.find(School.class, item.getId());
    if (null != s) {
      em.remove(s);
    }
  }
}
