package ru.edu.pgtk.weducation.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.utils.PlanParser;

public class ImportPlanMB {

  private Part file;
  private String fileContent;
  private String contentType;
  private String fileName;
  private long size;
  private boolean uploaded;
  private PlanParser parser;

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
      parser = new PlanParser(file.getInputStream());
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
      if ((null != parser) && (parser.isCorrect())) {
        StudyPlan sp = parser.getStudyPlan();
        return "Обнаружен учебный план специальности \""
                + sp.getName() + " " + sp.getDescription() + 
                "\". Срок обучения - " + sp.getLength() + ", " +
                sp.getExtramural();
      } else {
        return "Документ не содержит учебных планов!";
      }
    } catch (Exception e) {
      return "Exception was occured with message " + e.getMessage();
    }
  }
}
