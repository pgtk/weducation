package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.GOSMark;
import ru.edu.pgtk.weducation.core.entity.StudyCard;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("gosMarksEJB")
public class GOSMarksEJB extends AbstractEJB implements GOSMarksDAO {

    @EJB
    private SubjectsDAO subjects;

    @Override
    public GOSMark get(final int id) {
        GOSMark result = em.find(GOSMark.class, id);
        if (null != result) {
            return result;
        }
        throw new EJBException("GOSMark not found with id " + id);
    }

    public List<GOSMark> fetchAll(final StudyCard card) {
        TypedQuery<GOSMark> q = em.createQuery(
                "SELECT gm FROM GOSMark gm WHERE (gm.card = :c) ORDER BY gm.subject.fullName", GOSMark.class);
        q.setParameter("c", card);
        return q.getResultList();
    }

    @Override
    public GOSMark save(GOSMark item) {
        if (item == null) {
            throw new IllegalArgumentException("You can't save NULL GOSMark!");
        }
        item.setSubject(subjects.get(item.getSubjectCode()));
        if (0 == item.getId()) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Override
    public void delete(GOSMark item) {
        GOSMark gm = em.find(GOSMark.class, item.getId());
        if (null != gm) {
            em.remove(gm);
        }
    }
}
