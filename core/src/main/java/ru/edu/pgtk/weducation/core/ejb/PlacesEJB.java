package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Place;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Named("placesEJB")
public class PlacesEJB extends AbstractEJB implements PlacesDAO {

    @Override
    public Place get(final int id) {
        Place result = em.find(Place.class, id);
        if (null != result) {
            return result;
        }
        throw new EJBException("Place not found with id " + id);
    }

    @Override
    public List<Place> fetchAll() {
        TypedQuery<Place> q = em.createQuery(
                "SELECT p FROM Place p ORDER BY p.name", Place.class);
        return q.getResultList();
    }

    @Override
    public List<Place> findByName(final String name) {
        TypedQuery<Place> q = em.createQuery(
                "SELECT p FROM Place p WHERE (p.name LIKE :n) ORDER BY p.name", Place.class);
        q.setParameter("n", name);
        return q.getResultList();
    }

    @Override
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

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Place save(Place item) {
        if (item == null) {
            throw new IllegalArgumentException("You can't save NULL Place!");
        }
        if (0 == item.getId()) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(final Place item) {
        Place p = em.find(Place.class, item.getId());
        if (null != p) {
            em.remove(p);
        }
    }
}
