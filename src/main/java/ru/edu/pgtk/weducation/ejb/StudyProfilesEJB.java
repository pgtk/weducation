package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.DepartmentProfile;

/**
 * Корпоративный бин для профилей обучения
 *
 * @author Воронин Леонид
 */
@Stateless
public class StudyProfilesEJB {

  @PersistenceContext
  EntityManager em;

  public DepartmentProfile get(final int id) {
    DepartmentProfile item = em.find(DepartmentProfile.class, id);
    if (null != item) {
      return item;
    }
    throw new EJBException("StudyProfile not found with id " + id);
  }

  public List<DepartmentProfile> fetchAll() {
    TypedQuery<DepartmentProfile> query = em.createQuery("SELECT sp FROM StudyProfile sp", DepartmentProfile.class);
    return query.getResultList();
  }

  public List<DepartmentProfile> findByDepartment(final Department dep) {
    TypedQuery<DepartmentProfile> q = em.createQuery("SELECT sp FROM StudyProfile sp WHERE (sp.department = :department)", DepartmentProfile.class);
    q.setParameter("department", dep);
    return q.getResultList();
  }

  public List<DepartmentProfile> findBySpeciality(final Speciality spc) {
    TypedQuery<DepartmentProfile> q = em.createQuery("SELECT sp FROM StudyProfile sp WHERE (sp.speciality = :speciality)", DepartmentProfile.class);
    q.setParameter("speciality", spc);
    return q.getResultList();
  }

  public List<DepartmentProfile> findByExtramural(final boolean extramural) {
    TypedQuery<DepartmentProfile> q = em.createQuery("SELECT sp FROM StudyProfile sp WHERE (sp.extramural = :em)", DepartmentProfile.class);
    q.setParameter("em", extramural);
    return q.getResultList();
  }

  public void save(DepartmentProfile profile) {
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

  public void delete(final DepartmentProfile profile) {
    DepartmentProfile item = em.find(DepartmentProfile.class, profile.getId());
    if (null != item) {
      em.remove(item);
    }
  }
}
