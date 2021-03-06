package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.FinalPracticMark;
import ru.edu.pgtk.weducation.core.entity.StudyCard;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("finalPracticMarksEJB")
public class FinalPracticMarksEJB extends AbstractEJB implements FinalPracticMarksDAO {
    @EJB
    private FinalPracticsDAO practics;

    @Override
    public FinalPracticMark get(final int id) {
        FinalPracticMark result = em.find(FinalPracticMark.class, id);
        if (null != result) {
            return result;
        }
        throw new EJBException("FinalPracticMark not found with id " + id);
    }

    @Override
    public List<FinalPracticMark> fetchAll(final StudyCard card) {
        TypedQuery<FinalPracticMark> q = em.createQuery(
                "SELECT fpm FROM FinalPracticMark fpm WHERE (fpm.card = :c) ORDER BY fpm.practic.number, fpm.practic.name", FinalPracticMark.class);
        q.setParameter("c", card);
        return q.getResultList();
    }

    @Override
    public float getSummaryLoad(final StudyCard card) {
        try {
            TypedQuery<Double> q = em.createQuery("SELECT SUM(fpm.practic.length) FROM FinalPracticMark fpm WHERE (fpm.card = :c)", Double.class);
            q.setParameter("c", card);
            double result = q.getSingleResult();
            return (float) result;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public FinalPracticMark save(FinalPracticMark item) {
        if (item == null) {
            throw new IllegalArgumentException("You can't save NULL FinalPracticMark!");
        }
        item.setPractic(practics.get(item.getPracticCode()));
        if (0 == item.getId()) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Override
    public void delete(final FinalPracticMark item) {
        FinalPracticMark fpm = em.find(FinalPracticMark.class, item.getId());
        if (null != fpm) {
            em.remove(fpm);
        }
    }
}
