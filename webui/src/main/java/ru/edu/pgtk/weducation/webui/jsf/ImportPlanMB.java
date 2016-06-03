package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.*;
import ru.edu.pgtk.weducation.core.entity.*;
import ru.edu.pgtk.weducation.webui.xmlimport.*;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static ru.edu.pgtk.weducation.core.utils.Utils.getShortName;
import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

/**
 * Класс для реализации импорта планов.
 * @author Воронин Леонид
 */
@Named("importPlanMB")
@ViewScoped
public class ImportPlanMB implements Serializable {

	private final long serialVersionUID = 0L;

	private transient Part file;
	private boolean uploaded;
	@EJB
	private transient SpecialitiesDAO specialities;
	@EJB
	private transient StudyPlansDAO plans;
	@EJB
	private transient StudyModulesDAO modules;
	@EJB
	private transient PracticsDAO practics;
	@EJB
	private transient SubjectsDAO subjects;
	@EJB
	private transient SubjectLoadEJB load;
	private int specialityCode;
	private Speciality speciality;
	private List<StudyPlan> existingPlans;

	/**
	 * Анализирует учебные планы на предмет совпадения некоторых полей.
	 * Анализ выполняется по следующим полям: форма обучения, год начала действия плана.
	 * @param a Первый учебный план
	 * @param b Второй учебный план
	 * @return истина, если анализируемые поля у двух планов совпадают. Иначе - ложь.
	 */
	private boolean match(final StudyPlan a, final StudyPlan b) {
		if (a.isExtramural() != b.isExtramural()) {
			return false;
		}
		// TODO добавить еще поля для сравнения
		return a.getBeginYear() == b.getBeginYear();
	}

	public List<Speciality> getSpecialitiesList() {
		return specialities != null ? specialities.fetchAll() : Collections.EMPTY_LIST;
	}

	/**
	 * Обрабатывае событие при смене специальности.
	 * @param event событие.
	 */
	public void changeSpeciality(final ValueChangeEvent event) {
		try {
			int code = (Integer) event.getNewValue();
			if (code > 0) {
				speciality = specialities.get(code);
				existingPlans = plans.findBySpeciality(speciality);
			} else {
				speciality = null;
				existingPlans = Collections.<StudyPlan>emptyList();
			}
		} catch (Exception e) {
			speciality = null;
			existingPlans = Collections.<StudyPlan>emptyList();
			addMessage(e);
		}
	}

	/**
	 * Загружает на сервер данные учебного плана.
	 */
	public void upload() {
		uploaded = false;
		try {
			PlanParser parser = new PlanParser(file.getInputStream());
			if (parser.isCorrect()) {
				if (null == speciality) {
					addMessage("Нет данных о специальности для импорта учебного плана!");
				} else {
					// Получаем учебный план
					StudyPlan sp = parser.getStudyPlan();
					// Проверим перед импортом список планов по выбранной специальности.
					boolean exist = false;
					for (StudyPlan p : existingPlans) {
						if (match(p, sp)) {
							exist = true;
							break;
						}
					}
					// Если совпадений не обнарудено, проверим перед импортом совпадения в общем списке планов
					if (!exist) {
						List<StudyPlan> samePlans = plans.findLike(sp);
						if (!samePlans.isEmpty()) {
							exist = true;
							for (StudyPlan p : samePlans) {
								addMessage("Уже имеется учебный план \"" + p.getNameForList() + "\" с наименованием специальности \"" +
								           p.getSpecialityName() + "\" и аналогичными другими параметрами в специальности " +
								           p.getSpeciality().getName() + ". Импорт невозможен!");
							}
						}
					}
					if (!exist) {
						sp.setSpeciality(speciality);
						plans.save(sp);
						int moduleNumber = 0; //Порядковый номер модуля
						int subjectNumber = 0; //Порядковый номер дисциплины
						// Импорт модулей
						for (XMLModule mod : parser.getModules()) {
							StudyModule sm = null;
							if (mod.getType() == 2) {
								sm = new StudyModule();
								sm.setName(mod.getName());
								sm.setPlan(sp);
								sm.setNumber(moduleNumber++);
								if (mod.getKvExams() > 0) {
									sm.setExamForm(ExamForm.KVALIFEXAM);
								}
								modules.save(sm);
							}
							// Импорт дисциплин
							for (XMLSubject xs : mod.getSubjects()) {
								Subject s = new Subject();
								s.setFullName(xs.getName());
								if (xs.getName().length() <= 30) {
									s.setShortName(xs.getName());
								} else {
									s.setShortName(getShortName(xs.getName()));
								}
								s.setPlan(sp);
								s.setNumber(subjectNumber++);
								s.setModule(sm);
								subjects.save(s);
								// Нагрузка
								for (XMLSubjectLoad xsl : xs.getLoad()) {
									SubjectLoad sl = new SubjectLoad();
									sl.setSubject(s);
									sl.setMaximumLoad(xsl.getMaxLoad());
									sl.setAuditoryLoad(xsl.getAudLoad());
									sl.setCourseProjectLoad(xsl.getCprLoad());
									sl.setSemester(xsl.getSemester());
									sl.setCourse((sl.getSemester() + 1) / 2);
									sl.setExamForm(xsl.getExamType());
									load.save(sl);
								}
							}
							// Импорт практик
							for (XMLPractice xp : mod.getPractices()) {
								// Для каждой практики смотрим нагрузки
								for (XMLPracticeLoad xpl : xp.getLoad()) {
									// Формируем практику для новой базы
									Practic p = new Practic();
									p.setPlan(sp);
									p.setModule(sm);
									p.setFullName(xp.getName());
									p.setLength(xpl.getWeeks());
									p.setShortName(getShortName(p.getFullName()));
									p.setSemester(xpl.getSemester());
									p.setCourse((p.getSemester() + 1) / 2);
									practics.save(p);
								}
							}
						}
						uploaded = true;
					} else {
						addMessage("По нашим данным, учебный план для такой же формы обучения с таким же годом начала действия уже есть!\n"
						           + "Боюсь, вы не сможете импортировать этот план.");
					}
				}
			}
		} catch (Exception e) {
			addMessage(e);
		}
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public boolean isUploaded() {
		return uploaded;
	}

	public int getSpecialityCode() {
		return specialityCode;
	}

	public void setSpecialityCode(int specialityCode) {
		this.specialityCode = specialityCode;
	}

	public Speciality getSpeciality() {
		return speciality;
	}

	public List<StudyPlan> getExistingPlans() {
		return existingPlans;
	}
}
