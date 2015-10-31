package ru.edu.pgtk.weducation.jsf;

import ru.edu.pgtk.weducation.ejb.StudyModulesEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.entity.Subject;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@Named("subjectsMB")
@ViewScoped
public class SubjectsMB extends GenericBean<Subject> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private transient SubjectsEJB ejb;
	@EJB
	private transient StudyModulesEJB mejb;
	@EJB
	private transient StudyPlansEJB plans;

	private StudyPlan plan = null;
	private int planCode;

	public int getPlanCode() {
		return planCode;
	}

	public void setPlanCode(int planCode) {
		this.planCode = planCode;
	}

	public StudyPlan getPlan() {
		return plan;
	}

	public void setPlan(StudyPlan plan) {
		this.plan = plan;
	}

	public void loadPlan() {
		try {
			if (planCode > 0) {
				plan = plans.get(planCode);
			}
		} catch (Exception e) {
			addMessage(e);
		}
	}

	public List<Subject> getSubjects() {
		return ejb.fetchAll(plan);
	}

	public List<StudyModule> getStudyModules() {
		return mejb.fetchAll(plan);
	}

	@Override
	public void newItem() {
		item = new Subject();
		item.setPlan(plan);
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
