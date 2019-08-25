package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.GroupSemestersDAO;
import ru.edu.pgtk.weducation.core.ejb.SemesterMarksDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyGroupsDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyModulesDAO;
import ru.edu.pgtk.weducation.core.entity.GroupSemester;
import ru.edu.pgtk.weducation.core.entity.SemesterMark;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.StudyModule;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@ViewScoped
@Named("semesterModuleMarksMB")
public class SemesterModuleMarksMB implements Serializable {

    long serialVersionUID = 0L;

    @EJB
    private transient StudyGroupsDAO groups;
    @EJB
    private transient StudyModulesDAO modules;
    @EJB
    private transient GroupSemestersDAO semesters;
    @EJB
    private transient SemesterMarksDAO marks;
    private int groupCode;
    private StudyGroup group;
    private int moduleCode;
    private StudyModule module;
    private int semesterCode;
    private GroupSemester semester;
    private List<GroupSemester> semesterList;
    private List<StudyModule> moduleList;
    private List<SemesterMark> markList;

    /**
     * Функция для построения списка оценок
     */
    private void makeList() {
        if ((group != null) && (module != null) && (semester != null)) {
            markList = marks.fetchAll(group, module, semester.getCourse(), semester.getSemester());
        } else {
            // Если хоть один из параметров отсутствует - очищаем список
            markList = null;
        }
    }

    public void loadGroup() {
        try {
            if (groupCode > 0) {
                group = groups.get(groupCode);
                // Можно загрузить список групп
                semesterList = semesters.fetchAll(group);
            }
        } catch (Exception e) {
            addMessage(e);
        }
    }

    public void save() {
        try {
            if (markList != null) {
                for (SemesterMark m : markList) {
                    marks.save(m);
                }
            }
        } catch (Exception e) {
            addMessage(e);
        }
    }

    public void changeSemester(ValueChangeEvent event) {
        try {
            int code = (Integer) event.getNewValue();
            if (code > 0) {
                semester = semesters.get(code);
                // Корректируем список дисциплин для этого семестра
                moduleList = modules.fetch(group, semester.getCourse(), semester.getSemester());
                makeList();
            } else {
                semester = null;
            }
        } catch (Exception e) {
            semester = null;
            addMessage(e);
        }
    }

    public void changeModule(ValueChangeEvent event) {
        try {
            int code = (Integer) event.getNewValue();
            if (code > 0) {
                module = modules.get(code);
                makeList();
            } else {
                module = null;
            }
        } catch (Exception e) {
            module = null;
            addMessage(e);
        }
    }

    public List<GroupSemester> getSemesterList() {
        if (semesterList != null) {
            return semesterList;
        } else {
            return new ArrayList<>();
        }
    }

    public List<StudyModule> getModuleList() {
        if (moduleList == null) {
            moduleList = new ArrayList<>();
        }
        return moduleList;
    }

    public List<SemesterMark> getMarkList() {
        if (markList == null) {
            markList = new ArrayList<>();
        }
        return markList;
    }

    public int getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(int groupCode) {
        this.groupCode = groupCode;
    }

    public StudyGroup getGroup() {
        return group;
    }

    public int getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(int semesterCode) {
        this.semesterCode = semesterCode;
    }

    public GroupSemester getSemester() {
        return semester;
    }

    public int getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(int moduleCode) {
        this.moduleCode = moduleCode;
    }

    public StudyModule getModule() {
        return module;
    }
}
