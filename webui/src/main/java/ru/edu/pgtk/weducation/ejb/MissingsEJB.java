package ru.edu.pgtk.weducation.ejb;

import ru.edu.pgtk.weducation.data.entity.Missing;
import ru.edu.pgtk.weducation.data.entity.StudyCard;
import ru.edu.pgtk.weducation.data.entity.StudyGroup;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.LinkedList;
import java.util.List;

@Stateless
@Named("missingsEJB")
public class MissingsEJB {

	@PersistenceContext(unitName = "weducationPU")
	private EntityManager em;
	@EJB
	private StudycardsDAO cards;

	public Missing get(final StudyCard card, final int year, final int month, final int week) {
		try {
			TypedQuery<Missing> q = em.createQuery("SELECT m FROM Missing m WHERE (m.card = :c) AND (m.year = :y) AND (m.month = :m) AND (m.week = :w)", Missing.class);
			q.setParameter("c", card);
			q.setParameter("y", year);
			q.setParameter("m", month);
			q.setParameter("w", week);
			return q.getSingleResult();
		} catch (NoResultException e) {
			// Создадим новый объект
			Missing missing = new Missing();
			missing.setCard(card);
			missing.setPerson(card.getPerson());
			missing.setMonth(month);
			missing.setYear(year);
			missing.setWeek(week);
			return missing;
		}
		// В остальных случаях - дальше разберемся.
	}

	public Missing get(final StudyCard card, final int year, final int month) {
		Missing result = new Missing();
		try {
			TypedQuery<Missing> q = em.createQuery("SELECT m FROM Missing m WHERE (m.card = :c) AND (m.year = :y) AND (m.month = :m)", Missing.class);
			q.setParameter("c", card);
			q.setParameter("y", year);
			q.setParameter("m", month);
			result.setCard(card);
			result.setPerson(card.getPerson());
			result.setYear(year);
			result.setMonth(month);
			int legal = 0;
			int illegal = 0;
			for (Missing m : q.getResultList()) {
				legal += m.getLegal();
				illegal += m.getIllegal();
			}
			result.setLegal(legal);
			result.setIllegal(illegal);
			return result;
		} catch (NoResultException e) {
			// Возвратим всё по нулям
			return result;
		}
		// В остальных случаях - дальше разберемся.
	}

	public List<Missing> fetchAll(final StudyGroup group, final int year, final int month, final int week) {
		List<Missing> result = new LinkedList<>();
		for (StudyCard sc : cards.findByGroup(group)) {
			result.add(get(sc, year, month, week));
		}
		return result;
	}

	public Missing save(Missing item) {
		if (0 == item.getId()) {
			em.persist(item);
			return item;
		} else {
			return em.merge(item);
		}
	}

	public void delete(final Missing item) {
		Missing m = em.find(Missing.class, item.getId());
		if (null != m) {
			em.remove(m);
		}
	}
}
