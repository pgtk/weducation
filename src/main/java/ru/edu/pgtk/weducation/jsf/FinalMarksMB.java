package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import ru.edu.pgtk.weducation.ejb.FinalMarksEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.ejb.StudyModulesEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.FinalMark;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.Subject;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@ViewScoped
@ManagedBean(name = "finalMarksMB")
public class FinalMarksMB extends GenericBean<FinalMark> implements Serializable {

  long serialVersionUID = 0L;
  
  @Inject
  private transient FinalMarksEJB ejb;
  @Inject
  private transient StudyCardsEJB cards;
  @Inject
  private transient StudyModulesEJB modules;
  @Inject
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

  public int getCardCode() {
    return cardCode;
  }

  public void setCardCode(int cardCode) {
    this.cardCode = cardCode;
  }

  public StudyCard getCard() {
    return card;
  }

  @Override
  public void newItem() {
    item = new FinalMark();
    item.setCard(card);
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
  }
}
