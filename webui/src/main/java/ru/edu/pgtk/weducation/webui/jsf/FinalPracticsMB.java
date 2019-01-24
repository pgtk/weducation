package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.FinalPracticsDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyPlansDAO;
import ru.edu.pgtk.weducation.core.entity.FinalPractic;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@Named("finalPracticsMB")
@ViewScoped
public class FinalPracticsMB extends GenericBean<FinalPractic> implements Serializable {

    long serialVersionUID = 0L;

    @EJB
    private FinalPracticsDAO ejb;
    @EJB
    private StudyPlansDAO planEJB;
    private StudyPlan plan = null;
    private int planCode;

    public StudyPlan getPlan() {
        return plan;
    }

    public void setPlan(StudyPlan plan) {
        this.plan = plan;
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

    public List<FinalPractic> getFinalPractics() {
        return ejb.fetchAll(plan);
    }

    @Override
    public void newItem() {
        item = new FinalPractic();
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
