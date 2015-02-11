package ru.edu.pgtk.weducation.jsf;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import ru.edu.pgtk.weducation.ejb.PracticsEJB;
import ru.edu.pgtk.weducation.ejb.SpecialitiesEJB;
import ru.edu.pgtk.weducation.ejb.StudyModulesEJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.ejb.SubjectLoadEJB;
import ru.edu.pgtk.weducation.ejb.SubjectsEJB;
import ru.edu.pgtk.weducation.entity.ExamForm;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.utils.XMLCourse;
import ru.edu.pgtk.weducation.utils.PlanParser;
import ru.edu.pgtk.weducation.utils.XMLModule;

@ManagedBean(name = "importPlanMB")
@ViewScoped
public class ImportPlanMB {

  private Part file;
  private boolean uploaded;
  private boolean createSpeciality;
  private PlanParser parser;
  @EJB
  private SpecialitiesEJB specialitiesEJB;
  @EJB
  private StudyPlansEJB plansEJB;
  @EJB
  private StudyModulesEJB modulesEJB;
  @EJB
  private PracticsEJB practicsEJB;
  @EJB
  private SubjectsEJB subjectsEJB;
  @EJB
  private SubjectLoadEJB loadEJB;

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
        if ((null == spc) && createSpeciality) {
          spc = parser.getSpeciality();
          specialitiesEJB.save(spc);
        }
        // Получаем учебный план
        StudyPlan sp = parser.getStudyPlan();
        sp.setSpeciality(spc);
        plansEJB.save(sp);
        // Импорт модулей
        for(XMLModule mod: parser.getModules()) {
          StudyModule sm;
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
          
          // Импорт практик
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

  public boolean isCreateSpeciality() {
    return createSpeciality;
  }

  public void setCreateSpeciality(boolean createSpeciality) {
    this.createSpeciality = createSpeciality;
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

  public String getSemesters() {
    try {
      if ((null != parser) && (parser.isCorrect())) {
        StringBuilder sb = new StringBuilder();
        for (XMLCourse c : parser.getCourses()) {
          sb.append(c.toString());
        }
        return sb.toString();
      } else {
        return "No courses information found!";
      }
    } catch (Exception e) {
      return "Exception was occured with message " + e.getMessage();
    }
  }
}
