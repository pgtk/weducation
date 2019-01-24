package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.*;
import ru.edu.pgtk.weducation.core.entity.*;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@Named("studyCardsMB")
@ViewScoped
public class StudyCardsMB extends GenericBean<StudyCard> implements Serializable {

    long serialVersionUID = 0L;

    @EJB
    private transient StudyCardsDAO ejb;
    @EJB
    private transient PersonsDAO personEJB;
    @EJB
    private transient StudyGroupsDAO groupsEJB;
    @EJB
    private transient StudyPlansDAO plansEJB;
    @EJB
    private transient SpecialitiesDAO specialitiesDAO;
    @EJB
    private transient SchoolsDAO schoolsDao;
    @EJB
    private transient FinalMarksDAO finalMarksDao;

    private Person person;
    private Speciality speciality;
    private int personCode;
    private int cardCode;

    public List<School> getSchoolsList() {
        return schoolsDao != null ? schoolsDao.fetchAll() : Collections.EMPTY_LIST;
    }

    public String getAverageBall() {
        float ball = finalMarksDao != null ? finalMarksDao.getAverageMark(item) : 0f;
        return String.format("%01.02f", ball);
    }

    // TODO Убрать хардкордные ссылки на ведомости и заменить их вызовами методов этого класса

    public int getPersonCode() {
        return personCode;
    }

    public void setPersonCode(int personCode) {
        this.personCode = personCode;
    }

    public int getCardCode() {
        return cardCode;
    }

    public void setCardCode(int cardCode) {
        this.cardCode = cardCode;
    }

    public Person getPerson() {
        return person;
    }

    public boolean isViewActive() {
        return null != item && (null == item.getGroup() || item.getGroup().isActive());
    }

    public void preparePage() {
        try {
            if (personCode > 0) {
                // Список личных карточек персоны
                person = personEJB.get(personCode);
            } else if (cardCode > 0) {
                // Детали личной карточки определенной персоны
                item = ejb.get(cardCode);
                person = item.getPerson();
                details = true;
            } else {
                person = null;
                error = true;
            }
        } catch (Exception e) {
            person = null;
            addMessage(e);
            error = true;
        }
    }

    public void changeGroup(ValueChangeEvent event) {
        try {
            int code = (Integer) event.getNewValue();
            if (code > 0) {
                // Получаем группу и устанавдиваем другие данные из неё
                StudyGroup group = groupsEJB.get(code);
                item.setGroup(group);
                item.setSpeciality(group.getSpeciality());
                item.setPlan(group.getPlan());
                item.setExtramural(group.isExtramural());
                item.setActive(group.isActive());
            } else {
                item.setGroup(null);
            }
        } catch (Exception e) {
            item.setGroup(null);
            addMessage(e);
        }
    }

    public void changeSpeciality(ValueChangeEvent event) {
        try {
            int code = (Integer) event.getNewValue();
            if (code > 0) {
                speciality = specialitiesDAO.get(code);
                item.setSpeciality(speciality);
            } else {
                speciality = null;
            }
        } catch (Exception e) {
            speciality = null;
            addMessage(e);
        }
    }

    public List<Speciality> getSpecialities() {
        if (item.isActive()) {
            return specialitiesDAO.fetchActual();
        } else {
            return specialitiesDAO.fetchAll();
        }
    }

    public List<StudyCard> getStudyCards() {
        if (null != person) {
            return ejb.findByPerson(person);
        } else {
            return new ArrayList<>();
        }
    }

    public List<StudyGroup> getStudyGroups() {
        return groupsEJB.fetchActual();
    }

    public List<StudyPlan> getStudyPlans() {
        if (null != speciality) {
            return plansEJB.findBySpeciality(speciality, item.isExtramural());
        } else {
            List<StudyPlan> result = new ArrayList<>();
            if (null != item.getPlan()) {
                result.add(item.getPlan());
            }
            return result;
        }
    }

    @Override
    public void newItem() {
        if (null != person) {
            item = new StudyCard();
            item.setPerson(person);
        }
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
        details = true;
    }
}
