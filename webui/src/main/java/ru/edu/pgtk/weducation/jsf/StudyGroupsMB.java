package ru.edu.pgtk.weducation.jsf;

import ru.edu.pgtk.weducation.data.entity.Department;
import ru.edu.pgtk.weducation.data.entity.Speciality;
import ru.edu.pgtk.weducation.data.entity.StudyGroup;
import ru.edu.pgtk.weducation.data.entity.StudyPlan;
import ru.edu.pgtk.weducation.ejb.DepartmentsEJB;
import ru.edu.pgtk.weducation.ejb.SpecialitiesDAO;
import ru.edu.pgtk.weducation.ejb.StudyGroupsDAO;
import ru.edu.pgtk.weducation.ejb.StudyPlansDAO;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@Named("studyGroupsMB")
@ViewScoped
public class StudyGroupsMB extends GenericBean<StudyGroup> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private transient StudyGroupsDAO ejb;
	@EJB
	private transient DepartmentsEJB depejb;
	@EJB
	private transient StudyPlansDAO plansEJB;
	@EJB
	private transient SpecialitiesDAO spcejb;
	private Department department;
	private Speciality speciality;
	private int departmentCode;
	private int groupCode;

	public int getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(int departmentCode) {
		this.departmentCode = departmentCode;
	}

	public Department getDepartment() {
		return department;
	}

	public void preparePage() {
		// Получим код отделения из параметра, если есть
		if (user.isDepartment() && (user.getCode() > 0)) {
			departmentCode = user.getCode();
		}
		// Иначе - попробуем выудить из GET параметров
		if (departmentCode > 0) {
			department = depejb.get(departmentCode);
		} else {
			department = null;
		}
		if (groupCode > 0) {
			item = ejb.get(groupCode);
			details = true;
		}
	}

	public void changeSpeciality(ValueChangeEvent event) {
		try {
			int code = (Integer) event.getNewValue();
			if (code > 0) {
				speciality = spcejb.get(code);
			} else {
				speciality = null;
			}
		} catch (Exception e) {
			speciality = null;
			addMessage(e);
		}
	}

	public List<StudyGroup> getStudyGroups() {
		if (null != department) {
			return ejb.findByDepartment(department);
		} else {
			return ejb.fetchAll();
		}
	}

	public List<Speciality> getSpecialities() {
		if (null != department) {
			return spcejb.fetchActual(department);
		} else {
			return spcejb.fetchActual();
		}
	}

	public List<StudyPlan> getStudyPlans() {
		if (null != speciality) {
			return plansEJB.findBySpeciality(speciality, item.isExtramural());
		} else {
			return plansEJB.fetchAll();
		}
	}

	public int getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(int groupCode) {
		this.groupCode = groupCode;
	}

	@Override
	public void newItem() {
		item = new StudyGroup();
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
