package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.DepartmentsDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyCardsDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyGroupsDAO;
import ru.edu.pgtk.weducation.core.entity.Account;
import ru.edu.pgtk.weducation.core.entity.Department;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@Named("departmentRootMB")
@ViewScoped
public class DepartmentRootMB implements Serializable {

    long serialVersionUID = 0L;

    @Inject
    private transient Account account;
    @EJB
    private transient DepartmentsDAO departments;
    @EJB
    private transient StudyGroupsDAO groups;
    @EJB
    private transient StudyCardsDAO cards;
    private transient Department department;
    private boolean edit = false;
    private StudyGroup group;
    private int groupCode;
    private List<StudyGroup> groupList;

    @PostConstruct
    private void initBean() {
        groupList = new ArrayList<>();
        if (null != account) {
            int depCode = account.getCode();
            if (depCode > 0) {
                try {
                    department = departments.get(depCode);
                    groupList = groups.findByDepartment(department);
                } catch (Exception e) {
                    addMessage(e);
                }
            }
        } else {
            addMessage("Ошибка при получении учетной записи!");
        }
    }

    public void changeGroup(ValueChangeEvent event) {
        try {
            int code = (Integer) event.getNewValue();
            if (code > 0) {
                group = groups.get(code);
            } else {
                group = null;
            }
        } catch (Exception e) {
            group = null;
            addMessage(e);
        }
    }

    public void toggleEdit() {
        try {
            if (edit && (null != department)) {
                departments.save(department);
            }
            edit = !edit;
        } catch (Exception e) {
            addMessage(e);
        }
    }

    public List<StudyCard> getStudents() {
        if (null != group) {
            return cards.findByGroup(group);
        }
        return new ArrayList<>();
    }

    public String getEditLabel() {
        return edit ? "Сохранить" : "Редактировать";
    }

    public boolean isValid() {
        return department != null;
    }

    public Account getAccount() {
        return account;
    }

    public boolean isEdit() {
        return edit;
    }

    public Department getDepartment() {
        return department;
    }

    public StudyGroup getGroup() {
        return group;
    }

    public List<StudyGroup> getGroupList() {
        return groupList;
    }

    public int getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(int groupCode) {
        this.groupCode = groupCode;
    }
}
