package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.DepartmentsEJB;
import ru.edu.pgtk.weducation.core.entity.Department;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@Named("departmentsMB")
@ViewScoped
public class DepartmentsMB extends GenericBean<Department> implements Serializable {

	long serialVersionUID = 0L;
	private List<Department> list;

	@EJB
	private transient DepartmentsEJB ejb;
	private int departmentCode;

	@PostConstruct
	private void updateList() {
		list = ejb != null ? ejb.fetchAll() : null;
	}

	public boolean isEmptyList() {
		return list == null || list.isEmpty();
	}

	public List<Department> getList() {
		return list;
	}

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
			updateList();
		}
	}

	@Override
	public void saveItem() {
		ejb.save(item);
		updateList();
	}
}
