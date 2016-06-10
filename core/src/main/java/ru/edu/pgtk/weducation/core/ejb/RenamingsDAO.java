package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Renaming;

import java.util.Date;
import java.util.List;

/**
 * Интерфейс корпоративного компонента для переименований
 * Created by leonid on 07.06.16.
 */
public interface RenamingsDAO extends EntityDAO<Renaming> {

    List<Renaming> findByDates(final Date beginDate, final Date endDate);
}
