package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.WeekMissing;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;

@Stateless
@Named("missingsEJB")
public class WeekMissingsEJB extends AbstractEJB implements WeekMissingsDAO {
    @EJB
    private StudyCardsDAO cards;

    @Override
    public WeekMissing get(int id) {
        WeekMissing result = em.find(WeekMissing.class, id);
        if (result != null) {
            return result;
        }
        throw new EJBException("WeekMissing not found with id " + id);
    }

    @Override
    public WeekMissing get(final StudyCard card, final int year, final int month, final int week) {
        try {
            TypedQuery<WeekMissing> q = em.createQuery("SELECT m FROM WeekMissing m WHERE (m.card = :c) AND (m.year = :y) AND (m.month = :m) AND (m.week = :w)", WeekMissing.class);
            q.setParameter("c", card);
            q.setParameter("y", year);
            q.setParameter("m", month);
            q.setParameter("w", week);
            return q.getSingleResult();
        } catch (NoResultException e) {
            // Создадим новый объект
            WeekMissing weekMissing = new WeekMissing();
            weekMissing.setCard(card);
            weekMissing.setPerson(card.getPerson());
            weekMissing.setMonth(month);
            weekMissing.setYear(year);
            weekMissing.setWeek(week);
            return weekMissing;
        }
        // В остальных случаях - дальше разберемся.
    }

    @Override
    public WeekMissing get(final StudyCard card, final int year, final int month) {
        WeekMissing result = new WeekMissing();
        try {
            TypedQuery<WeekMissing> q = em.createQuery("SELECT m FROM WeekMissing m WHERE (m.card = :c) AND (m.year = :y) AND (m.month = :m)", WeekMissing.class);
            q.setParameter("c", card);
            q.setParameter("y", year);
            q.setParameter("m", month);
            result.setCard(card);
            result.setPerson(card.getPerson());
            result.setYear(year);
            result.setMonth(month);
            int legal = 0;
            int illegal = 0;
            for (WeekMissing m : q.getResultList()) {
                legal += m.getLegal();
                illegal += m.getIllegal();
            }
            result.setLegal(legal);
            result.setIllegal(illegal);
            return result;
        } catch (NoResultException e) {
            // Возвратим всё по нулям
            return result;
        }
        // В остальных случаях - дальше разберемся.
    }

    @Override
    public List<WeekMissing> fetchAll(final StudyGroup group, final int year, final int month, final int week) {
        List<WeekMissing> result = new LinkedList<>();
        for (StudyCard sc : cards.findByGroup(group)) {
            result.add(get(sc, year, month, week));
        }
        return result;
    }

    @Override
    public WeekMissing save(WeekMissing item) {
        if (0 == item.getId()) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Override
    public void delete(final WeekMissing item) {
        WeekMissing m = em.find(WeekMissing.class, item.getId());
        if (null != m) {
            em.remove(m);
        }
    }
}
