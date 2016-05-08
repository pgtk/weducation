package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.AccountRole;
import ru.edu.pgtk.weducation.core.entity.Department;
import ru.edu.pgtk.weducation.core.interceptors.Restricted;
import ru.edu.pgtk.weducation.core.interceptors.WithLog;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Корпоративный бин для отделений
 *
 * @author Воронин Леонид
 */
@Stateless
@Named("departmentsEJB")
public class DepartmentsEJB {

  @PersistenceContext(unitName = "weducationPU")
  EntityManager em;

  public Department get(final int id) {
    Department item = em.find(Department.class, id);
    if (null != item) {
      return item;
    }
    throw new EJBException("Department not found with id " + id);
  }

  public List<Department> fetchAll() {
    TypedQuery<Department> query = em.createQuery("SELECT d FROM Department d ORDER BY d.name", Department.class);
    return query.getResultList();
  }

  @WithLog
  @Restricted(allowedRoles = {AccountRole.DEPARTMENT})
  public void save(Department department) {
    if (department.getId() == 0) {
      em.persist(department);
    } else {
      em.merge(department);
    }
  }

  @WithLog
  @Restricted(allowedRoles = {AccountRole.DEPARTMENT})
  public void delete(final Department department) {
    Department item = em.find(Department.class, department.getId());
    if (null != item) {
      em.remove(item);
    }
  }
}
