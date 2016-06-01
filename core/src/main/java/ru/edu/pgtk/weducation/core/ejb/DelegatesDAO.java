package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Delegate;
import ru.edu.pgtk.weducation.core.entity.Person;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для работы с законными представителями
 * Created by admin on 01.06.2016.
 */
public interface DelegatesDAO extends WeakEntityDAO<Delegate> {

    List<Delegate> fetchAll(final Person person);
}
