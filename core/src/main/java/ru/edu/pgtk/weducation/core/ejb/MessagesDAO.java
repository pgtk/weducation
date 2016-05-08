package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Account;
import ru.edu.pgtk.weducation.core.entity.Message;

import java.util.List;

/**
 * Интерфейс для корпоративного компонента управления сообщениями
 * @author Voronin Leonid
 * @since 08.05.2016
 */
public interface MessagesDAO extends WeakEntityDAO<Message> {

	List<Message> getOutgoing(final Account account);

	List<Message> getIncoming(final Account account);
}
