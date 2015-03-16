package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import ru.edu.pgtk.weducation.ejb.FinalMarksEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.ejb.StudyModulesEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.FinalMark;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.Subject;

@ViewScoped
@ManagedBean(name = "finalMarksMB")
public class FinalMarksMB extends GenericBean<FinalMark> implements Serializable {

  @EJB
  private transient FinalMarksEJB ejb;
  @EJB
  private transient StudyCardsEJB cards;
  @EJB
  private transient StudyModulesEJB modules;
  @EJB
  private transient SubjectsEJB subjects;
  private StudyCard card;
  private int cardCode;

  public void loadCard() {
    try {
      if (cardCode > 0) {
        card = cards.get(cardCode);
      }
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public List<FinalMark> getModuleMarks() {
    return ejb.fetchModules(card);
  }

  public List<FinalMark> getSubjectMarks() {
    if (card != null) {
      return ejb.fetchSubjects(card);
    }
    return new ArrayList<>();
  }

  public List<StudyModule> getModules() {
    if (null != card) {
      return modules.fetchForCard(card);
    }
    return new ArrayList<>();
  }

  public List<Subject> getSubjects() {
    if (null != card) {
      return subjects.fetchForCard(card);
    }
    return new ArrayList<>();
  }

  public void countLoad(ValueChangeEvent event) {
    try {
      int code = (Integer) event.getNewValue();
      if (code > 0) {
        Subject s = subjects.get(code);
        item.setSubject(s);
        // Запишем модуль, к которому принадлежит дисциплина
        item.setModule(s.getModule());
        // Посчитаем кол-во часов аудиторной нагрузки
        item.setAuditoryLoad(subjects.getAudLoad(s));
        // И максимальной
        item.setMaximumLoad(subjects.getMaxLoad(s));
      } else {
        addMessage("Не удалось правильно обработать смену дисциплины!");
      }
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void add() {
    item = new FinalMark();
    item.setCard(card);
    edit = true;
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

  public int getCardCode() {
    return cardCode;
  }

  public void setCardCode(int cardCode) {
    this.cardCode = cardCode;
  }

  public StudyCard getCard() {
    return card;
  }
}
