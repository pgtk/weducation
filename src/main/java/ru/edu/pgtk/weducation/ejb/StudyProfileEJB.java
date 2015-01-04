package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyProfile;

/**
 * Корпоративный бин для профилей обучения
 *
 * @author Воронин Леонид
 */
@Stateless
public class StudyProfileEJB {

  @PersistenceContext
  EntityManager em;

  public StudyProfile get(final int id) {
    StudyProfile item = em.find(StudyProfile.class, id);
    if (null != item) {
      return item;
    }
    throw new EJBException("StudyProfile not found with id " + id);
  }

  public List<StudyProfile> fetchAll() {
    TypedQuery<StudyProfile> query = em.createQuery("SELECT sp FROM StudyProfile sp", StudyProfile.class);
    return query.getResultList();
  }

  public List<StudyProfile> findByDepartment(final Department dep) {
    TypedQuery<StudyProfile> q = em.createQuery(
        "SELECT sp FROM StudyProfile sp WHERE (sp.department = :department)", StudyProfile.class);
    q.setParameter("department", dep);
    return q.getResultList();
  }

  public List<StudyProfile> findBySpeciality(final Speciality spc) {
    TypedQuery<StudyProfile> q = em.createQuery(
        "SELECT sp FROM StudyProfile sp WHERE (sp.speciality = :speciality)", StudyProfile.class);
    q.setParameter("speciality", spc);
    return q.getResultList();
  }

  public List<StudyProfile> findByExtramural(final boolean extramural) {
    TypedQuery<StudyProfile> q = em.createQuery(
        "SELECT sp FROM StudyProfile sp WHERE (sp.extramural = :em)", StudyProfile.class);
    q.setParameter("em", extramural);
    return q.getResultList();
  }

  public void save(StudyProfile profile) {
    if (profile.getDepartmentCode() > 0) {
      Department dep = em.find(Department.class, profile.getDepartmentCode());
      if (null != dep) {
        profile.setDepartment(dep);
      } else {
        throw new EJBException("Wrong department code: " + profile.getDepartmentCode());
      }
    }
    if (profile.getSpecialityCode() > 0) {
      Speciality spc = em.find(Speciality.class, profile.getSpecialityCode());
      if (null != spc) {
        profile.setSpeciality(spc);
      } else {
        throw new EJBException("Wrong speciality code: " + profile.getSpecialityCode());
      }
    }
    if (profile.getId() == 0) {
      em.persist(profile);
    } else {
      em.merge(profile);
    }
  }

  public void delete(final StudyProfile profile) {
    StudyProfile item = em.find(StudyProfile.class, profile.getId());
    if (null != item) {
      em.remove(item);
    }
  }
}
