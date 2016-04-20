package ru.edu.pgtk.weducation.jsf;

import ru.edu.pgtk.weducation.data.entity.Department;
import ru.edu.pgtk.weducation.ejb.DepartmentsEJB;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@Named("departmentsMB")
@ViewScoped
public class DepartmentsMB extends GenericBean<Department> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private transient DepartmentsEJB ejb;
	private int departmentCode;

	public void loadDepartment() {
		try {
			// Получим код отделения из параметра, если есть
			if (user.isDepartment() && (user.getCode() > 0)) {
				departmentCode = user.getCode();
			}
			if (departmentCode > 0) {
				item = ejb.get(departmentCode);
				details = true;
			}
		} catch (Exception e) {
			addMessage(e);
			departmentCode = 0;
			resetState();
		}
	}

	public int getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(int departmentCode) {
		this.departmentCode = departmentCode;
	}

	@Override
	public void newItem() {
		item = new Department();
	}

	@Override
	public void deleteItem() {
		if (delete && (item != null)) {
			ejb.delete(item);
		}
	}

	@Override
	public void saveItem() {
		ejb.save(item);
	}
}
