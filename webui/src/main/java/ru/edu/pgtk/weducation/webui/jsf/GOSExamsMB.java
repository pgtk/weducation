package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.GOSExamsDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyPlansDAO;
import ru.edu.pgtk.weducation.core.ejb.SubjectsDAO;
import ru.edu.pgtk.weducation.core.entity.GOSExam;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;
import ru.edu.pgtk.weducation.core.entity.Subject;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@Named("gosexamsMB")
@ViewScoped
public class GOSExamsMB extends GenericBean<GOSExam> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private transient GOSExamsDAO ejb;
	@EJB
	private transient StudyPlansDAO planEJB;
	@EJB
	private transient SubjectsDAO subjectEJB;
	private StudyPlan plan = null;
	private int planCode;

	public List<GOSExam> getGosexams() {
		if (null != plan) {
			return ejb.fetchAll(plan);
		}
		return new ArrayList<>();
	}

	public List<Subject> getSubjects() {
		if (null != plan) {
			return subjectEJB.fetchAll(plan);
		}
		return new ArrayList<>();
	}

	public StudyPlan getPlan() {
		return plan;
	}

	public int getPlanCode() {
		return planCode;
	}

	public void setPlanCode(int planCode) {
		this.planCode = planCode;
	}

	public void loadPlan() {
		try {
			if (planCode > 0) {
				plan = planEJB.get(planCode);
			} else {
				addMessage("Wrond StudyPlan identifier " + planCode);
			}
		} catch (Exception e) {
			addMessage(e);
		}
	}

	@Override
	public void newItem() {
		item = new GOSExam();
		item.setPlan(plan);
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
