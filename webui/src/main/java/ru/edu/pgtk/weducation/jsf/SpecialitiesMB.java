package ru.edu.pgtk.weducation.jsf;

import ru.edu.pgtk.weducation.ejb.SpecialitiesDAO;
import ru.edu.pgtk.weducation.entity.Speciality;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("specialitiesMB")
@ViewScoped
public class SpecialitiesMB extends GenericBean<Speciality> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private transient SpecialitiesDAO ejb;

	@Override
	public void newItem() {
		item = new Speciality();
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
