package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.MonthMark;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.Subject;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;

@Stateless
@Named("monthMarksEJB")
public class MonthMarksEJB extends AbstractEJB implements MonthMarksDAO {
    @EJB
    private StudyCardsDAO cards;

    @Override
    public MonthMark get(int id) {
        MonthMark result = em.find(MonthMark.class, id);
        if (result != null) {
            return result;
        }
        throw new EJBException("MonthMark not found with id " + id);
    }

    @Override
    public MonthMark get(final StudyCard card, final Subject subject, final int year, final int month) {
        try {
            TypedQuery<MonthMark> q = em.createQuery(
                    "SELECT m FROM MonthMark m WHERE (m.card = :c) AND (m.subject = :s) AND (m.year = :y) AND (m.month = :mn)", MonthMark.class);
            q.setParameter("c", card);
            q.setParameter("s", subject);
            q.setParameter("y", year);
            q.setParameter("mn", month);
            return q.getSingleResult();
        } catch (NoResultException e) {
            // Создадим новый объект
            MonthMark mark = new MonthMark();
            mark.setCard(card);
            mark.setPerson(card.getPerson());
            mark.setSubject(subject);
            mark.setMonth(month);
            mark.setYear(year);
            return mark;
        }
        // В остальных случаях - дальше разберемся.
    }

    @Override
    public List<MonthMark> fetchAll(final StudyGroup group, final Subject subject, final int year, final int month) {
        List<MonthMark> result = new LinkedList<>();
        for (StudyCard sc : cards.findByGroup(group)) {
            result.add(get(sc, subject, year, month));
        }
        return result;
    }

    @Override
    public MonthMark save(MonthMark item) {
        if (item == null) {
            throw new IllegalArgumentException("You can't save NULL MonthMark!");
        }
        if (0 == item.getId()) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Override
    public void delete(final MonthMark item) {
        MonthMark m = em.find(MonthMark.class, item.getId());
        if (null != m) {
            em.remove(m);
        }
    }
}
