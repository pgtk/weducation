package ru.edu.pgtk.weducation.ejb;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.WeekMissing;

@Stateless
@Named("weekMissingsEJB")
public class WeekMissingsEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  @EJB
  private StudyCardsEJB cards;

  public WeekMissing get(final StudyCard card, final int year, final int month, final int week) {
    try {
      TypedQuery<WeekMissing> q = em.createQuery(
        "SELECT wm FROM WeekMissing wm WHERE (wm.card = :c) AND (wm.year = :y) AND (wm.month = :m) AND (wm.week = :w)", WeekMissing.class);
      q.setParameter("c", card);
      q.setParameter("y", year);
      q.setParameter("m", month);
      q.setParameter("w", week);
      return q.getSingleResult();
    } catch (NoResultException e) {
      // Создадим новый объект
      WeekMissing missing = new WeekMissing();
      missing.setCard(card);
      missing.setPerson(card.getPerson());
      missing.setMonth(month);
      missing.setYear(year);
      missing.setWeek(week);
      return missing;
    }
    // В остальных случаях - дальше разберемся.
  }

  public List<WeekMissing> fetchAll(final StudyGroup group, final int year, final int month, final int week) {
    List<WeekMissing> result = new LinkedList<>();
    for (StudyCard sc : cards.findByGroup(group)) {
      result.add(get(sc, year, month, week));
    }
    return result;
  }

  public WeekMissing save(WeekMissing item) {
    if (0 == item.getId()) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(final WeekMissing item) {
    WeekMissing m = em.find(WeekMissing.class, item.getId());
    if (null != m) {
      em.remove(m);
    }
  }
}
