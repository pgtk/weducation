package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Account;

/**
 * Интерфейс для корпоративного компонента учетных записей
 * @author Voronin Leonid
 * @since 08.05.2016
 */
public interface AccountsDAO extends EntityDAO<Account> {

	Account get(final String login, final String password);

	boolean hasAdmins();
}
