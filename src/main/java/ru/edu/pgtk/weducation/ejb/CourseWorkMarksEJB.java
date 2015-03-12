package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.CourseWorkMark;
import ru.edu.pgtk.weducation.entity.StudyCard;

@Stateless
@Named("courseWorkMarksEJB")
public class CourseWorkMarksEJB {
  
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  @EJB
  SubjectsEJB subjects;
  
  public CourseWorkMark get(final int id) {
    CourseWorkMark result = em.find(CourseWorkMark.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("CourseWorkMark not found with id " + id);
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
