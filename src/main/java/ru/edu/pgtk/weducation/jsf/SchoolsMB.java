package ru.edu.pgtk.weducation.jsf;

import ru.edu.pgtk.weducation.ejb.SchoolsEJB;
import ru.edu.pgtk.weducation.entity.School;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("schoolsMB")
@ViewScoped
public class SchoolsMB extends GenericBean<School> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private transient SchoolsEJB ejb;

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
