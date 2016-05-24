package ru.edu.pgtk.weducation.webtest.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Абстрактный класс-предок, в котором реализован общий для всех бинов функционал
 * Created by leonid on 21.05.16.
 */
class AbstractBean {

    protected void addErrorMessage(Exception e) {
        Throwable t = e;
        String message = t.getMessage();
        if (message == null || message.isEmpty()) {
            if (t.getCause() != null) {
                t = t.getCause();
                message = t.getMessage();
            }
        }
        if (message == null || message.isEmpty()) {
            message = "Исключение " + t.getClass().getName();
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, t.getClass().getName());
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            context.addMessage(t.getClass().getName(), msg);
        }
    }
}
