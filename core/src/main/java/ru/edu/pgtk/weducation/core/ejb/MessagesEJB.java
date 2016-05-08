package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Account;
import ru.edu.pgtk.weducation.core.entity.Message;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * TODO: comment
 * @author leonid
 * @since 08.05.2016
 */
@Stateless
@Named("messagesEJB")
public class MessagesEJB extends AbstractEJB implements MessagesDAO {

	@Override
	public List<Message> getOutgoing(Account account) {
		TypedQuery<Message> q = em.createQuery("SELECT m FROM Message m WHERE (m.sourceAccount = :a) ORDER BY m.timestamp DESC", Message.class);
		q.setParameter("a", account);
		return q.getResultList();
	}

	@Override
	public List<Message> getIncoming(Account account) {
		TypedQuery<Message> q = em.createQuery("SELECT m FROM Message m WHERE (m.destinationAccount = :a) ORDER BY m.timestamp DESC", Message.class);
		q.setParameter("a", account);
		return q.getResultList();
	}

	@Override
	public Message get(int id) {
		Message result = em.find(Message.class, id);
		if (result == null) {
			throw new EJBException("Message not found with id " + id);
		}
		return result;
	}

	@Override
	public Message save(Message item) {
		if (item == null) {
			throw new IllegalArgumentException("You cannot save NULL Message!");
		}
		if (item.getId() == 0) {
			// INSERT
			em.persist(item);
			return item;
		} else {
			return em.merge(item);
		}
	}

	@Override
	public void delete(Message item) {
		if (item != null) {
			Message msg = em.find(Message.class, item.getId());
			if (msg != null) {
				em.remove(msg);
			}
		}
	}
}
