package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.AccountsDAO;
import ru.edu.pgtk.weducation.core.ejb.MessagesDAO;
import ru.edu.pgtk.weducation.core.entity.Account;
import ru.edu.pgtk.weducation.core.entity.Message;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
	long serialVersionUID = 0L;
	private boolean outgoing = false;
	@EJB
	private MessagesDAO messagesDao;
	@EJB
	private AccountsDAO accountsDao;
	private List<Message> messages;
	private int desinationAccountCode;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	@PostConstruct
	private void updateList() {
		desinationAccountCode = 0;
		try {
			if (messagesDao != null && user != null) {
				messages = outgoing ? messagesDao.getOutgoing(user) : messagesDao.getIncoming(user);
			}
		} catch (Exception e) {
			messages = null;
			addError(e);
		}
	}

	public String getLabel() {
		return "К списку " + (outgoing ? "входящих" : "исходящих");
	}

	public void changeThread() {
		outgoing = !outgoing;
		updateList();
	}

	@Override
	public void newItem() {
		item = new Message();
		item.setSourceAccount(user);
		item.setFrom(user.getFullName());
		item.setTimestamp(new Date());
		desinationAccountCode = 0;
	}

	public void hide(Message item) {
		if (item != null && item.getId() > 0) {
			try {
				if (outgoing) {
					item.setDeleted(true);
				} else {
					item.setReceived(true);
				}
				if (item.isDeleted() && item.isReceived()) {
					messagesDao.delete(item);
				} else {
					messagesDao.save(item);
				}
			} catch (Exception e) {
				addError(e);
			}
		}
		updateList();
	}

	@Override
	public void deleteItem() {
		messagesDao.delete(item);
		updateList();
	}

	@Override
	public void saveItem() {
		if (desinationAccountCode > 0) {
			item.setDestinationAccount(accountsDao.get(desinationAccountCode));
			messagesDao.save(item);
		}
		updateList();
	}

	public String getMessageTimestamp(Date date) {
		return date == null ? "" : simpleDateFormat.format(date);
	}

	public List<Account> getAccountsList() {
		return accountsDao != null ? accountsDao.fetchAll() : Collections.EMPTY_LIST;
	}

	public String getTitle() {
		return (outgoing ? "Исходящие" : "Входящие") + " сообщения";
	}

	public boolean isOutgoing() {
		return outgoing;
	}

	public boolean isEmptyList() {
		return messages == null || messages.isEmpty();
	}

	public List<Message> getMessages() {
		return messages != null ? messages : Collections.EMPTY_LIST;
	}

	public int getDesinationAccountCode() {
		return desinationAccountCode;
	}

	public void setDesinationAccountCode(int desinationAccountCode) {
		this.desinationAccountCode = desinationAccountCode;
	}
}
