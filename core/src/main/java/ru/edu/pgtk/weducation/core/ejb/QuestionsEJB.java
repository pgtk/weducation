package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Question;
import ru.edu.pgtk.weducation.core.entity.Test;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Корпоративный компонент для работы с вопросами тестов
 * @author Voronin Leonid
 * @since 17.05.2016
 */
@Named("questionsEJB")
@Stateless
public class QuestionsEJB extends AbstractEJB implements QuestionsDAO {

	@Override
	public List<Question> fetchForTest(Test test) {
		TypedQuery<Question> q = em.createQuery("SELECT q FROM Question q WHERE (q.test = :t) ORDER BY q.id", Question.class);
		q.setParameter("t", test);
		return q.getResultList();
	}

	@Override
	public Question get(int id) {
		Question result = em.find(Question.class, id);
		if (result != null) {
			return result;
		}
		throw new EJBException("Question not found with id " + id);
	}

	@Override
	public Question save(Question item) {
		if (item == null) {
			throw new IllegalArgumentException("Cannot save null Question!");
		}
		if (item.getId() > 0) {
			return em.merge(item);
		} else {
			em.persist(item);
			return item;
		}
	}

	@Override
	public void delete(Question item) {
		if (item != null) {
			Question q = em.find(Question.class, item.getId());
			if (q != null) {
				em.remove(q);
			}
		}
	}
}
