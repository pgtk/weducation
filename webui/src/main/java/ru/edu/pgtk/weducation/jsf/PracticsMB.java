package ru.edu.pgtk.weducation.jsf;

import ru.edu.pgtk.weducation.ejb.PracticsEJB;
import ru.edu.pgtk.weducation.ejb.StudyModulesEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansDAO;
import ru.edu.pgtk.weducation.entity.Practic;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@Named("practicsMB")
@ViewScoped
public class PracticsMB extends GenericBean<Practic> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private PracticsEJB ejb;
	@EJB
	private StudyModulesEJB mejb;
	@EJB
	private StudyPlansDAO planEJB;
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
				plan = planEJB.get(planCode);
			}
		} catch (Exception e) {
			addMessage(e);
		}
	}

	public List<Practic> getPractics() {
		return ejb.findByPlan(plan);
	}

	public List<StudyModule> getStudyModules() {
		return mejb.fetchAll(plan);
	}

	@Override
	public void newItem() {
		item = new Practic();
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
