package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.RenamingsDAO;
import ru.edu.pgtk.weducation.core.entity.Renaming;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Управляемый бин для переименований
 *
 * @author Воронин Леонид
 */
@Named("renamingsMB")
@ViewScoped
public class RenamingsMB extends GenericBean<Renaming> implements Serializable {

    long serialVersionUID = 0L;
    private List<Renaming> list;

    @EJB
    private transient RenamingsDAO ejb;

    @PostConstruct
    private void updateList() {
        list = ejb != null ? ejb.fetchAll() : null;
    }

    public List<Renaming> getList() {
        return list;
    }

    public boolean isEmptyList() {
        return list == null || list.isEmpty();
    }

    @Override
    public void newItem() {
        item = new Renaming();
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
