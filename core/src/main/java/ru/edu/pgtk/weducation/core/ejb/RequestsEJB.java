package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.Request;
import ru.edu.pgtk.weducation.core.entity.Speciality;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@RequestScoped
@Named("requestsEJB")
public class RequestsEJB extends AbstractEJB implements RequestsDAO {

    @Override
    public Request get(final int id) {
        Request result = em.find(Request.class, id);
        if (null != result) {
            return result;
        } else {
            throw new EJBException("Request not found with id " + id);
        }
    }

    @Override
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

    @Override
    public List<Request> fetchAll(final Person person) {
        TypedQuery<Request> q = em.createQuery(
                "SELECT r FROM Request r WHERE (r.person = :p) ORDER BY r.speciality.description", Request.class);
        q.setParameter("p", person);
        return q.getResultList();
    }

    @Override
    public List<Request> fetch(final Speciality speciality, final boolean extramural) {
        TypedQuery<Request> q = em.createQuery("SELECT r FROM Request r WHERE (r.speciality = :s) AND (r.extramural = :e)", Request.class);
        q.setParameter("s", speciality);
        q.setParameter("e", extramural);
        return q.getResultList();
    }

    @Override
    public List<Request> fetch(final Person person, final boolean extramural) {
        TypedQuery<Request> q = em.createQuery("SELECT r FROM Request r WHERE (r.person = :p) AND (r.extramural = :e)", Request.class);
        q.setParameter("p", person);
        q.setParameter("e", extramural);
        return q.getResultList();
    }

    @Override
    public List<Request> fetch(final Person person, final boolean extramural, final int year) {
        TypedQuery<Request> q = em.createQuery(
                "SELECT r FROM Request r WHERE (r.person = :p) AND (r.extramural = :e) AND (r.year = :y)", Request.class);
        q.setParameter("p", person);
        q.setParameter("e", extramural);
        q.setParameter("y", year);
        return q.getResultList();
    }

    @Override
    public Request save(Request item) {
        if (item == null) {
            throw new IllegalArgumentException("You can't save NULL Request!");
        }
        if (item.getId() == 0) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Override
    public void delete(final Request item) {
        Request r = em.find(Request.class, item.getId());
        if (null != r) {
            em.remove(r);
        }
    }
}
