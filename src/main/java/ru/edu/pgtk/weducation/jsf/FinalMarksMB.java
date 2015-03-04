package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.FinalMarksEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.entity.FinalMark;
import ru.edu.pgtk.weducation.entity.StudyCard;

@ViewScoped
@ManagedBean(name = "finalMarksMB")
public class FinalMarksMB extends GenericBean<FinalMark> implements Serializable {
  
  @EJB
  private transient FinalMarksEJB ejb;
  @EJB
  private transient StudyCardsEJB cards;
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
