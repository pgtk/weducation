package ru.edu.pgtk.weducation.interceptors;

import java.util.logging.Logger;
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

  @AroundInvoke
  public Object logMethod(InvocationContext context) throws Exception {
//    Logger logger = Logger.getLogger(context.getTarget().getClass().getName());
//    logger.entering(context.getTarget().getClass().getName(), context.getMethod().getName());
//    try {
    System.out.println("Method " + context.getMethod().getName() + 
        " of class " + context.getTarget().getClass().getName() + " was called.");
    return context.proceed();
//    } finally {
//      logger.exiting(context.getTarget().getClass().getName(), context.getMethod().getName());
//    }
  }
  
  @PostConstruct
  public void construct(InvocationContext context) {
    System.out.println("@Postconstruct of " + 
        context.getMethod().getDeclaringClass().getName() + " started.");
  }
}
