package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.TestList;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Корпоративный компонент для работы со списками тестов.
 * @author Voronin Leonid
 * @since 18.05.2016
 */
@Named("testListsEJB")
@Stateless
public class TestListsEJB extends AbstractEJB implements TestListsDAO {

	@Override
	public List<TestList> fetchAll() {
		TypedQuery<TestList> q = em.createQuery("SELECT tl FROM TestList tl", TestList.class);
		return q.getResultList();
	}

	@Override
	public TestList get(int id) {
		TestList result = em.find(TestList.class, id);
		if (result != null) {
			return result;
		}
		throw new EJBException("TestList not found with id " + id);
	}

	@Override
	public TestList save(TestList item) {
		if (item == null) {
			throw new IllegalArgumentException("You cannot save NULL TestList");
		}
		if (item.getId() > 0) {
			return em.merge(item);
		} else {
			em.persist(item);
			return item;
		}
	}

	@Override
	public void delete(TestList item) {
		if (item != null) {
			TestList tl = em.find(TestList.class, item.getId());
			if (tl != null) {
				em.remove(tl);
			}
		}
	}
}
