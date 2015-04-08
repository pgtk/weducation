package ru.edu.pgtk.weducation.jsf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import ru.edu.pgtk.weducation.ejb.PersonsEJB;
import ru.edu.pgtk.weducation.ejb.SpecialitiesEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.ejb.StudyGroupsEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.entity.Person;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.reports.DiplomeBlanksEJB;
import ru.edu.pgtk.weducation.reports.ReferenceBlanksEJB;

@ManagedBean(name = "studyCardsMB")
@ViewScoped
public class StudyCardsMB extends GenericBean<StudyCard> implements Serializable {

  @EJB
  private transient StudyCardsEJB ejb;
  @EJB
  private transient PersonsEJB personEJB;
  @EJB
  private transient StudyGroupsEJB groupsEJB;
  @EJB
  private transient StudyPlansEJB plansEJB;
  @EJB
  private transient SpecialitiesEJB specialitiesEJB;
  @EJB
  private transient DiplomeBlanksEJB diplome;
  @EJB
  private transient ReferenceBlanksEJB reference;

  private Person person;
  private Speciality speciality;
  private int personCode;
  private int cardCode;

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
    if (null == item) {
      return false;
    }
    if (null == item.getGroup()) {
      return true;
    }
    return item.getGroup().isActive();
  }

  private void getBlank(final boolean copy, final boolean duplicate) {
    StringBuilder fileName = new StringBuilder("diplome-");
    if (copy) {
      fileName.append("copy-");
    }
    if (duplicate) {
      fileName.append("duplicate-");
    }
    fileName.append(item.getId()).append(".pdf");
    // Get the FacesContext
    FacesContext facesContext = FacesContext.getCurrentInstance();
    // Get HTTP response
    ExternalContext ec = facesContext.getExternalContext();
    // Set response headers
    ec.responseReset();   // Reset the response in the first place
    ec.setResponseContentType("application/pdf");  // Set only the content type
    // Установка данного заголовка будет иннициировать процесс скачки файла вместо его отображения в браузере.
    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName.toString() + "\"");
    try (OutputStream responseOutputStream = ec.getResponseOutputStream()) {
      responseOutputStream.write(diplome.getDiplome(item, copy, duplicate));
      responseOutputStream.flush();
    } catch (IOException e) {
      addMessage(e);
    }
    facesContext.responseComplete();
  }

  public void printDiplome() {
    getBlank(false, false);
  }

  public void printReference() {
    StringBuilder fileName = new StringBuilder("reference-");
    fileName.append(item.getId()).append(".pdf");
    // Get the FacesContext
    FacesContext facesContext = FacesContext.getCurrentInstance();
    // Get HTTP response
    ExternalContext ec = facesContext.getExternalContext();
    // Set response headers
    ec.responseReset();   // Reset the response in the first place
    ec.setResponseContentType("application/pdf");  // Set only the content type
    // Установка данного заголовка будет иннициировать процесс скачки файла вместо его отображения в браузере.
    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName.toString() + "\"");
    try (OutputStream responseOutputStream = ec.getResponseOutputStream()) {
      responseOutputStream.write(reference.getBlank(item));
      responseOutputStream.flush();
    } catch (IOException e) {
      addMessage(e);
    }
    facesContext.responseComplete();
  }

  public void printDiplomeDuplicate() {
    getBlank(false, true);
  }

  public void printDiplomeCopy() {
    getBlank(true, false);
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
        speciality = specialitiesEJB.get(code);
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
      return specialitiesEJB.fetchActual();
    } else {
      return specialitiesEJB.fetchAll();
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
    if (null != speciality) {
      return groupsEJB.findBySpeciality(speciality, item.isExtramural());
    } else {
      return groupsEJB.fetchActual();
    }
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

  public void add() {
    item = new StudyCard();
    edit = true;
    if (null != person) {
      item.setPerson(person);
    }
  }

  public void save() {
    try {
      ejb.save(item);
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void confirmDelete() {
    try {
      if ((null != item) && delete) {
        ejb.delete(item);
      }
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }
}
