package ru.edu.pgtk.weducation.webtest.jsf;

import ru.edu.pgtk.weducation.core.ejb.SpecialitiesDAO;
import ru.edu.pgtk.weducation.core.ejb.StudycardsDAO;
import ru.edu.pgtk.weducation.core.entity.Speciality;
import ru.edu.pgtk.weducation.core.entity.StudyCard;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Сессионный компонент для веб-приложения
 * @author Voronin Leonid
 * @since 09.05.2016
 */
@Named("sessionBean")
@SessionScoped
public class SessionBean implements Serializable {

	private StudyCard studentCard = null;
	private Speciality speciality = null;
	private int specialityCode;
	private String biletNumber;
	private boolean extramural = false;
	private boolean autorized = false;

	@EJB
	private StudycardsDAO cardsDao;
	@EJB
	private SpecialitiesDAO specialitiesDao;

	private void resetData() {
		studentCard = null;
		speciality = null;
		autorized = false;
	}

	private void addErrorMessage(Exception e) {
		Throwable t = e;
		String message = t.getMessage();
		if (message == null || message.isEmpty()) {
			if (t.getCause() != null) {
				t = t.getCause();
				message = t.getMessage();
			}
		}
		if (message == null || message.isEmpty()) {
			message = "Исключение " + t.getClass().getName();
		}
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, t.getClass().getName());
		FacesContext.getCurrentInstance().addMessage(t.getClass().getName(), msg);
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
					autorized = true;
				}
			}
		} catch (Exception e) {
			resetData();
			addErrorMessage(e);
		}
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

//	public void changeSpeciality(ValueChangeEvent event) {
//		try {
//			specialityCode = (Integer) event.getNewValue();
//			if (specialityCode > 0) {
//				speciality = specialitiesDao.get(specialityCode);
//			} else {
//				speciality = null;
//			}
//		} catch (Exception e) {
//			resetData();
//			addErrorMessage(e);
//		}
//	}

	public StudyCard getStudentCard() {
		return studentCard;
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

	public boolean isAutorized() {
		return autorized;
	}

	public Speciality getSpeciality() {
		return speciality;
	}
}
