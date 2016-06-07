package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Seat;
import ru.edu.pgtk.weducation.core.entity.Speciality;

import java.util.List;

/**
 * Интерфейс корпоративного компонента управления контрольными цифрами приема
 * Created by leonid on 07.06.16.
 */
public interface SeatsDAO extends WeakEntityDAO<Seat> {

    Seat get(final int year, final Speciality speciality, final boolean extramural);

    List<Seat> fetch(final int year, final boolean extramural);

}
