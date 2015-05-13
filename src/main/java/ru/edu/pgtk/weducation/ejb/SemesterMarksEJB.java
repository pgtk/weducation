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
import ru.edu.pgtk.weducation.entity.MonthMark;
import ru.edu.pgtk.weducation.entity.SemesterMark;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.Subject;

@Stateless
@Named("semesterMarksEJB")
public class SemesterMarksEJB {
 
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  @EJB
  private StudyCardsEJB cards;
  
  public SemesterMark get(final StudyCard card, final Subject subject, final int course, final int semester) {
    try {
    TypedQuery<SemesterMark> q = em.createQuery(
      "SELECT m FROM SemesterMark m WHERE (m.card = :c) AND (m.subject = :s) AND (m.course = :cr) AND (m.semester = :sm)", SemesterMark.class);
    q.setParameter("c", card);
    q.setParameter("s", subject);
    q.setParameter("cr", course);
    q.setParameter("sm", semester);
    return q.getSingleResult();
    } catch (NoResultException e) {
      // Создадим новый объект
      SemesterMark mark = new SemesterMark();
      mark.setCard(card);
      mark.setPerson(card.getPerson());
      mark.setSubject(subject);
      mark.setCourse(course);
      mark.setSemester(semester);
      return mark;
    }
    // В остальных случаях - дальше разберемся.
  }
  
  public List<SemesterMark> fetchAll(final StudyGroup group, final Subject subject, final int course, final int semester) {
    List<SemesterMark> result = new LinkedList<>();
    for (StudyCard sc: cards.findByGroup(group)) {
      result.add(get(sc, subject, course, semester));
    }
    return result;
  }
  
  public SemesterMark save(SemesterMark item) {
    if (0 == item.getId()) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }
  
  public void delete(final SemesterMark item) {
    SemesterMark m = em.find(SemesterMark.class, item.getId());
    if (null != m) {
      em.remove(m);
    }
  }
}
