package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Account;

import java.util.List;

/**
 * Интерфейс для корпоративного компонента учетных записей
 * @author Voronin Leonid
 * @since 08.05.2016
 */
public interface AccountsDAO extends EntityDAO<Account> {

	/**
	 * Получает учетную запись по логину и паролю
	 * @param login    логин
	 * @param password пароль
	 * @return экземпляр класса Account если такая учетная запись нашлась. Иначе - null
	 */
	Account get(final String login, final String password);

	/**
	 * Определяет наличие учетных записей с ролью "Администратор"
	 * @return истина, если есть хотя бы один администратор. Иначе 0 ложь
	 */
	boolean hasAdmins();

	/**
	 * Получает список учетных записей администраторов системы
	 * @return список экземпляров класса Account
	 */
	List<Account> fetchAdmins();
}
