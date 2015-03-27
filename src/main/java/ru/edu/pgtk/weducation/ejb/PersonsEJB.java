package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Person;

@Stateless
@Named("personsEJB")
public class PersonsEJB {
  
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  @EJB
  private PlacesEJB places;
  
  public Person get(final int id) {
    Person result = em.find(Person.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("Person not found with id " + id);
  }
  
  public List<Person> fetchAll() {
    TypedQuery<Person> q = em.createQuery(
            "SELECT p FROM Person p ORDER BY p.firstName, p.middleName, p.lastName", Person.class);
    return q.getResultList();
  }
  
  public List<Person> findByName(final String fname) {
    TypedQuery<Person> q = em.createQuery(
            "SELECT p FROM Person p WHERE (p.firstName LIKE :fname) "
                    + "ORDER BY p.firstName, p.middleName, p.lastName", Person.class);
    q.setParameter("fname", fname + "%");
    return q.getResultList();
  }
  
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
  
  public void delete(final Person item) {
    Person p = em.find(Person.class, item.getId());
    if (null != p) {
      em.remove(p);
    }
  }  
}
