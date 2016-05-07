package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.Department;
import ru.edu.pgtk.weducation.data.entity.StudyGroup;

import java.util.List;

/**
 * Интерфейс корпоративного компонента учебной группы
 * Created by Voronin Leonis on 01.04.16.
 */
public interface StudyGroupsDAO extends EntityDAO<StudyGroup> {

    /**
     * Выбирает только актуальные группы
     *
     * @return список групп
     */
    List<StudyGroup> fetchActual();

    /**
     * Выбирает группы для определенного оделения
     *
     * @param department отделение
     * @return список групп
     */
    List<StudyGroup> findByDepartment(final Department department);

    /**
     * Ищет группу по наименованию
     *
     * @param name наименование группы
     * @return группа, либо null, если нет ничего с таким наименованием
     */
    StudyGroup findByName(final String name);
}
