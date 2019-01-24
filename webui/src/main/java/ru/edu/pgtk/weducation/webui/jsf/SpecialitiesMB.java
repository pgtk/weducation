package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.SpecialitiesDAO;
import ru.edu.pgtk.weducation.core.entity.Speciality;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("specialitiesMB")
@ViewScoped
public class SpecialitiesMB extends GenericBean<Speciality> implements Serializable {

    long serialVersionUID = 0L;
    private List<Speciality> specialityList;
    @EJB
    private transient SpecialitiesDAO ejb;

    @PostConstruct
    private void updateList() {
        specialityList = ejb != null ? ejb.fetchAll() : null;
    }

    public List<Speciality> getSpecialityList() {
        return specialityList;
    }

    public boolean isEmptyList() {
        return specialityList == null || specialityList.isEmpty();
    }

    @Override
    public void newItem() {
        item = new Speciality();
    }

    @Override
    public void deleteItem() {
        if (delete && (null != item)) {
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
