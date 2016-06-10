package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.FinalPractic;
import ru.edu.pgtk.weducation.core.entity.StudyModule;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;
import ru.edu.pgtk.weducation.core.entity.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * EJB компонент для реализации различных серсов.
 *
 * @author Воронин Леонид
 */
@Stateless
@Named("servicesEJB")
public class ServicesEJB extends AbstractEJB implements ServicesDAO {
    @Inject
    private StudyPlansDAO plans;
    @Inject
    private SubjectsDAO subjects;
    @Inject
    private PracticsDAO practics;
    @Inject
    private StudyModulesDAO modules;
    @Inject
    private FinalPracticsDAO fpractics;

    /**
     * Копирует содержимое из другого плана.
     *
     * @param source      План-источник из которого копируется содержимое.
     * @param destination План-назначение в которое копируется содержимое.
     */
    @Override
    public void copyPlan(final StudyPlan source, final StudyPlan destination) {
        if (null == source) {
            throw new IllegalArgumentException("Источник для копирования плана не должен быть null!");
        }
        if (null == destination) {
            throw new IllegalArgumentException("Назначение для копирования плана не должен быть null!");
        }
        // Проверяем наличие отсутствия практик
        if (!practics.findByPlan(destination).isEmpty()) {
            throw new IllegalArgumentException("В назначении уже есть практики. Копирование невозможно!");
        }
        // Проверяем наличие отсутствия модулей
        if (!modules.fetchAll(destination).isEmpty()) {
            throw new IllegalArgumentException("В назначении уже есть модули. Копирование невозможно!");
        }
        // Проверяем наличие отсутствия дисциплин
        if (!subjects.fetchAll(destination).isEmpty()) {
            throw new IllegalArgumentException("В назначении уже есть дисциплины. Копирование невозможно!");
        }
        // Проверяем наличие отсутствия итоговых практик
        if (!fpractics.fetchAll(destination).isEmpty()) {
            throw new IllegalArgumentException("В назначении уже есть итоговые практики. Копирование невозможно!");
        }
        // Проверяем наличие дисциплин
        if (subjects.fetchAll(source).isEmpty()) {
            throw new IllegalArgumentException("В источнике отсутствуют дисциплины. Копирование невозможно!");
        }
        // Проверяем наличие отсутствия итоговых практик
        if (fpractics.fetchAll(source).isEmpty()) {
            throw new IllegalArgumentException("В отсутствуют итоговые практики. Копирование невозможно!");
        }
        // Копируем итоговые практики
        for (FinalPractic fp : fpractics.fetchAll(source)) {
            FinalPractic copy = new FinalPractic(fp);
            copy.setPlan(destination);
            fpractics.save(copy);
        }
        // Копируем модули
        for (StudyModule sm : modules.fetchAll(source)) {
            StudyModule copy = new StudyModule(sm);
            copy.setPlan(destination);
            modules.save(copy);
            modules.copy(sm, copy);
        }
        // Копируем дисциплины без модулей
        for (Subject s : subjects.fetchNoModules(source)) {
            Subject copy = new Subject(s);
            copy.setModule(null);
            copy.setPlan(destination);
            subjects.save(copy);
            subjects.copy(s, copy);
        }
    }
}
