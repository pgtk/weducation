package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.FinalPracticMarksEJB;
import ru.edu.pgtk.weducation.ejb.FinalPracticsEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.entity.FinalPractic;
import ru.edu.pgtk.weducation.entity.FinalPracticMark;
import ru.edu.pgtk.weducation.entity.StudyCard;

@ViewScoped
@ManagedBean(name = "finalPracticMarksMB")
public class FinalPracticMarksMB extends GenericBean<FinalPracticMark> implements Serializable {

  @EJB
  private transient FinalPracticMarksEJB ejb;
  @EJB
  private transient StudyCardsEJB cards;
  @EJB
  private transient FinalPracticsEJB practics;
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

  public List<FinalPracticMark> getPracticMarks() {
    if (null != card) {
      return ejb.fetchAll(card);
    }
    return new ArrayList<>();
  }

  public List<FinalPractic> getPractics() {
    if (null != card) {
      return practics.fetchForCard(card);
    }
    return new ArrayList<>();
  }

  public void add() {
    item = new FinalPracticMark();
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

  public StudyCard getCard() {
    return card;
  }

  public int getCardCode() {
    return cardCode;
  }

  public void setCardCode(int cardCode) {
    this.cardCode = cardCode;
  }
}
