package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Speciality;
import ru.edu.pgtk.weducation.core.entity.Test;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Корпоративный компонент для управления тестами
 * Created by admin on 17.05.2016.
 */
@Named("testsEJB")
@Stateless
public class TestsEJB extends AbstractEJB implements TestsDAO {

    @Override
    public List<Test> fetchAll() {
        TypedQuery<Test> q = em.createQuery("SELECT t FROM Test t", Test.class);
        return q.getResultList();
    }

    @Override
    public List<Test> fetchForSpeciality(Speciality speciality) {
        if (speciality == null) {
            throw new IllegalArgumentException("Cannot fetch Tests for NULL speciality!");
        }
        TypedQuery<Test> q = em.createQuery(
                "SELECT t FROM Test t WHERE (t.id IN (SELECT tl.id FROM TestList tl WHERE (tl.speciality = :s))) ORDER BY t.title",
                Test.class);
        q.setParameter("s", speciality);
        return q.getResultList();
    }

    @Override
    public Test get(int id) {
        Test result = em.find(Test.class, id);
        if (result != null) {
            return result;
        }
        throw new EJBException("Test not found with id " + id);
    }

    @Override
    public Test save(Test item) {
        if (item == null) {
            throw new EJBException("You cannot save NULL instead of Test instance");
        }
        if (item.getId() > 0) {
            // update
            return em.merge(item);
        } else {
            //add
            em.persist(item);
            return item;
        }
    }

    @Override
    public void delete(Test item) {
        if (item != null) {
            Test result = em.find(Test.class, item.getId());
            if (result != null) {
                em.remove(result);
            }
        }
    }
}
