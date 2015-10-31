package ru.edu.pgtk.weducation.jsf;

import ru.edu.pgtk.weducation.ejb.DepartmentProfilesEJB;
import ru.edu.pgtk.weducation.entity.DepartmentProfile;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("departmentProfilesMB")
@ViewScoped
public class DepartmentProfilesMB extends GenericBean<DepartmentProfile> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private DepartmentProfilesEJB ejb;

	@Override
	public void newItem() {
		item = new DepartmentProfile();
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
