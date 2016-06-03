package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.Request;
import ru.edu.pgtk.weducation.core.entity.Speciality;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для управления подачей заявками
 * <p>
 * Created by leonid on 03.06.16.
 */
public interface RequestsDAO extends WeakEntityDAO<Request> {

    Request get(final Speciality speciality, final Person person, final int year, final boolean extramural);

    List<Request> fetchAll(final Person person);

    List<Request> fetch(final Speciality speciality, final boolean extramural);

    List<Request> fetch(final Person person, final boolean extramural);

    List<Request> fetch(final Person person, final boolean extramural, final int year);

}
