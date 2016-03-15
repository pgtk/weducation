package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Person;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("personsEJB")
public class PersonsEJB extends AbstractEJB implements PersonsDAO {

  @EJB
  private PlacesEJB places;

  @Override
  public Person get(final int id) {
    Person result = em.find(Person.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("Person not found with id " + id);
  }

  @Override
  public List<Person> fetchAll() {
    TypedQuery<Person> q = em.createQuery(
            "SELECT p FROM Person p ORDER BY p.firstName, p.middleName, p.lastName", Person.class);
    return q.getResultList();
  }

  @Override
  public List<Person> findByName(final String fname) {
    TypedQuery<Person> q = em.createQuery(
            "SELECT p FROM Person p WHERE (p.firstName LIKE :fname) "
            + "ORDER BY p.firstName, p.middleName, p.lastName", Person.class);
    q.setParameter("fname", fname + "%");
    return q.getResultList();
  }

  @Override
  public Person findLike(final Person sample) {
    try {
      TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE (p.firstName LIKE :fn) "
              + "AND (p.middleName LIKE :mn) AND (p.lastName LIKE :ln) "
              + "AND (p.passportSeria LIKE :ps) AND (p.passportNumber LIKE :pn)", Person.class);
      q.setParameter("fn", sample.getFirstName());
      q.setParameter("mn", sample.getMiddleName());
      q.setParameter("ln", sample.getLastName());
      q.setParameter("ps", sample.getPassportSeria());
      q.setParameter("pn", sample.getPassportNumber());
      return q.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public Person save(Person item) {
    if (item.getPlaceCode() > 0) {
      item.setPlace(places.get(item.getPlaceCode()));
    }
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  @Override
  public void delete(final Person item) {
    Person p = em.find(Person.class, item.getId());
    if (null != p) {
      em.remove(p);
    }
  }
}
