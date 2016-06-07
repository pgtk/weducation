package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.SubjectLoadDAO;
import ru.edu.pgtk.weducation.core.ejb.SubjectsDAO;
import ru.edu.pgtk.weducation.core.entity.ExamForm;
import ru.edu.pgtk.weducation.core.entity.Subject;
import ru.edu.pgtk.weducation.core.entity.SubjectLoad;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("subjectLoadMB")
@ViewScoped
public class SubjectLoadMB extends GenericBean<SubjectLoad> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private SubjectLoadDAO ejb;
	@EJB
	private SubjectsDAO sejb;

	private Subject subject = null;
	private int subjectCode;

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public int getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(int subjectCode) {
		this.subjectCode = subjectCode;
	}

	public void loadSubject() {
		subject = sejb.get(subjectCode);
	}

	public List<SubjectLoad> getSubjectLoad() {
		return ejb.fetchAll(subject);
	}

	public ExamForm[] getExamForms() {
		return ExamForm.values();
	}

	@Override
	public void newItem() {
		item = new SubjectLoad();
		item.setSubject(subject);
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
