package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.Person;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для персон
 * @author Voronin Leonid
 * @since 08.04.2016
 */
public interface PersonsDAO extends EntityDAO<Person> {

	List<Person> findByName(final String fname);

	Person findLike(final Person sample);
}
