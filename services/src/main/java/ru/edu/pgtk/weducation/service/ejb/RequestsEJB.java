package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.Person;
import ru.edu.pgtk.weducation.data.entity.Request;
import ru.edu.pgtk.weducation.data.entity.Speciality;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.persistence.*;
import java.util.List;

@Stateless
@RequestScoped
public class RequestsEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  public Request get(final int id) {
    Request result = em.find(Request.class, id);
    if (null != result) {
      return result;
    } else {
      throw new EJBException("Request not found with id " + id);
    }
  }

  public Request get(final Speciality speciality, final Person person, final int year, final boolean extramural) {
    try {
      TypedQuery<Request> q = em.createQuery(
        "SELECT r FROM Request r WHERE (r.person = :p) AND (r.speciality = :s) AND (r.year = :y) AND (r.extramural = :e)", Request.class);
      q.setParameter("p", person);
      q.setParameter("s", speciality);
      q.setParameter("y", year);
      q.setParameter("e", extramural);
      return q.getSingleResult();
    } catch (NoResultException e) {
      return null;
    } catch (NonUniqueResultException e) {
      throw new EJBException("Request is not unique, but should be!");
    }
  }
  
  public List<Request> fetchAll(final Person person) {
    TypedQuery<Request> q = em.createQuery(
      "SELECT r FROM Request r WHERE (r.person = :p) ORDER BY r.speciality.fullName", Request.class);
    q.setParameter("p", person);
    return q.getResultList();
  }
  
  public List<Request> fetch(final Speciality speciality, final boolean extramural) {
    TypedQuery<Request> q = em.createQuery("SELECT r FROM Request r WHERE (r.speciality = :s) AND (r.extramural = :e)", Request.class);
    q.setParameter("s", speciality);
    q.setParameter("e", extramural);
    return q.getResultList();
  }
  
  public List<Request> fetch(final Person person, final boolean extramural) {
    TypedQuery<Request> q = em.createQuery("SELECT r FROM Request r WHERE (r.person = :p) AND (r.extramural = :e)", Request.class);
    q.setParameter("p", person);
    q.setParameter("e", extramural);
    return q.getResultList();
  }
  
  public List<Request> fetch(final Person person, final boolean extramural, final int year) {
    TypedQuery<Request> q = em.createQuery(
      "SELECT r FROM Request r WHERE (r.person = :p) AND (r.extramural = :e) AND (r.year = :y)", Request.class);
    q.setParameter("p", person);
    q.setParameter("e", extramural);
    q.setParameter("y", year);
    return q.getResultList();
  }
  
  public Request save(Request item) {
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }
  
  public void delete(final Request item) {
    Request r = em.find(Request.class, item.getId());
    if (null != r) {
      em.remove(r);
    }
  }
}
