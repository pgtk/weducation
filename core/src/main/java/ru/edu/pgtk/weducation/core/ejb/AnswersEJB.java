package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Answer;
import ru.edu.pgtk.weducation.core.entity.Question;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Корпоративный компонент для управления вариантами ответа
 * Created by admin on 18.05.2016.
 */
@Named("answersEJB")
@Stateless
public class AnswersEJB extends AbstractEJB implements AnswersDAO {
    @Override
    public List<Answer> fetchForQuestion(Question question) {
        TypedQuery<Answer> q = em.createQuery("SELECT a FROM Answer a WHERE (a.question = :q)", Answer.class);
        q.setParameter("q", question);
        return q.getResultList();
    }

    @Override
    public Answer get(int id) {
        Answer result = em.find(Answer.class, id);
        if (result != null) {
            return result;
        }
        throw new EJBException("Answer not found with id " + id);
    }

    @Override
    public Answer save(Answer item) {
        if (item == null) {
            throw new IllegalArgumentException("You cannot save null Answer");
        }
        if (item.getId() > 0) {
            return em.merge(item);
        } else {
            em.persist(item);
            return item;
        }
    }

    @Override
    public void delete(Answer item) {
        if (item != null) {
            Answer a = em.find(Answer.class, item.getId());
            if (a != null) {
                em.remove(a);
            }
        }
    }
}
