package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.School;

/**
 * Интерфейс корпоративного компонента для учебных заведений
 *
 * @author Voronin Leonid
 * @since 30.03.2016
 */
public interface SchoolsDAO extends EntityDAO<School> {

    /**
     * Получает текущее учебное заведение.
     *
     * @return экземпляр учебного заведения, являющегося текущим.
     */
    School getCurrent();

    /**
     * Ищет в базе данных учебное заведение, подобное образцу
     *
     * @param sample образец
     * @return экземпляр класса или Null
     */
    School findLike(final School sample);
}
