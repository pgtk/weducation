package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.Department;
import ru.edu.pgtk.weducation.data.entity.Speciality;
import ru.edu.pgtk.weducation.data.entity.StudyPlan;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * EJB для организации функционала для учебных планов.
 * @author Воронин Леонид
 */
@Stateless
@Named("studyPlansEJB")
public class StudyPlansEJB extends AbstractEJB implements StudyPlansDAO {

	@EJB
	private SpecialitiesDAO specialities;

	@Override
	public StudyPlan get(final int id) {
		StudyPlan result = em.find(StudyPlan.class, id);
		if (null != result) {
			return result;
		}
		throw new EJBException("StudyPlan not found with id " + id);
	}

	@Override
	public List<StudyPlan> fetchAll() {
		TypedQuery<StudyPlan> q = em.createQuery("SELECT sp FROM StudyPlan sp ORDER BY sp.speciality.name, sp.beginYear DESC", StudyPlan.class);
		return q.getResultList();
	}

	@Override
	public List<StudyPlan> findBySpeciality(final Speciality spc) {
		TypedQuery<StudyPlan> q = em.createQuery(
				"SELECT sp FROM StudyPlan sp WHERE (sp.speciality = :spec) ORDER BY sp.beginYear DESC", StudyPlan.class);
		q.setParameter("spec", spc);
		return q.getResultList();
	}

	@Override
	public List<StudyPlan> findBySpeciality(final Speciality spc, final boolean extramural) {
		TypedQuery<StudyPlan> q = em.createQuery(
				"SELECT sp FROM StudyPlan sp WHERE (sp.speciality = :spec) AND (sp.extramural = :em) ORDER BY sp.beginYear DESC", StudyPlan.class);
		q.setParameter("spec", spc);
		q.setParameter("em", extramural);
		return q.getResultList();
	}

	@Override
	public List<StudyPlan> findByDepartment(final Department dep) {
		TypedQuery<StudyPlan> q = em.createQuery(
				"SELECT sp FROM StudyPlan sp "
				+ "WHERE (sp.speciality.id IN (SELECT dp.speciality.id FROM DepartmentProfile dp WHERE (dp.department = :dep)))"
				+ " AND (sp.extramural IN (SELECT dp.extramural FROM DepartmentProfile dp WHERE (dp.department = :dep))) "
				+ "ORDER BY sp.speciality.name, sp.beginYear DESC", StudyPlan.class);
		q.setParameter("dep", dep);
		return q.getResultList();
	}

	@Override
	public List<StudyPlan> findLike(final StudyPlan plan) {
		try {
			TypedQuery<StudyPlan> q = em.createQuery(
					"SELECT sp FROM StudyPlan sp WHERE (sp.beginYear = :by) AND (sp.specialityName LIKE :sn) "
					+ "AND (sp.specialityKey = :sk) AND (sp.kvalification = :kv) AND (sp.extramural = :em)", StudyPlan.class);
			q.setParameter("by", plan.getBeginYear());
			q.setParameter("sn", plan.getSpecialityName());
			q.setParameter("sk", plan.getSpecialityKey());
			q.setParameter("kv", plan.getKvalification());
			q.setParameter("em", plan.getExtramural());
			return q.getResultList();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@Override
	public StudyPlan save(StudyPlan item) {
		if (null == item) {
			throw new IllegalArgumentException("StudyPlan must be not null!");
		}
		item.setSpeciality(specialities.get(item.getSpecialityCode()));
		if (item.getId() == 0) {
			em.persist(item);
			return item;
		} else {
			return em.merge(item);
		}
	}

	@Override
	public void delete(final StudyPlan item) {
		StudyPlan sp = em.find(StudyPlan.class, item.getId());
		if (null != sp) {
			em.remove(sp);
		}
	}
}
