package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.AccountRole;
import ru.edu.pgtk.weducation.core.entity.Department;
import ru.edu.pgtk.weducation.core.interceptors.Restricted;
import ru.edu.pgtk.weducation.core.interceptors.WithLog;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Корпоративный бин для отделений
 * @author Воронин Леонид
 */
@Stateless
@Named("departmentsEJB")
public class DepartmentsEJB extends AbstractEJB implements DepartmentsDAO {

    @Override
    public Department get(final int id) {
        Department item = em.find(Department.class, id);
        if (null != item) {
            return item;
        }
        throw new EJBException("Department not found with id " + id);
    }

    @Override
    public List<Department> fetchAll() {
        TypedQuery<Department> query = em.createQuery("SELECT d FROM Department d ORDER BY d.name", Department.class);
        return query.getResultList();
    }

    @Override
    @WithLog
    @Restricted(allowedRoles = {AccountRole.DEPARTMENT})
    public Department save(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("You can't save NULL Department!");
        }
        if (department.getId() == 0) {
            em.persist(department);
            return department;
        } else {
            return em.merge(department);
        }
    }

    @Override
    @WithLog
    @Restricted(allowedRoles = {AccountRole.DEPARTMENT})
    public void delete(final Department department) {
        Department item = em.find(Department.class, department.getId());
        if (null != item) {
            em.remove(item);
        }
    }
}
