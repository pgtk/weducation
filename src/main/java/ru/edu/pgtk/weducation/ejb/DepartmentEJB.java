package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Department;

/**
 * Корпоративный бин для отделений
 *
 * @author Воронин Леонид
 */
@Stateless
public class DepartmentEJB {

  @PersistenceContext(unitName = "weducationPU")
  EntityManager em;

  public Department get(final int id) throws DataException {
    Department item = em.find(Department.class, id);
    if (null != item) {
      return item;
    }
    throw new DataException("Department not found with id " + id);
  }

  public List<Department> fetchAll() {
    TypedQuery<Department> query = em.createQuery("SELECT d FROM Department d", Department.class);
    return query.getResultList();
  }

  public void save(Department department) {
    if (department.getId() == 0) {
      em.persist(department);
    } else {
      em.merge(department);
    }
  }

  public void delete(final Department department) {
    Department item = em.find(Department.class, department.getId());
    if (null != item) {
      em.remove(item);
    }
  }
}
