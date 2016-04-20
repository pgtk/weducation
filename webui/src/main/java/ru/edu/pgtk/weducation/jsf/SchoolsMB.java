package ru.edu.pgtk.weducation.jsf;

import ru.edu.pgtk.weducation.data.entity.School;
import ru.edu.pgtk.weducation.ejb.SchoolsDAO;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("schoolsMB")
@ViewScoped
public class SchoolsMB extends GenericBean<School> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private transient SchoolsDAO ejb;

	@Override
	public void newItem() {
		item = new School();
	}

	@Override
	public void deleteItem() {
		if ((null != item) && delete) {
			ejb.delete(item);
		}
	}

	@Override
	public void saveItem() {
		ejb.save(item);
	}
}
