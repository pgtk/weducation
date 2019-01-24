package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.DepartmentsDAO;
import ru.edu.pgtk.weducation.core.ejb.ServicesDAO;
import ru.edu.pgtk.weducation.core.ejb.SpecialitiesDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyPlansDAO;
import ru.edu.pgtk.weducation.core.entity.Department;
import ru.edu.pgtk.weducation.core.entity.Speciality;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@Named("studyPlansMB")
@ViewScoped
public class StudyPlansMB extends GenericBean<StudyPlan> implements Serializable {

    long serialVersionUID = 0L;

    @EJB
    private transient StudyPlansDAO ejb;
    @EJB
    private transient DepartmentsDAO departments;
    @EJB
    private transient SpecialitiesDAO specialities;
    @EJB
    private transient ServicesDAO services;
    private int planCode;
    private List<StudyPlan> plansList;
    private List<Speciality> specialitiesList;
    private Department dep;
    private boolean copy; // Признак того, что идет операция копирования.
    private StudyPlan source; // Источник учебного плана для копирования.

    @PostConstruct
    private void loadDepartment() {
        try {
            if ((user != null) && user.isDepartment()) {
                dep = departments.get(user.getCode());
            } else {
                dep = null;
            }
        } catch (Exception e) {
            dep = null;
        }
    }

    private void prepareList() {
        if (dep != null) {
            plansList = ejb.findByDepartment(dep);
            specialitiesList = specialities.fetchAll(dep);
        } else {
            plansList = ejb.fetchAll();
            specialitiesList = specialities.fetchAll();
        }
    }

    public int getPlanCode() {
        return planCode;
    }

    public void copy(final StudyPlan source) {
        // Сохраним источник
        this.source = source;
        // Создадим новый учебный план с параметрами исходного
        item = new StudyPlan(source);
        // Включим режим редактирования
        edit = true;
    }

    public void setPlanCode(int planCode) {
        this.planCode = planCode;
    }

    public void loadPlan() {
        try {
            if (planCode > 0) {
                item = ejb.get(planCode);
                details = true;
            }
        } catch (Exception e) {
            addMessage(e);
        }
    }

    public List<StudyPlan> getPlans() {
        if (plansList == null) {
            prepareList();
        }
        return plansList;
    }

    public List<Speciality> getSpecialities() {
        if (specialitiesList == null) {
            prepareList();
        }
        return specialitiesList;
    }

    @Override
    public void newItem() {
        item = new StudyPlan();
        Calendar cal = new GregorianCalendar();
        item.setBeginYear(cal.get(Calendar.YEAR));
        item.setYears(3);
        item.setMonths(10);
    }

    @Override
    public void deleteItem() {
        if (delete && (null != item)) {
            ejb.delete(item);
            prepareList();
        }
    }

    @Override
    public void saveItem() {
        ejb.save(item);
        // Если это операция копирования и есть источник, то скопируем дисциплины и т.п.
        if (copy && (null != source)) {
            services.copyPlan(source, item);
        }
        source = null;
        copy = false;
        prepareList();
    }

    public boolean isCopy() {
        return copy;
    }
}
