package ru.edu.pgtk.weducation.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import ru.edu.pgtk.weducation.entity.Account;
import ru.edu.pgtk.weducation.entity.AccountRole;

/**
 * Класс перехватчика системы ббезопасности.
 * <p>
 * Для подключения перехватчика к компоненту, можно использовать аннотацию типа
 * {@code @Restricted(roles = {AccountRole.DEPARTMENT})}.</p>
 * <p>
 * Указывать роль администратора ({@code AccountRole.ADMIN}) не обязательно,
 * поскольку подразумевается, что администратору разрешено всё.</p>
 *
 * @author Воронин Леонид
 */
@Interceptor
public class SecurityInterceptor {

  @AroundInvoke
  @Restricted(roles = {})
  public Object checkSecurity(InvocationContext context) {
    try {
      // Попробуем получить учетную запись
      Logger log = Logger.getLogger(context.getTarget().getClass().getName());
      log.info("Ищем sessionMB.user в контексте");
      FacesContext fc = FacesContext.getCurrentInstance();
      Account account = fc.getApplication().evaluateExpressionGet(fc,
          "#{sessionMB.user}", Account.class);
      if (null == account) {
        log.info("Либо учетка не найдена, либо пользователь не залогинился!");
        // Пользователь не залогинился, выкидываем исключение
        throw new SecurityException("Seems like user was not logged in!");
      }
      for (AccountRole r : getAllowedRoles(context.getMethod())) {
        // Если нашли хоть одно совпадение, прекращаем дальнейший поиск 
        // и выполняем метод
        if (account.getRole() == r) {
          return context.proceed();
        }
      }
      throw new SecurityException(account.getFullName()
          + " cannot access method " + context.getMethod().getName());
    } catch (Exception e) {
      throw new SecurityException("Exception class " + e.getClass().getName()
          + "with message " + e.getMessage());
    }
  }

  private AccountRole[] getAllowedRoles(Method m) {
    // Просмотрим аннотации для метода
    for (Annotation a : m.getAnnotations()) {
      if (a instanceof Restricted) {
        return ((Restricted) a).roles();
      }
    }
    // Если поиск по методу не дал результатов, посмотрим у класса
    for (Annotation a : m.getDeclaringClass().getAnnotations()) {
      if (a instanceof Restricted) {
        return ((Restricted) a).roles();
      }
    }
    // Если аннотации не найдено, выбросим исключение
    throw new RuntimeException("@Restricted not found on method " + m.getName()
        + " or its class " + m.getClass().getName());
  }
}
