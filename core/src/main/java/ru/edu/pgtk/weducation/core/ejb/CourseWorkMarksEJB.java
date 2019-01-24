package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.AccountRole;
import ru.edu.pgtk.weducation.core.entity.CourseWorkMark;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.Subject;
import ru.edu.pgtk.weducation.core.interceptors.Restricted;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;

@Stateless
@Named("courseWorkMarksEJB")
public class CourseWorkMarksEJB extends AbstractEJB implements CourseWorkMarksDAO {

    @EJB
    SubjectsDAO subjects;
    @EJB
    StudyCardsDAO cards;

    @Override
    public CourseWorkMark get(final int id) {
        CourseWorkMark result = em.find(CourseWorkMark.class, id);
        if (null != result) {
            return result;
        }
        throw new EJBException("CourseWorkMark not found with id " + id);
    }

    @Override
    public CourseWorkMark get(final StudyCard card, final Subject subject, final int course, final int semester) {
        try {
            TypedQuery<CourseWorkMark> q = em.createQuery("SELECT m FROM CourseWorkMark m WHERE (m.card = :crd) AND "
                    + "(m.subject = :sub) AND (m.course = :crs) AND (m.semester = :sem)", CourseWorkMark.class);
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

    @Override
    public List<CourseWorkMark> fetchAll(final StudyGroup group, final Subject subject, final int course, final int semester) {
        List<CourseWorkMark> result = new LinkedList<>();
        for (StudyCard sc : cards.findByGroup(group)) {
            result.add(get(sc, subject, course, semester));
        }
        return result;
    }

    @Override
    public List<CourseWorkMark> fetchAll(final StudyCard card) {
        TypedQuery<CourseWorkMark> q = em.createQuery(
                "SELECT cm FROM CourseWorkMark cm WHERE (cm.card = :c) ORDER BY cm.subject.number,  cm.subject.fullName", CourseWorkMark.class);
        q.setParameter("c", card);
        return q.getResultList();
    }

    @Override
    @Restricted(allowedRoles = {AccountRole.DEPARTMENT, AccountRole.DEPOT})
    public CourseWorkMark save(CourseWorkMark item) {
        item.setSubject(subjects.get(item.getSubjectCode()));
        if (0 == item.getId()) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Override
    @Restricted(allowedRoles = {AccountRole.DEPARTMENT, AccountRole.DEPOT})
    public void delete(final CourseWorkMark item) {
        CourseWorkMark cm = em.find(CourseWorkMark.class, item.getId());
        if (null != cm) {
            em.remove(cm);
        }
    }
}
