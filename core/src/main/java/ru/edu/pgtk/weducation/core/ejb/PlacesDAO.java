package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Place;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для населенных пунктов
 * <p>
 * Created by leonid on 03.06.16.
 */
public interface PlacesDAO extends EntityDAO<Place> {

    List<Place> findByName(final String name);

    Place findLike(final Place sample);

}
