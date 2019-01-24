package ru.edu.pgtk.weducation.core.interceptors;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Аннотация, которая будет говорить о необходимости логировать
 * методы и конструкторы определенных компонентов.
 *
 * @author Воронин Леонид
 */
@Inherited
@InterceptorBinding
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface WithLog {
    // Никаких параметров для аннотации не предусмотрено
}
