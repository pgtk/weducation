package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.GOSExam;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;
import ru.edu.pgtk.weducation.core.entity.Subject;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("gosexamsEJB")
public class GOSExamsEJB extends AbstractEJB implements GOSExamsDAO {

    @EJB
    private SubjectsDAO subjects;

    @Override
    public GOSExam get(final int id) {
        GOSExam result = em.find(GOSExam.class, id);
        if (null != result) {
            return result;
        }
        throw new EJBException("GOSExam not found with id " + id);
    }

    @Override
    public List<GOSExam> fetchAll(final StudyPlan plan) {
        TypedQuery<GOSExam> q = em.createQuery(
                "SELECT g FROM GOSExam g WHERE (g.plan = :p) ORDER BY g.subject.fullName", GOSExam.class);
        q.setParameter("p", plan);
        return q.getResultList();
    }

    @Override
    public List<Subject> fetchForCard(final StudyCard card) {
        TypedQuery<Subject> q = em.createQuery(
                "SELECT g.subject FROM GOSExam g WHERE (g.plan = :p)"
                        + "AND (g.subject.id NOT IN (SELECT gm.subject.id FROM GOSMark gm WHERE (gm.card = :c)))"
                        + "ORDER BY g.subject.fullName", Subject.class);
        q.setParameter("p", card.getPlan());
        q.setParameter("c", card);
        return q.getResultList();
    }

    @Override
    public GOSExam save(GOSExam item) {
        if (item == null) {
            throw new IllegalArgumentException("You can't save NULL GOSExam!");
        }
        item.setSubject(subjects.get(item.getSubjectCode()));
        if (item.getId() == 0) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Override
    public void delete(final GOSExam item) {
        GOSExam g = em.find(GOSExam.class, item.getId());
        if (null != g) {
            em.remove(g);
        }
    }
}
