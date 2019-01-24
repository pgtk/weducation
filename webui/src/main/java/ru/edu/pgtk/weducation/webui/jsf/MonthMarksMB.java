package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.GroupSemestersDAO;
import ru.edu.pgtk.weducation.core.ejb.MonthMarksDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyGroupsDAO;
import ru.edu.pgtk.weducation.core.ejb.SubjectsDAO;
import ru.edu.pgtk.weducation.core.entity.GroupSemester;
import ru.edu.pgtk.weducation.core.entity.MonthMark;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.Subject;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@ViewScoped
@Named("monthMarksMB")
public class MonthMarksMB implements Serializable {

    long serialVersionUID = 0L;

    @EJB
    private transient StudyGroupsDAO groups;
    @EJB
    private transient SubjectsDAO subjects;
    @EJB
    private transient GroupSemestersDAO semesters;
    @EJB
    private transient MonthMarksDAO marks;
    private int groupCode;
    private StudyGroup group;
    private int subjectCode;
    private Subject subject;
    private int semesterCode;
    private GroupSemester semester;
    private List<GroupSemester> semesterList;
    private List<Subject> subjectList;
    private List<MonthMark> markList;
    private int markDate;

    /**
     * Функция для построения списка оценок
     */
    private void makeList() {
        if ((group != null) && (markDate > 0)) {
            if (subject != null) {
                markList = marks.fetchAll(group, subject, markDate / 100, markDate % 100);
            }
        } else {
            // Если хоть один из параметров отсутствует - очищаем список
            markList = null;
        }
    }

    public boolean isAviableSheet() {
        return (group != null) && (markDate > 0);
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
                for (MonthMark m : markList) {
                    marks.save(m);
                }
            }
        } catch (Exception e) {
            addMessage(e);
        }
    }

    public Map<String, Integer> getMonths() {
        Map<String, Integer> result = new TreeMap<>();
        if (semester != null) {
            int year = semester.getBeginYear();
            int month = semester.getBeginMonth();
            int edate = semester.getEndYear() * 100 + semester.getEndMonth();
            int date;
            do {
                date = year * 100 + month;
                result.put(String.format("%04d-%02d", year, month), date);
                month += 1;
                if (month == 13) {
                    month = 1;
                    year += 1;
                }
            } while (date <= edate);
        }
        return result;
    }

    public void changeSemester(ValueChangeEvent event) {
        try {
            int code = (Integer) event.getNewValue();
            if (code > 0) {
                semester = semesters.get(code);
                // Корректируем список дисциплин для этого семестра
                subjectList = subjects.fetch(group, semester.getCourse(), semester.getSemester());
                makeList();
            } else {
                semester = null;
            }
        } catch (Exception e) {
            semester = null;
            addMessage(e);
        }
    }

    public void changeSubject(ValueChangeEvent event) {
        try {
            int code = (Integer) event.getNewValue();
            if (code > 0) {
                subject = subjects.get(code);
                makeList();
            } else {
                subject = null;
            }
        } catch (Exception e) {
            subject = null;
            addMessage(e);
        }
    }

    public void changeDate(ValueChangeEvent event) {
        try {
            int newDate = (Integer) event.getNewValue();
            if (newDate > 0) {
                markDate = newDate;
                makeList();
            } else {
                markDate = 0;
            }
        } catch (Exception e) {
            markDate = 0;
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

    public List<Subject> getSubjectList() {
        if (subjectList == null) {
            subjectList = new ArrayList<>();
        }
        return subjectList;
    }

    public List<MonthMark> getMarkList() {
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

    public int getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(int subjectCode) {
        this.subjectCode = subjectCode;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getMarkDate() {
        return markDate;
    }

    public void setMarkDate(int markDate) {
        this.markDate = markDate;
    }
}
