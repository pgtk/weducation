package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Department;
import ru.edu.pgtk.weducation.core.entity.DepartmentProfile;
import ru.edu.pgtk.weducation.core.entity.Speciality;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("departmentProfilesEJB")
public class DepartmentProfilesEJB extends AbstractEJB implements DepartmentProfilesDAO {

    @Override
    public DepartmentProfile get(final int id) {
        DepartmentProfile result = em.find(DepartmentProfile.class, id);
        if (null != result) {
            return result;
        }
        throw new EJBException("DepartmentProfile not found with id " + id);
    }

    @Override
    public List<DepartmentProfile> fetchAll() {
        TypedQuery<DepartmentProfile> q = em.createQuery(
                "SELECT dp FROM DepartmentProfile dp ORDER BY dp.department.name, dp.speciality.name",
                DepartmentProfile.class);
        return q.getResultList();
    }

    @Override
    public List<DepartmentProfile> findByDepartment(final Department department) {
        TypedQuery<DepartmentProfile> q = em.createQuery(
                "SELECT dp FROM DepartmentProfile dp WHERE (dp.department = :dep) "
                        + "ORDER BY dp.department.name, dp.speciality.name",
                DepartmentProfile.class);
        q.setParameter("dep", department);
        return q.getResultList();
    }

    @Override
    public DepartmentProfile save(DepartmentProfile item) {
        if (item == null) {
            throw new IllegalArgumentException("You can't save NULL DepartmentProfile!");
        }
        if (item.getDepartmentCode() > 0) {
            Department dep = em.find(Department.class, item.getDepartmentCode());
            if (null != dep) {
                item.setDepartment(dep);
            } else {
                throw new EJBException("Department not found with id " + item.getDepartmentCode());
            }
        } else {
            throw new EJBException("Wrong Department code" + item.getDepartmentCode());
        }
        if (item.getSpecialityCode() > 0) {
            Speciality spc = em.find(Speciality.class, item.getSpecialityCode());
            if (null != spc) {
                item.setSpeciality(spc);
            } else {
                throw new EJBException("Speciality not found with id " + item.getSpecialityCode());
            }
        } else {
            throw new EJBException("Wrong Speciality code " + item.getSpecialityCode());
        }
        if (item.getId() == 0) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Override
    public void delete(final DepartmentProfile item) {
        DepartmentProfile dp = em.find(DepartmentProfile.class, item.getId());
        if (null != dp) {
            em.remove(dp);
        }
    }
}
