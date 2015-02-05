package ru.edu.pgtk.weducation.jsf;

import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import ru.edu.pgtk.weducation.entity.Speciality;
import ru.edu.pgtk.weducation.utils.PlanXMLParser;

public class ImportPlanMB {

  private Part file;
  private String fileContent;
  private String contentType;
  private String fileName;
  private long size;
  private boolean uploaded;
  private PlanXMLParser parser;

  private void addMessage(final Exception e) {
    FacesContext context = FacesContext.getCurrentInstance();
    String message = "Exception class " + e.getClass().getName() + " with message " + e.getMessage();
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "Error"));
  }

  public void upload() {
    try {
      contentType = file.getContentType();
      size = file.getSize();
      fileName = file.getSubmittedFileName();
      uploaded = true;
      parser = new PlanXMLParser(file.getInputStream());
//      if (parser.isCorrectXML()) {
        // Получаем курсы...
//        List<Course> courses = parser.getCourses();
//        for (Course c : courses) {
        //Найден курс
//        }
        //Получаем модули и дисциплины...
//        List<Module> modules = parser.getModules();
//        for (Module m : modules) {
        //Найден модуль
//          for (Subject s : m.getSubjects()) {
        // Дисциплина
//            for (SubjectLoad l : s.getLoad()) {
        // Нагрузка по дисциплине
//            }
//          }
//          for (Practice p : m.getPractices()) {
        // Практика
//            for (PracticeLoad l : p.getLoad()) {
        // Нагрузка по практике
//            }
//          }
//        }
//      }

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

  public String getContentType() {
    return contentType;
  }

  public String getFileName() {
    return fileName;
  }

  public long getSize() {
    return size;
  }

  public boolean isUploaded() {
    return uploaded;
  }

  public String getPlanTitle() {
    try {
      if ((null != parser) && (parser.isCorrectXML())) {
        Speciality spec = parser.getSpeciality();
        return "Обнаружен учебный план специальности \""
                + spec.getKey() + " " + spec.getFullName() + "\". Квалификация - " + spec.getKvalification();
      } else {
        return "Документ не содержит учебных планов!";
      }
    } catch (Exception e) {
      return "Exception was occured with message " + e.getMessage();
    }
  }
}
