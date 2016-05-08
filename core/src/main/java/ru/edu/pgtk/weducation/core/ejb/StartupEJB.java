package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Account;
import ru.edu.pgtk.weducation.core.entity.AccountRole;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * EJB для инициализации некоторых параметров. Например, этот бин будет
 * создавать пользователя, если ни одного пользователя в системе не
 * зарегистрировано.
 */
@Startup
@Singleton
public class StartupEJB {

	@EJB
	private transient AccountsDAO ejb;

	/**
	 * Выполняем начальную настройку приложения.
	 */
	@PostConstruct
	private void setupApplication() {
		try {
			if (!ejb.hasAdmins()) {
				// Ни одного администратора нет, нужно создать!
				Account admin = new Account();
				admin.setFullName("Администратор системы");
				admin.setLogin("admin");
				admin.setPassword("admin");
				admin.setConfirm("admin");
				admin.updatePassword();
				admin.setRole(AccountRole.ADMIN);
				ejb.save(admin);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace(System.err);
			throw new RuntimeException("Ошибка при создании встроенной учетной записи администратора");
		}
	}

	@PreDestroy
	private void shutdownApplication() {
		// Может что-то и будет в дальнейшем, но пока ничего тут делать не надо
	}

}
