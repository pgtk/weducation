package ru.edu.pgtk.weducation.service.interceptors;

import ru.edu.pgtk.weducation.data.entity.AccountRole;
import ru.edu.pgtk.weducation.service.ejb.SessionEJB;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Класс перехватчика системы ббезопасности.
 * <p>
 * Для подключения перехватчика к компоненту, можно использовать аннотацию типа
 * {@code @Restricted(allowedRoles = {AccountRole.DEPARTMENT})}.</p>
 * <p>
 * Рекомендуется аннотировать методы, выполняющие значительные изменения
 * (например, удаление или изменение записи). Не рекомендуется использовать
 * перехватчики по отношению к компонентам в пакете
 * ru.edu.pgtk.weducation.webui.jsf</p>
 * <p>
 * Указывать роль администратора ({@code AccountRole.ADMIN}) не обязательно,
 * поскольку подразумевается, что администратору разрешено всё.</p>
 *
 * @author Воронин Леонид
 */
@Interceptor
@Restricted(allowedRoles = {})
public class SecurityInterceptor implements Serializable {

    @Inject
    private transient SessionEJB session;

    @AroundInvoke
    public Object checkSecurity(InvocationContext context) throws Exception {
        //System.out.println("Security checker started.");
        if ((session == null) || (session.getUser() == null)) {
            throw new SecurityException("Невозможно получить информацию о пользователе!");
        }
        // Администратору разрешено всё
        if (session.isAdmin()) {
            //System.out.println("It's admin.");
            return context.proceed();
        }
        // Если пользователь не администратор, то перебираем все роли
        for (AccountRole r : getAllowedRoles(context.getMethod())) {
            // Если нашли хоть одно совпадение, прекращаем дальнейший поиск
            // и выполняем метод
            if (session.getUser().getRole() == r) {
                //System.out.println("It's " + r.getDescription());
                return context.proceed();
            }
        }
        throw new SecurityException(session.getUser().getFullName()
                + " не имеет достаточных полномочий ");
    }

    private AccountRole[] getAllowedRoles(Method m) {
        if (null == m) {
            throw new IllegalArgumentException("Метод не должен быть null!");
        }
        // Просмотрим аннотации для метода
        for (Annotation a : m.getAnnotations()) {
            if (a instanceof Restricted) {
                return ((Restricted) a).allowedRoles();
            }
        }
        // Если поиск по методу не дал результатов, посмотрим у класса
        if (null != m.getDeclaringClass()) {
            for (Annotation a : m.getDeclaringClass().getAnnotations()) {
                if (a instanceof Restricted) {
                    return ((Restricted) a).allowedRoles();
                }
            }
        }
        // Если аннотации не найдено, выбросим исключение
        throw new RuntimeException("Аннотация @Restricted не обнаружена у метода "
                + m.getName() + " или его класса.");
    }
}
