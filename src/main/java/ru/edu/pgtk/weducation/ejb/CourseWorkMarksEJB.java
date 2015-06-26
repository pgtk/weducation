package ru.edu.pgtk.weducation.ejb;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.CourseWorkMark;
import ru.edu.pgtk.weducation.entity.SemesterMark;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.Subject;

@Stateless
@Named("courseWorkMarksEJB")
public class CourseWorkMarksEJB {
  
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  @Inject
  SubjectsEJB subjects;
  @Inject
  StudyCardsEJB cards;
  
  public CourseWorkMark get(final int id) {
    CourseWorkMark result = em.find(CourseWorkMark.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("CourseWorkMark not found with id " + id);
  }
  
  public CourseWorkMark get(final StudyCard card, final Subject subject, final int course, final int semester) {
    try {
    TypedQuery<CourseWorkMark> q = em.createQuery(
      "SELECT m FROM CourseWorkMark m WHERE (m.card = :crd) AND (m.subject = :sub) AND (m.course = :crs) AND (m.semester = :sem)", CourseWorkMark.class);
    q.setParameter("crd", card);
    q.setParameter("sub", subject);
    q.setParameter("crs", course);
    q.setParameter("sem", semester);
    return q.getSingleResult();
    } catch (NoResultException e) {
      // Создадим новый объект
      CourseWorkMark mark = new CourseWorkMark();
      mark.setCard(card);
      mark.setSubject(subject);
      return mark;
    }
    // В остальных случаях - дальше разберемся.
  }

  public List<CourseWorkMark> fetchAll(final StudyGroup group, final Subject subject, final int course, final int semester) {
    List<CourseWorkMark> result = new LinkedList<>();
    for (StudyCard sc: cards.findByGroup(group)) {
      result.add(get(sc, subject, course, semester));
    }
    return result;
  }
  
  
  public List<CourseWorkMark> fetchAll(final StudyCard card) {
    TypedQuery<CourseWorkMark> q = em.createQuery(
            "SELECT cm FROM CourseWorkMark cm WHERE (cm.card = :c) ORDER BY cm.subject.fullName", CourseWorkMark.class);
    q.setParameter("c", card);
    return q.getResultList();
  }
  
  public CourseWorkMark save(CourseWorkMark item) {
    item.setSubject(subjects.get(item.getSubjectCode()));
    if (0 == item.getId()) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }
  
  public void delete(final CourseWorkMark item) {
    CourseWorkMark cm = em.find(CourseWorkMark.class, item.getId());
    if (null != cm) {
      em.remove(cm);
    }
  }
}
