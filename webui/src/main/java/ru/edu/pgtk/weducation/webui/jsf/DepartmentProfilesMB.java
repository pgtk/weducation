package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.DepartmentProfilesDAO;
import ru.edu.pgtk.weducation.core.ejb.DepartmentsDAO;
import ru.edu.pgtk.weducation.core.ejb.SpecialitiesDAO;
import ru.edu.pgtk.weducation.core.entity.Department;
import ru.edu.pgtk.weducation.core.entity.DepartmentProfile;
import ru.edu.pgtk.weducation.core.entity.Speciality;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("departmentProfilesMB")
@ViewScoped
public class DepartmentProfilesMB extends GenericBean<DepartmentProfile> implements Serializable {

    long serialVersionUID = 0L;
    private List<DepartmentProfile> list;

    @EJB
    private DepartmentProfilesDAO ejb;
    @EJB
    private DepartmentsDAO departmentsDao;
    @EJB
    private SpecialitiesDAO specialitiesDao;

    @PostConstruct
    private void updateList() {
        list = ejb != null ? ejb.fetchAll() : null;
    }

    public List<DepartmentProfile> getList() {
        return list;
    }

    public List<Department> getDepartmentsList() {
        return departmentsDao != null ? departmentsDao.fetchAll() : Collections.EMPTY_LIST;
    }

    public List<Speciality> getSpecialitiesList() {
        return specialitiesDao != null ? specialitiesDao.fetchAll() : Collections.EMPTY_LIST;
    }

    public boolean isEmptyList() {
        return list == null || list.isEmpty();
    }

    @Override
    public void newItem() {
        item = new DepartmentProfile();
    }

    @Override
    public void deleteItem() {
        if ((null != item) && delete) {
            ejb.delete(item);
            updateList();
        }
    }

    @Override
    public void saveItem() {
        ejb.save(item);
        updateList();
    }
}
