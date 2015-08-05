package ru.edu.pgtk.weducation.interceptors;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import ru.edu.pgtk.weducation.entity.AccountRole;

/**
 * Аннотация для организации системы безопасности на основе перехватчиков.
 * @author Воронин Леонид
 */
@Inherited
@InterceptorBinding
@Target({METHOD})
@Retention(RUNTIME)
public @interface Restricted {
  @Nonbinding
  AccountRole[] roles();
}
