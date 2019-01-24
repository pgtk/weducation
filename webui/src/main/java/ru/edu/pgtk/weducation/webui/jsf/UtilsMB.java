package ru.edu.pgtk.weducation.webui.jsf;

import javax.ejb.Stateless;
import javax.inject.Named;

/**
 * EJB для функций, используемых на различных страницах.
 *
 * @author Воронин Леонид
 */
@Stateless
@Named("utilsMB")
public class UtilsMB {

    /**
     * Возвращает класс для CSS в зависимости от значения логической переменной.
     * Данный метод можно использовать чтобы сделать неактивные записи тусклыми.
     *
     * @param enabled логическая переменная или выражение
     * @return CSS класс в виде строки
     */
    public String getEnabledClass(final boolean enabled) {
        return enabled ? "enabled" : "disabled";
    }


    /**
     * Возвращает удобное для восприятия человеком логическое значение.
     *
     * @param value логическое значение
     * @return Строковое представление значения (да/нет)
     */
    public String getHumanReadableBool(boolean value) {
        return value ? "да" : "нет";
    }

}
