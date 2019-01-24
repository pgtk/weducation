package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Question;
import ru.edu.pgtk.weducation.core.entity.TestDetail;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Корпоративный компонент для работы с вариантами ответов, выбранными пользователем
 *
 * @author Voronin Leonid
 * @since 18.05.2016
 */
@Named("testDetailsEJB")
@Stateless
public class TestDetailsEJB extends AbstractEJB implements TestDetailsDAO {

    @Override
    public List<TestDetail> fetchForQuestion(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("NULL Question must not be here!");
        }
        TypedQuery<TestDetail> q = em.createQuery("SELECT td FROM TestDetail td WHERE (td.question = :q)",
                TestDetail.class);
        q.setParameter("q", question);
        return q.getResultList();
    }

    @Override
    public TestDetail get(int id) {
        TestDetail result = em.find(TestDetail.class, id);
        if (result != null) {
            return result;
        }
        throw new EJBException("TestDetail not found with id " + id);
    }

    @Override
    public TestDetail save(TestDetail item) {
        if (item == null) {
            throw new IllegalArgumentException("You cannot save NULL TestDetail!");
        }
        if (item.getId() > 0) {
            return em.merge(item);
        } else {
            em.persist(item);
            return item;
        }
    }

    @Override
    public void delete(TestDetail item) {
        if (item != null) {
            TestDetail td = em.find(TestDetail.class, item.getId());
            if (td != null) {
                em.remove(td);
            }
        }
    }
}
