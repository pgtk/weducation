package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import ru.edu.pgtk.weducation.ejb.GOSExamsEJB;
import ru.edu.pgtk.weducation.ejb.GOSMarksEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.entity.GOSMark;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.Subject;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@ViewScoped
@ManagedBean(name = "gosMarksMB")
public class GOSMarksMB extends GenericBean<GOSMark> implements Serializable {

  long serialVersionUID = 0L;

  @Inject
  private transient GOSMarksEJB ejb;
  @Inject
  private transient StudyCardsEJB cards;
  @Inject
  private transient GOSExamsEJB exams;
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

  public List<GOSMark> getMarks() {
    if (null != card) {
      return ejb.fetchAll(card);
    }
    return new ArrayList<>();
  }

  public List<Subject> getSubjects() {
    if (null != card) {
      return exams.fetchForCard(card);
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
    item = new GOSMark();
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
