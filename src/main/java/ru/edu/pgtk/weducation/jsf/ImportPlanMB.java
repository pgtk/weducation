package ru.edu.pgtk.weducation.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

public class ImportPlanMB {

  private Part file;
  private String fileContent;
  private String contentType;
  private String fileName;
  private long size;
  private boolean uploaded;

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
}
