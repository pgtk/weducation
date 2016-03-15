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

	/**
	 * Получает список отправленных сообщений
	 * @param account учетная запись от имени которой были отправлены сообщения
	 * @return список сообщений
	 */
	List<Message> getOutgoing(final Account account);

	/**
	 * Получает список входящих сообщений
	 * @param account учетная запись, для которой получается список
	 * @return список сообщений
	 */
	List<Message> getIncoming(final Account account);

	/**
	 * Отправляет информационное собщение всем пользователям с ролью "Администратор"
	 * @param title   заголовок сообщения
	 * @param message само сообщение
	 */
	void sendNotification(final String title, final String message);
}
