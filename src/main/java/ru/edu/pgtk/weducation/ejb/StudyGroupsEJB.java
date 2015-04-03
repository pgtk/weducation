package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.StudyPlan;

@Stateless
@Named("studyGroupsEJB")
public class StudyGroupsEJB {
  
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  @EJB
  private SpecialitiesEJB specialities;
  @EJB
  private StudyPlansEJB plans;
  
  public StudyGroup get(final int id) {
    StudyGroup result = em.find(StudyGroup.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("StudyGroup not found with id " + id);
  }
  
  public List<StudyGroup> fetchAll() {
    TypedQuery<StudyGroup> q = em.createQuery(
            "SELECT sg FROM StudyGroup sg ORDER BY sg.course, sg.name", StudyGroup.class);
    return q.getResultList();
  }
  
  public List<StudyGroup> findByDepartment(final Department department) {
    TypedQuery<StudyGroup> q = em.createQuery(
            "SELECT sg FROM StudyGroup sg, DepartmentProfile dp "
            + "WHERE (sg.speciality = dp.speciality) AND "
            + "(sg.extramural = dp.extramural) AND (dp.department = :dep) "
            + "ORDER BY sg.course, sg.name", StudyGroup.class);
    q.setParameter("dep", department);
    return q.getResultList();
  }
  
  public List<StudyGroup> findBySpeciality(final Speciality speciality) {
    TypedQuery<StudyGroup> q = em.createQuery(
            "SELECT sg FROM StudyGroup sg WHERE (sg.speciality = :spc) "
            + "ORDER BY sg.course, sg.name", StudyGroup.class);
    q.setParameter("spc", speciality);
    return q.getResultList();
  }
  
  public List<StudyGroup> findBySpeciality(final Speciality speciality, final boolean extramural) {
    TypedQuery<StudyGroup> q = em.createQuery(
            "SELECT sg FROM StudyGroup sg WHERE (sg.speciality = :spc) AND (sg.extramural = :em)"
            + "ORDER BY sg.course, sg.name", StudyGroup.class);
    q.setParameter("spc", speciality);
    q.setParameter("em", extramural);
    return q.getResultList();
  }
  
  public StudyGroup findByName(final String name) {
    try {
      TypedQuery<StudyGroup> q = em.createQuery(
              "SELECT sg FROM StudyGroup sg WHERE (sg.name = :gn)", StudyGroup.class);
      q.setParameter("gn", name);
      return q.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }
  
  public StudyGroup save(StudyGroup item) {
    if (item.getSpecialityCode() > 0) {
      item.setSpeciality(specialities.get(item.getSpecialityCode()));
    }
    if (item.getPlanCode() > 0) {
      item.setPlan(plans.get(item.getPlanCode()));
    }
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }
  
  public void delete(final StudyGroup item) {
    StudyGroup sg = em.find(StudyGroup.class, item.getId());
    if (null != sg) {
      em.remove(sg);
    }
  }
}
