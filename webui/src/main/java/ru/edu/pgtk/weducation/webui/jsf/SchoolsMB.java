package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.SchoolsDAO;
import ru.edu.pgtk.weducation.core.entity.School;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("schoolsMB")
@ViewScoped
public class SchoolsMB extends GenericBean<School> implements Serializable {

    long serialVersionUID = 0L;
    private List<School> list;
    @EJB
    private transient SchoolsDAO ejb;

    @PostConstruct
    private void updateList() {
        list = ejb != null ? ejb.fetchAll() : null;
    }

    public boolean isEmptyList() {
        return list == null || list.isEmpty();
    }

    public List<School> getList() {
        return list;
    }

    @Override
    public void newItem() {
        item = new School();
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
