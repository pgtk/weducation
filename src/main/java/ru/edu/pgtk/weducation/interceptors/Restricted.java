package ru.edu.pgtk.weducation.interceptors;

import ru.edu.pgtk.weducation.entity.AccountRole;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Аннотация для организации системы безопасности на основе перехватчиков.
 * @author Воронин Леонид
 */
@Inherited
@InterceptorBinding
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface Restricted {
  @Nonbinding
  AccountRole[] allowedRoles();
}
