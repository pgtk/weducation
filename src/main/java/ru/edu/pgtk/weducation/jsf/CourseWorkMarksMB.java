package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.CourseWorkMarksEJB;
import ru.edu.pgtk.weducation.ejb.StudyCardsEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.CourseWorkMark;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.Subject;

@ViewScoped
@ManagedBean(name = "courseWorkMarksMB")
public class CourseWorkMarksMB extends GenericBean<CourseWorkMark> implements Serializable {
  
  @EJB
  private transient CourseWorkMarksEJB ejb;
  @EJB
  private transient StudyCardsEJB cards;
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
  
  public List<CourseWorkMark> getMarks() {
    if (null != card) {
      return ejb.fetchAll(card);
    }
    return new ArrayList<>();
  }
  
  public List<Subject> getSubjects() {
    if (null != card) {
      return subjects.fetchCourseWorksForCard(card);
    }
    return new ArrayList<>();
  }

  public void add() {
    item = new CourseWorkMark();
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