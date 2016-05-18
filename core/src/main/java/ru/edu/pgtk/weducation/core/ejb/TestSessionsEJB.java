package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.Test;
import ru.edu.pgtk.weducation.core.entity.TestSession;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Корпоративный компонент для сеансов тестирования
 * Created by admin on 18.05.2016.
 */
@Named("testSessionsEJB")
@Stateless
public class TestSessionsEJB extends AbstractEJB implements TestSessionsDAO {

	@Override
	public List<TestSession> fetchForTest(Test test) {
		TypedQuery<TestSession> q = em.createQuery(
				"SELECT ts FROM TestSession ts WHERE (ts.test = :t)", TestSession.class);
		q.setParameter("t", test);
		return q.getResultList();
	}

	@Override
	public List<TestSession> fetchFroPerson(Person person) {
		TypedQuery<TestSession> q = em.createQuery(
				"SELECT  psn FROM TestSession psn WHERE (psn.test = :p)", TestSession.class);
		q.setParameter("p", person);
		return q.getResultList();
	}

	@Override
	public List<TestSession> fetchAll() {
		TypedQuery<TestSession> q = em.createQuery(
				"SELECT ts FROM TestSession ts", TestSession.class);
		return q.getResultList();
	}

	@Override
	public TestSession get(int id) {
		TestSession result = em.find(TestSession.class, id);
		if (result != null) {
			return result;
		}
		throw new EJBException("Answer not found with id " + id);

	}

	@Override
	public TestSession save(TestSession item) {
		if (item == null) {
			throw new EJBException("You can't save nothing");
		}
		if (item.getId() > 0) {
			return em.merge(item);
		} else {
			em.persist(item);
			return item;
		}

	}

	@Override
	public void delete(TestSession item) {
		if (item != null) {
			TestSession ts = em.find(TestSession.class, item.getId());
			if (ts != null) {
				em.remove(ts);
			}
		}
	}
}
