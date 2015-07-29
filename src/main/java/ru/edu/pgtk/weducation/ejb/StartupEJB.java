package ru.edu.pgtk.weducation.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import ru.edu.pgtk.weducation.entity.Account;
import ru.edu.pgtk.weducation.entity.AccountRole;

/**
 * EJB для инициализации некоторых параметров. Например, этот бин будет
 * создавать пользователя, если ни одного пользователя в системе не
 * зарегистрировано.
 */
@Startup
@Singleton
public class StartupEJB {
  
  @EJB
  private transient AccountsEJB ejb;

  /**
   * Выводим список администраторов проекта и если он пуст,
     создаём учетную запись администратора.
   */
  @PostConstruct
  private void setupApplication() {
    try {
      if (ejb.fetchAdmins().isEmpty()) {
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
      System.out.println("EJB Exception: " + e.getMessage());
      e.printStackTrace(System.out);
      throw new EJBException("Ошибка при создании встроенной учетной записи администратора");
    }
  }

  @PreDestroy
  private void shutdownApplication() {
    // Может что-то и будет в дальнейшем, но пока ничего тут делать не надо
  }

}
