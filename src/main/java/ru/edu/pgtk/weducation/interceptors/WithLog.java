package ru.edu.pgtk.weducation.interceptors;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.interceptor.InterceptorBinding;

/**
 * Аннотация, которая будет говорить о необходимости логировать 
 * методы и конструкторы определенных компонентов.
 * @author Воронин Леонид
 */
@Inherited
@InterceptorBinding
@Target({TYPE})
@Retention(RUNTIME)
public @interface WithLog {
  // Никаких параметров для аннотации не предусмотрено  
}
