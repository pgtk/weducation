package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
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

  private Person person;
  private Speciality speciality;
  private int personCode;

  public int getPersonCode() {
    return personCode;
  }

  public void setPersonCode(int personCode) {
    this.personCode = personCode;
  }

  public Person getPerson() {
    return person;
  }

  public void loadPerson() {
    try {
      if (personCode > 0) {
        person = personEJB.get(personCode);
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

  public void changeSpeciality(ValueChangeEvent event) {
    try {
      int code = (Integer) event.getNewValue();
      if (code > 0) {
        speciality = specialitiesEJB.get(code);
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
      List<StudyGroup> result = new ArrayList<>();
      if (null != item.getGroup()) {
        result.add(item.getGroup());
      }
      return result;
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
