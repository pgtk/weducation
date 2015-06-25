package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import ru.edu.pgtk.weducation.ejb.FinalPracticMarksEJB;
import ru.edu.pgtk.weducation.ejb.FinalPracticsEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.entity.FinalPractic;
import ru.edu.pgtk.weducation.entity.FinalPracticMark;
import ru.edu.pgtk.weducation.entity.StudyCard;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@ViewScoped
@ManagedBean(name = "finalPracticMarksMB")
public class FinalPracticMarksMB extends GenericBean<FinalPracticMark> implements Serializable {

  long serialVersionUID = 0L;

  @Inject
  private transient FinalPracticMarksEJB ejb;
  @Inject
  private transient StudyCardsEJB cards;
  @Inject
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

  public List<FinalPracticMark> getMarks() {
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

  public StudyCard getCard() {
    return card;
  }

  public int getCardCode() {
    return cardCode;
  }

  public void setCardCode(int cardCode) {
    this.cardCode = cardCode;
  }

  @Override
  public void newItem() {
    item = new FinalPracticMark();
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
