package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Place;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("placesEJB")
public class PlacesEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  public Place get(final int id) {
    Place result = em.find(Place.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("Place not found with id " + id);
  }

  public List<Place> fetchAll() {
    TypedQuery<Place> q = em.createQuery(
            "SELECT p FROM Place p ORDER BY p.name", Place.class);
    return q.getResultList();
  }

  public List<Place> findByName(final String name) {
    TypedQuery<Place> q = em.createQuery(
            "SELECT p FROM Place p WHERE (p.name LIKE :n) ORDER BY p.name", Place.class);
    q.setParameter("n", name);
    return q.getResultList();
  }

  public Place findLike(final Place sample) {
    Place result;
    TypedQuery<Place> q = em.createQuery(
            "SELECT p FROM Place p WHERE (p.name LIKE :n) AND (p.type = :t)", Place.class);
    q.setParameter("n", sample.getName());
    q.setParameter("t", sample.getType());
    try {
      result = q.getSingleResult();
    } catch (Exception e) {
      result = null;
    }
    return result;
  }

  public Place save(Place item) {
    if (0 == item.getId()) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(final Place item) {
    Place p = em.find(Place.class, item.getId());
    if (null != p) {
      em.remove(p);
    }
  }
}
