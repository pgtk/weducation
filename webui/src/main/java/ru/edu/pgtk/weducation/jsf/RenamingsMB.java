package ru.edu.pgtk.weducation.jsf;

import ru.edu.pgtk.weducation.ejb.RenamingsEJB;
import ru.edu.pgtk.weducation.entity.Renaming;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Управляемый бин для переименований
 * @author Воронин Леонид
 */
@Named("renamingsMB")
@ViewScoped
public class RenamingsMB extends GenericBean<Renaming> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private transient RenamingsEJB ejb;

	@Override
	public void newItem() {
		item = new Renaming();
	}

	@Override
	public void deleteItem() {
		if (delete && (null != item)) {
			ejb.delete(item);
		}
	}

	@Override
	public void saveItem() {
		ejb.save(item);
	}
}
