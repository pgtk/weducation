package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.data.entity.FinalMark;
import ru.edu.pgtk.weducation.data.entity.StudyCard;
import ru.edu.pgtk.weducation.data.entity.StudyModule;
import ru.edu.pgtk.weducation.data.entity.Subject;
import ru.edu.pgtk.weducation.service.ejb.FinalMarksEJB;
import ru.edu.pgtk.weducation.service.ejb.StudyModulesEJB;
import ru.edu.pgtk.weducation.service.ejb.StudycardsDAO;
import ru.edu.pgtk.weducation.service.ejb.SubjectsDAO;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@ViewScoped
@Named("finalMarksMB")
public class FinalMarksMB extends GenericBean<FinalMark> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private transient FinalMarksEJB ejb;
	@EJB
	private transient StudycardsDAO cards;
	@EJB
	private transient StudyModulesEJB modules;
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
