package ru.edu.pgtk.weducation.interceptors;

import java.lang.reflect.Method;
import javax.annotation.PostConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Класс-перехватчик, реализующий журналирование. Это тестовая реализация класса
 * с целью проверки механизма работы перехватчиков. Планируется использовать
 * данный класс для всех интересующих "клиентов" журналирования.
 *
 * @author Воронин Леонид
 */
@Interceptor
@WithLog
public class LogInterceptor {

  public LogInterceptor() {

  }

  @AroundInvoke
  private Object logMethod(InvocationContext context) throws Exception {
    System.out.println("Method " + context.getMethod().getName()
        + " of class " + context.getTarget().getClass().getName() + " was called.");
    return context.proceed();
  }

  @PostConstruct
  private void construct(InvocationContext context) {
    String details = "unknown";
    Method m = context.getMethod();
    if (m != null) {
      details = context.getMethod().getName();
      Class<?> cl = m.getDeclaringClass();
      if (cl != null) {
        details = cl.getName() + "." + details;
      }
    }
    System.out.println("started @Postconstruct ("
        + details + ")");
  }
}
