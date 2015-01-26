package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.entity.Speciality;

/**
 * Корпоративный бин для специальностей
 *
 * @author Воронин Леонид
 */
@Stateless
@Named("specialitiesEJB")
public class SpecialitiesEJB {

  @PersistenceContext(unitName = "weducationPU")
  EntityManager em;

  public Speciality get(final int id) {
    Speciality item = em.find(Speciality.class, id);
    if (null != item) {
      return item;
    }
    throw new EJBException("Speciality not found with id " + id);
  }

  public List<Speciality> fetchAll() {
    TypedQuery<Speciality> query = em.createQuery("SELECT s FROM Speciality s ORDER BY s.key, s.fullName", Speciality.class);
    return query.getResultList();
  }

  public List<Speciality> findByDepartment(final Department department) {
    TypedQuery<Speciality> query = em.createQuery(
            "SELECT dp.speciality FROM DepartmentProfile dp WHERE (dp.department = :dep) "
            + "ORDER BY dp.speciality.key, dp.speciality.fullName", Speciality.class);
    query.setParameter("dep", department);
    return query.getResultList();
  }

  public void save(Speciality speciality) {
    if (speciality.getId() == 0) {
      em.persist(speciality);
    } else {
      em.merge(speciality);
    }
  }

  public void delete(final Speciality speciality) {
    Speciality item = em.find(Speciality.class, speciality.getId());
    if (null != item) {
      em.remove(item);
    }
  }
}
