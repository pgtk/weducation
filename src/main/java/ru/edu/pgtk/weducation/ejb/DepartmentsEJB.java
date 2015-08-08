package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.AccountRole;
import ru.edu.pgtk.weducation.entity.Department;
import ru.edu.pgtk.weducation.interceptors.Restricted;
import ru.edu.pgtk.weducation.interceptors.WithLog;

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
  @Restricted(roles = {AccountRole.DEPARTMENT})
  public void save(Department department) {
    if (department.getId() == 0) {
      em.persist(department);
    } else {
      em.merge(department);
    }
  }

  @WithLog
  @Restricted(roles = {AccountRole.DEPARTMENT})
  public void delete(final Department department) {
    Department item = em.find(Department.class, department.getId());
    if (null != item) {
      em.remove(item);
    }
  }
}
