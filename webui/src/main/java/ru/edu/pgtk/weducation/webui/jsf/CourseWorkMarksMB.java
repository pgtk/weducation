package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.CourseWorkMarksDAO;
import ru.edu.pgtk.weducation.core.ejb.StudycardsDAO;
import ru.edu.pgtk.weducation.core.ejb.SubjectsDAO;
import ru.edu.pgtk.weducation.core.entity.CourseWorkMark;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.Subject;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@ViewScoped
@Named("courseWorkMarksMB")
public class CourseWorkMarksMB extends GenericBean<CourseWorkMark> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private transient CourseWorkMarksDAO ejb;
	@EJB
	private transient StudycardsDAO cards;
	@EJB
	private transient SubjectsDAO subjects;
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
		item = new CourseWorkMark();
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
