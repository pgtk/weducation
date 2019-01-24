package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.School;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("schoolsEJB")
public class SchoolsEJB extends AbstractEJB implements SchoolsDAO {

    @Override
    public School get(final int id) {
        School result = em.find(School.class, id);
        if (null != result) {
            return result;
        }
        throw new EJBException("School not found with id " + id);
    }

    @Override
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
        throw new EJBException("Can't find current school!");
    }

    @Override
    public List<School> fetchAll() {
        TypedQuery<School> q = em.createQuery(
                "SELECT s FROM School s ORDER BY s.shortName", School.class);
        return q.getResultList();
    }

    @Override
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

    @Override
    public School save(School item) {
        if (item.getId() == 0) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Override
    public void delete(final School item) {
        School s = em.find(School.class, item.getId());
        if (null != s) {
            em.remove(s);
        }
    }
}
