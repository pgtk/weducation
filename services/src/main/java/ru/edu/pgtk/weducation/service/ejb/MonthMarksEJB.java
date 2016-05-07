package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.MonthMark;
import ru.edu.pgtk.weducation.data.entity.StudyCard;
import ru.edu.pgtk.weducation.data.entity.StudyGroup;
import ru.edu.pgtk.weducation.data.entity.Subject;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;

@Stateless
@Named("monthMarksEJB")
public class MonthMarksEJB {
 
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  @EJB
  private StudycardsDAO cards;
  
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
  
  public List<MonthMark> fetchAll(final StudyGroup group, final Subject subject, final int year, final int month) {
    List<MonthMark> result = new LinkedList<>();
    for (StudyCard sc: cards.findByGroup(group)) {
      result.add(get(sc, subject, year, month));
    }
    return result;
  }
  
  public MonthMark save(MonthMark item) {
    if (0 == item.getId()) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }
  
  public void delete(final MonthMark item) {
    MonthMark m = em.find(MonthMark.class, item.getId());
    if (null != m) {
      em.remove(m);
    }
  }
}
