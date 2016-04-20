package ru.edu.pgtk.weducation.ejb;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import ru.edu.pgtk.weducation.data.entity.Account;
import ru.edu.pgtk.weducation.data.entity.AccountRole;
import ru.edu.pgtk.weducation.helpers.ContainerProvider;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author user
 */
@Ignore
public class AccountsEJBTest {

  private static ContainerProvider provider;
  private static AccountsEJB ejb;

  @BeforeClass
  public static void setUpClass() {
    provider = new ContainerProvider();
    ejb = (AccountsEJB) provider.getBean("AccountsEJB");
    System.out.println("AccountsEJBTest running...");
  }

  @AfterClass
  public static void tearDownClass() {
    try {
      provider.close();
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * Проверяет, является ли одинаковой информация у двух экземпляров класса.
   *
   * @param source Первый экземпляр
   * @param result Второй экземпляр
   * @return истина, если данные одинаковы. Иначе - ложь.
   */
  private boolean equals(Account source, Account result) {
    if (!source.getFullName().contentEquals(result.getFullName())) {
      return false;
    }
    if (!source.getLogin().contentEquals(result.getLogin())) {
      return false;
    }
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
