package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import ru.edu.pgtk.weducation.ejb.PracticsEJB;
import ru.edu.pgtk.weducation.ejb.SpecialitiesEJB;
import ru.edu.pgtk.weducation.ejb.StudyModulesEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.ejb.SubjectLoadEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.ExamForm;
import ru.edu.pgtk.weducation.entity.Practic;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.entity.Subject;
import ru.edu.pgtk.weducation.entity.SubjectLoad;
import ru.edu.pgtk.weducation.utils.PlanParser;
import static ru.edu.pgtk.weducation.utils.Utils.getShortName;
import ru.edu.pgtk.weducation.utils.XMLModule;
import ru.edu.pgtk.weducation.utils.XMLPractice;
import ru.edu.pgtk.weducation.utils.XMLPracticeLoad;
import ru.edu.pgtk.weducation.utils.XMLSubject;
import ru.edu.pgtk.weducation.utils.XMLSubjectLoad;

@Named("importPlanMB")
@ViewScoped
public class ImportPlanMB implements Serializable {

  long serialVersionUID = 0L;

  private transient Part file;
  private boolean uploaded;
  private transient PlanParser parser;
  @Inject
  private transient SpecialitiesEJB specialitiesEJB;
  @Inject
  private transient StudyPlansEJB plansEJB;
  @Inject
  private transient StudyModulesEJB modulesEJB;
  @Inject
  private transient PracticsEJB practicsEJB;
  @Inject
  private transient SubjectsEJB subjectsEJB;
  @Inject
  private transient SubjectLoadEJB loadEJB;

  private void addMessage(final Exception e) {
    FacesContext context = FacesContext.getCurrentInstance();
    String message = "Exception class " + e.getClass().getName() + " with message " + e.getMessage();
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "Error"));
  }

  public void upload() {
    try {
      uploaded = true;
      parser = new PlanParser(file.getInputStream());
      if ((null != parser) && (parser.isCorrect())) {
        //Импорт плана из файла и сохранение в базу
        Speciality spc = specialitiesEJB.findByKey(parser.getSpecialityKey());
        // Если специальности не найдено, то...
        if (null == spc) {
          spc = parser.getSpeciality();
          specialitiesEJB.save(spc);
        }
        // Получаем учебный план
        StudyPlan sp = parser.getStudyPlan();
        sp.setSpeciality(spc);
        plansEJB.save(sp);
        // Импорт модулей
        for (XMLModule mod : parser.getModules()) {
          StudyModule sm = null;
          if (mod.getType() == 2) {
            sm = new StudyModule();
            sm.setName(mod.getName());
            sm.setPlan(sp);
            if (mod.getKvExams() > 0) {
              sm.setExamForm(ExamForm.KVALIFEXAM);
            }
            modulesEJB.save(sm);
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
            s.setModule(sm);
            subjectsEJB.save(s);
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
              loadEJB.save(sl);
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
              p.setSemester(xpl.getSemester());
              p.setCourse((p.getSemester() + 1) / 2);
              practicsEJB.save(p);
            }
          }
        }
      }
    } catch (Exception e) {
      uploaded = false;
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

  public String getPlanTitle() {
    try {
      if ((null != parser) && (parser.isCorrect())) {
        StudyPlan sp = parser.getStudyPlan();
        return "Обнаружен учебный план специальности \""
          + sp.getName() + " " + sp.getDescription()
          + "\". Срок обучения - " + sp.getLength() + ", "
          + sp.getExtramural();
      } else {
        return "Документ не содержит учебных планов!";
      }
    } catch (Exception e) {
      return "Exception was occured with message " + e.getMessage();
    }
  }
}
