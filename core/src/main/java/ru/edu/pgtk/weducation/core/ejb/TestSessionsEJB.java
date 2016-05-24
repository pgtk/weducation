package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.Test;
import ru.edu.pgtk.weducation.core.entity.TestSession;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Корпоративный компонент для сеансов тестирования
 * Created by admin on 18.05.2016.
 */
@Named("testSessionsEJB")
@Stateless
public class TestSessionsEJB extends AbstractEJB implements TesSessionsDAO {

    @Override
    public List<TestSession> fetchForTest(Test test) {
        TypedQuery<TestSession> q = em.createQuery(
                "SELECT ts FROM TestSession ts WHERE (ts.test = :t)", TestSession.class);
        q.setParameter("t", test);
    }

    @Override
    public List<TestSession> fetch(Test test, Person person) {
        if (test == null || person == null) {
            throw new IllegalArgumentException("Cannot fetch TestSession if Test or Person is NULL!");
        }
        TypedQuery<TestSession> q = em.createQuery(
                "SELECT ts FROM TestSession ts WHERE (ts.test = :t) AND (ts.person = :p)", TestSession.class);
        q.setParameter("t", test);
        q.setParameter("p", person);
        return q.getResultList();
    }

    @Override
    public List<TestSession> fetchFroPerson(Person person) {
        TypedQuery<TestSession> q = em.createQuery(
                "SELECT  psn FROM TestSession psn WHERE (psn.test = :p)", TestSession.class);
        q.setParameter("p", person);
        return q.getResultList();
    }

    public int getSessionsCount(Test test, Person person) {
        if (test == null || person == null) {
            return 0;
        }
        TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(ts) FROM TestSession ts WHERE (ts.test = :t) AND (ts.person = :p)", Long.class);
        q.setParameter("t", test);
        q.setParameter("p", person);
        return q.getSingleResult().intValue();
    }

    @Override
    public List<TestSession> fetchAll() {
        TypedQuery<TestSession> q = em.createQuery(
                "SELECT ts FROM TestSession ts", TestSession.class);
        return q.getResultList();
    }

    @Override
    public TestSession get(int id) {
        TestSession result = em.find(TestSession.class, id);
        if (result != null) {
            return result;
        }
        throw new EJBException("Answer not found with id " + id);

    }

    @Override
    public TestSession save(TestSession item) {
        if (item == null) {
            throw new EJBException("You can't save nothing");
        }
        if (item.getId() > 0) {
            return em.merge(item);
        } else {
            em.persist(item);
            return item;
        }

    }

    @Override
    public void delete(TestSession item) {
        if (item != null) {
            TestSession ts = em.find(TestSession.class, item.getId());
            if (ts != null) {
                em.remove(ts);
            }
        }
    }
}
