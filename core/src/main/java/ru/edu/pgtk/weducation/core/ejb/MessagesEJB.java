package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Account;
import ru.edu.pgtk.weducation.core.entity.Message;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Корпоративный компонент для работы с сообщениями
 *
 * @author Voronin Leonid
 * @since 08.05.2016
 */
@Stateless
@Named("messagesEJB")
public class MessagesEJB extends AbstractEJB implements MessagesDAO {

    @EJB
    private AccountsDAO accountsDao;

    @Override
    public void sendNotification(String title, String message) {
        try {
            if (title != null && message != null) {
                List<Account> admins = accountsDao.fetchAdmins();
                for (Account admin : admins) {
                    Message msg = new Message();
                    msg.setFrom("Служба нотификации");
                    msg.setDeleted(true); // Чтобы сообщения удалялись сразу после прочтения
                    msg.setDestinationAccount(admin);
                    msg.setTimestamp(new Date());
                    msg.setTitle(title);
                    msg.setText(message);
                    save(msg);
                }
            }
        } catch (Exception e) {
            throw new EJBException("Ошибка при попытке отправить сообщение: " + e.getMessage());
        }
    }

    @Override
    public List<Message> getOutgoing(Account account) {
        TypedQuery<Message> q = em.createQuery("SELECT m FROM Message m WHERE (m.sourceAccount = :a) AND (m.deleted = false) ORDER BY m.timestamp DESC", Message.class);
        q.setParameter("a", account);
        return q.getResultList();
    }

    @Override
    public List<Message> getIncoming(Account account) {
        TypedQuery<Message> q = em.createQuery("SELECT m FROM Message m WHERE (m.destinationAccount = :a) AND (m.received = false) ORDER BY m.timestamp DESC", Message.class);
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
