package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.PracticsDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyModulesDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyPlansDAO;
import ru.edu.pgtk.weducation.core.entity.Practic;
import ru.edu.pgtk.weducation.core.entity.StudyModule;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@Named("practicsMB")
@ViewScoped
public class PracticsMB extends GenericBean<Practic> implements Serializable {

    long serialVersionUID = 0L;

    @EJB
    private PracticsDAO ejb;
    @EJB
    private StudyModulesDAO mejb;
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
