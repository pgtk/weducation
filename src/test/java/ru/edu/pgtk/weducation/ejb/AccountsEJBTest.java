package ru.edu.pgtk.weducation.ejb;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.edu.pgtk.weducation.entity.Account;
import ru.edu.pgtk.weducation.entity.AccountRole;

/**
 *
 * @author user
 */
public class AccountsEJBTest {

  private static EJBContainer container;
  private static AccountsEJB ejb;

  @BeforeClass
  public static void setUpClass() {
    try {
      Map<String, Object> properties = new HashMap<>();
      properties.put(EJBContainer.MODULES, new File("target/classes"));
      properties.put("org.glassfish.ejb.embedded.glassfish.installation.root", "glassfish");
      properties.put(EJBContainer.APP_NAME, "weducation");
      container = EJBContainer.createEJBContainer(properties);
      ejb = (AccountsEJB) container.getContext().lookup("java:global/weducation/classes/AccountsEJB");
      System.out.println("AccountsEJBTest running...");
    } catch (NamingException e) {
      fail("Ошибка при иннициализации сервера " + e.getMessage());
    }
  }

  @AfterClass
  public static void tearDownClass() {
    if (null != container) {
      container.close();
    }
    System.out.println("AccountsEJBTest finished");
  }

  /**
   * Проверяет, является ли одинаковой информация у двух экземпляров класса.
   * @param source Первый экземпляр
   * @param result Второй экземпляр
   * @return истина, если данные одинаковы. Иначе - ложь.
   */
  private boolean equals(Account source, Account result) {
    if (!source.getFullName().contentEquals(result.getFullName())) return false;
    if (!source.getLogin().contentEquals(result.getLogin())) return false;
    return source.getRole() == result.getRole();
  }
  
  /**
   * Проверка всех возможных операций над одной учетной записью.
   *
   * Сначала будет выполнено создание учетки, затем изменение пароля, и затем -
   * удаление.
   */
  @Test
  public void testOperations() {
    try {
      System.out.println("-->testOperations()");
      // Создаем нового пользователя
      Account testAccount = new Account();
      testAccount.setFullName("Test Account");
      testAccount.setLogin("test");
      testAccount.setPassword("test");
      testAccount.setConfirm("test");
      testAccount.updatePassword();
      testAccount.setRole(AccountRole.DEPOT);
      Account savedAccount = ejb.save(testAccount);
      assertTrue(equals(testAccount, savedAccount));
      
      savedAccount.setFullName("Still Test Account");
      savedAccount.setLogin("test1");
      testAccount = ejb.save(savedAccount);
      
      assertTrue(equals(testAccount, savedAccount));
      
      testAccount.setPassword("testpwd");
      testAccount.setConfirm("testpwd");
      testAccount.updatePassword();
      savedAccount = ejb.save(testAccount);
      
      assertTrue(equals(testAccount, savedAccount));

      ejb.delete(savedAccount);
    } catch (Exception e) {
      fail("Exception class " + e.getClass().getName() + " with message " + e.getMessage());
    }
  }

}
