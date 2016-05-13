package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.MessagesDAO;
import ru.edu.pgtk.weducation.core.ejb.SessionEJB;
import ru.edu.pgtk.weducation.core.entity.Message;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Класс для хранения JSF модели сообщений
 * Created by leonid on 13.05.16.
 */
@ViewScoped
@Named("messagesMB")
public class MessagesMB extends GenericBean<Message> implements Serializable {
    private boolean outgoing = false;
    @EJB
    private MessagesDAO messagesDao;
    private List<Message> messages;

    public void updateList() {
        try {
            if (messagesDao != null && user != null) {
                messages = outgoing ? messagesDao.getOutgoing(user) : messagesDao.getIncoming(user);
            }
        } catch (Exception e) {
            messages = null;
        }
    }

    @Override
    public void newItem() {
        item = new Message();
        item.setSourceAccount(user);
        item.setTimestamp(new Date());
        outgoing = true;
    }

    @Override
    public void deleteItem() {

    }

    @Override
    public void saveItem() {

    }

    public String getTitle() {
        return (outgoing ? "Исходящие" : "Входящие") + " сообщения";
    }

    public boolean isOutgoing() {
        return outgoing;
    }

    public void setOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
    }

    public boolean isEmptyList() {
        return messages == null || messages.isEmpty();
    }

    public List<Message> getMessages() {
        return messages != null ? messages : Collections.EMPTY_LIST;
    }
}
