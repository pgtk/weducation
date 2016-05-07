package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.Seat;
import ru.edu.pgtk.weducation.data.entity.Speciality;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
@RequestScoped
public class SeatsEJB {

	@PersistenceContext(unitName = "weducationPU")
	private EntityManager em;

	@EJB
	private SpecialitiesDAO specialities;

	public Seat get(final int id) {
		Seat result = em.find(Seat.class, id);
		if (result != null) {
			return result;
		} else {
			throw new EJBException("Seat not found with id " + id);
		}
	}

	public Seat get(final int year, final Speciality speciality, final boolean extramural) {
		try {
			TypedQuery<Seat> q = em.createQuery(
					"SELECT s FROM Seat s WHERE (s.speciality = :spc) AND (s.year = :year) AND (s.extramural = :ext)", Seat.class);
			q.setParameter("spc", speciality);
			q.setParameter("year", year);
			q.setParameter("ext", extramural);
			return q.getSingleResult();
		} catch (NoResultException e) {
			// Создадим новый объект
			Seat s = new Seat();
			s.setSpeciality(speciality);
			s.setYear(year);
			s.setExtramural(extramural);
			return s;
		} catch (NonUniqueResultException e) {
			throw new EJBException("Request for seat returned more than one record!");
		}
	}

	public List<Seat> fetch(final int year, final boolean extramural) {
		try {
			List<Seat> result = new ArrayList<>();
			for (Speciality spc : specialities.fetchActual(extramural)) {
				Seat s = get(year, spc, extramural);
				result.add(s);
			}
			return result;
		} catch (NoResultException | NonUniqueResultException e) {
			throw new EJBException("Error fetching list of Seat!");
		}
	}

	public Seat save(Seat item) {
		if (item.getId() == 0) {
			em.persist(item);
			return item;
		} else {
			return em.merge(item);
		}
	}

	public void delete(final Seat item) {
		Seat s = em.find(Seat.class, item.getId());
		if (null != s) {
			em.remove(s);
		}
	}
}
