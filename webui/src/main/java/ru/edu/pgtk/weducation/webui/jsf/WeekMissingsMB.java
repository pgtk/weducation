package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.GroupSemestersDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyGroupsDAO;
import ru.edu.pgtk.weducation.core.ejb.WeekMissingsDAO;
import ru.edu.pgtk.weducation.core.entity.GroupSemester;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.WeekMissing;

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
@Named("weekMissingsMB")
public class WeekMissingsMB implements Serializable {

    long serialVersionUID = 0L;

    @EJB
    private transient StudyGroupsDAO groups;
    @EJB
    private transient GroupSemestersDAO semesters;
    @EJB
    private transient WeekMissingsDAO ejb;
    private int groupCode;
    private StudyGroup group;
    private int semesterCode;
    private GroupSemester semester;
    private List<GroupSemester> semesterList;
    private List<WeekMissing> weekMissingList;
    private int missingDate;

    /**
     * Функция для построения списка пропусков
     */
    private void makeList() {
        if ((group != null) && (missingDate > 0)) {
            int year = missingDate / 1000;
            int tail = missingDate % 1000;
            int month = tail / 10;
            int week = tail % 10;
            weekMissingList = ejb.fetchAll(group, year, month, week);
        } else {
            // Если хоть один из параметров отсутствует - очищаем список
            weekMissingList = null;
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
            if (weekMissingList != null) {
                for (WeekMissing m : weekMissingList) {
                    ejb.save(m);
                }
            }
        } catch (Exception e) {
            addMessage(e);
        }
    }

    public Map<String, Integer> getDates() {
        Map<String, Integer> result = new TreeMap<>();
        if (semester != null) {
            int year = semester.getBeginYear();
            int month = semester.getBeginMonth();
            int week = semester.getBeginWeek();
            int date;
            do {
                date = year * 1000 + month * 10 + week;
                result.put(String.format("%4d-%02d, %d-я неделя", year, month, week), date);
                week += 1;
                if (week > 4) {
                    week = 1;
                    month += 1;
                    if (month > 12) {
                        month = 1;
                        year += 1;
                    }
                }
            } while (date <= semester.getEndDate());
        }
        return result;
    }

    public void changeSemester(ValueChangeEvent event) {
        try {
            int code = (Integer) event.getNewValue();
            if (code > 0) {
                semester = semesters.get(code);
                makeList();
            } else {
                semester = null;
            }
        } catch (Exception e) {
            semester = null;
            addMessage(e);
        }
    }

    public void changeDate(ValueChangeEvent event) {
        try {
            int newDate = (Integer) event.getNewValue();
            if (newDate > 0) {
                missingDate = newDate;
                makeList();
            } else {
                missingDate = 0;
            }
        } catch (Exception e) {
            missingDate = 0;
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

    public int getMissingDate() {
        return missingDate;
    }

    public void setMissingDate(int missingDate) {
        this.missingDate = missingDate;
    }

    public List<WeekMissing> getWeekMissingList() {
        return weekMissingList;
    }
}
