package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.GroupSemester;
import ru.edu.pgtk.weducation.data.entity.StudyGroup;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для семестров групп
 * Created by Voronin Leonid on 01.04.16.
 */
public interface GroupSemestersDAO extends WeakEntityDAO<GroupSemester> {

    /**
     * Получает семестр по данным группы, а так же порядковому номеру курса и семестра
     *
     * @param group    группа
     * @param course   курс
     * @param semester семестр
     * @return семестр группы
     */
    GroupSemester get(final StudyGroup group, final int course, final int semester);

    /**
     * Получает семестр по данным группы, года и месяца
     *
     * @param group группа
     * @param year  год
     * @param month месяц
     * @return семестр группы
     */
    GroupSemester getByMonth(final StudyGroup group, final int year, final int month);

    /**
     * Получает все семестры для конкретной группы
     *
     * @param group группа
     * @return список семестров группы
     */
    List<GroupSemester> fetchAll(final StudyGroup group);
}
