package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.StudyModulesDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyPlansDAO;
import ru.edu.pgtk.weducation.core.entity.ExamForm;
import ru.edu.pgtk.weducation.core.entity.StudyModule;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@Named("studyModulesMB")
@ViewScoped
public class StudyModulesMB extends GenericBean<StudyModule> implements Serializable {

    long serialVersionUID = 0L;

    @EJB
    private transient StudyModulesDAO ejb;
    @EJB
    private transient StudyPlansDAO planEJB;

    private StudyPlan plan = null;
    private int planCode;

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

    public List<StudyModule> getStudyModules() {
        return ejb.fetchAll(plan);
    }

    public ExamForm[] getExamForms() {
        return ExamForm.values();
    }

    @Override
    public void newItem() {
        item = new StudyModule();
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
