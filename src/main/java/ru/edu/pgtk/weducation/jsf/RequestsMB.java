package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import ru.edu.pgtk.weducation.ejb.PersonsEJB;
import ru.edu.pgtk.weducation.ejb.RequestsEJB;
import ru.edu.pgtk.weducation.ejb.SpecialitiesEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.entity.Person;
import ru.edu.pgtk.weducation.entity.Request;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyCard;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@ViewScoped
@ManagedBean(name = "requestsMB")
public class RequestsMB implements Serializable {

  long serialVersionUID = 0L;

  @Inject
  private transient RequestsEJB ejb;
  @Inject
  private transient SpecialitiesEJB specialities;
  @Inject
  private transient PersonsEJB persons;
  @Inject
  private transient StudyCardsEJB cards;
  private Person person;
  private int personCode;
  private int year;
  private boolean extramural;
  private int[] selectedSpecialities;
  private List<Speciality> specList;
  private List<Request> requestList;
  private boolean edit = false;

  private void prepareLists() {
    if ((person != null) && (year > 0)) {
      requestList = ejb.fetch(person, extramural, year);
      // Получаем список специальностей
      specList = specialities.fetchSuggestions(person, extramural, year);
    }
  }
  
  private void resetState() {
    selectedSpecialities = null;
    prepareLists();
    edit = false;
  }

  public void loadPerson() {
    if (personCode > 0) {
      person = persons.get(personCode);
    } else {
      person = null;
    }
  }

  public Map<String, Integer> getYears() {
    Map<String, Integer> result = new TreeMap<>();
    Calendar now = new GregorianCalendar();
    int y = now.get(Calendar.YEAR);
    for (int i = y - 1; i <= y + 1; i++) {
      result.put(i + "-й год", i);
    }
    return result;
  }

  public void changeYear(ValueChangeEvent event) {
    try {
      year = (Integer) event.getNewValue();
      prepareLists();
    } catch (Exception e) {
      specList = null;
      requestList = null;
      addMessage(e);
    }
  }

  public void changeForm(ValueChangeEvent event) {
    try {
      extramural = (Boolean) event.getNewValue();
      prepareLists();
    } catch (Exception e) {
      specList = null;
      requestList = null;
      addMessage(e);
    }
  }

  public void delete(final Request item) {
    try {
      ejb.delete(item);
      resetState();
    } catch (Exception e) {
      addMessage(e);
      resetState();
    }
  }

  public void save() {
    try {
      for (int s : selectedSpecialities) {
        Request nr = new Request();
        nr.setSpeciality(specialities.get(s));
        nr.setPerson(person);
        nr.setYear(year);
        nr.setExtramural(extramural);
        ejb.save(nr);
      }
      resetState();
    } catch (Exception e) {
      addMessage(e);
      resetState();
    }
  }
  
  public void cancel() {
    resetState();
  }

  public void addRequests() {
    edit = true;
  }

  public int getPersonCode() {
    return personCode;
  }

  public void setPersonCode(int personCode) {
    this.personCode = personCode;
  }

  public Person getPerson() {
    return person;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public boolean isExtramural() {
    return extramural;
  }

  public void setExtramural(boolean extramural) {
    this.extramural = extramural;
  }

  public List<Speciality> getSpecList() {
    return specList;
  }

  public List<Request> getRequestList() {
    return requestList;
  }

  public boolean isEdit() {
    return edit;
  }

  public int[] getSelectedSpecialities() {
    return selectedSpecialities;
  }

  public void setSelectedSpecialities(int[] selectedSpecialities) {
    this.selectedSpecialities = selectedSpecialities;
  }
}
