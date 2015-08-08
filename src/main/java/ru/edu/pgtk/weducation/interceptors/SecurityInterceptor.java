package ru.edu.pgtk.weducation.interceptors;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javax.inject.Inject;
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
 * Рекомендуется аннотировать методы, выполняющие значительные изменения
 * (например, удаление или изменение записи).
 * Не рекомендуется использовать перехватчики по отношению к компонентам в пакете
 * ru.edu.pgtk.weducation.jsf</p>
 * <p>
 * Указывать роль администратора ({@code AccountRole.ADMIN}) не обязательно,
 * поскольку подразумевается, что администратору разрешено всё.</p>
 *
 * @author Воронин Леонид
 */
@Interceptor
@Restricted(roles = {})
public class SecurityInterceptor implements Serializable {

  @Inject
  private transient Account user;

  @AroundInvoke
  public Object checkSecurity(InvocationContext context) {
    //System.out.println("Security checker started.");
    if (user == null) {
      throw new SecurityException("Cannot get current user information!");
    }
    try {
      //System.out.println("checking roles...");
      for (AccountRole r : getAllowedRoles(context.getMethod())) {
        // Если нашли хоть одно совпадение, прекращаем дальнейший поиск 
        // и выполняем метод
        if (user.getRole() == r) {
          return context.proceed();
        }
      }
      throw new SecurityException(user.getFullName()
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
