package ru.edu.pgtk.weducation.webtest.jsf;

import ru.edu.pgtk.weducation.core.ejb.SpecialitiesDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyCardsDAO;
import ru.edu.pgtk.weducation.core.ejb.TestsDAO;
import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.Speciality;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.Test;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Сессионный компонент для веб-приложения
 *
 * @author Voronin Leonid
 * @since 09.05.2016
 */
@Named("sessionBean")
@SessionScoped
public class SessionBean extends AbstractBean implements Serializable {

    private StudyCard studentCard = null;
    private Speciality speciality = null;
    private int specialityCode;
    private String biletNumber;
    private boolean extramural = false;
    private boolean authorized = false;
    private List<Test> testsList;

    @EJB
    private transient StudyCardsDAO cardsDao;
    @EJB
    private transient SpecialitiesDAO specialitiesDao;
    @EJB
    private transient TestsDAO testsDAO;

    private void resetData() {
        testsList = null;
        studentCard = null;
        speciality = null;
        authorized = false;
    }

    public String getCopirightYears() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
        return "1932 - " + sdf.format(new Date());
    }

    public void login() {
        try {
            // Если специальности нет, но можно попробовать найти - делаем.
            if (speciality == null && specialityCode > 0) {
                speciality = specialitiesDao.get(specialityCode);
            }
            if (speciality != null && cardsDao != null) {
                studentCard = cardsDao.get(speciality, extramural, biletNumber);
                if (studentCard != null) {
                    authorized = true;
                    testsList = testsDAO.fetchForSpeciality(speciality);
                }
            }
        } catch (Exception e) {
            resetData();
            addErrorMessage(e);
        }
    }

    public void logout() {
        resetData();
    }

    public List<Speciality> getSpecialitiesList() {
        return specialitiesDao != null ? specialitiesDao.fetchActual(extramural) : Collections.emptyList();
    }

    public void changeForm(ValueChangeEvent event) {
        try {
            extramural = (Boolean) event.getNewValue();
        } catch (Exception e) {
            resetData();
            addErrorMessage(e);
        }
    }

    public Person getPerson() {
        return studentCard == null ? null : studentCard.getPerson();
    }

    public StudyCard getStudentCard() {
        return studentCard;
    }

    public List<Test> getTestsList() {
        return testsList == null ? Collections.EMPTY_LIST : testsList;
    }

    public int getSpecialityCode() {
        return specialityCode;
    }

    public void setSpecialityCode(int specialityCode) {
        this.specialityCode = specialityCode;
    }

    public String getBiletNumber() {
        return biletNumber;
    }

    public void setBiletNumber(String biletNumber) {
        this.biletNumber = biletNumber;
    }

    public boolean isExtramural() {
        return extramural;
    }

    public void setExtramural(boolean extramural) {
        this.extramural = extramural;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public Speciality getSpeciality() {
        return speciality;
    }
}
