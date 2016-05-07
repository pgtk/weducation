package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.Department;
import ru.edu.pgtk.weducation.data.entity.Person;
import ru.edu.pgtk.weducation.data.entity.Speciality;

import java.util.List;

/**
 * Интерфейс для корпоративного компонента специальностей
 * @author Voronin Leonid
 * @since 10.04.2016
 */
public interface SpecialitiesDAO extends EntityDAO<Speciality> {

	List<Speciality> fetchAll(final Department department);

	List<Speciality> fetchActual();

	List<Speciality> fetchActual(final boolean extramural);

	List<Speciality> fetchActual(final Department department);

	List<Speciality> fetchAviable();

	List<Speciality> fetchAviable(final boolean extramural);

	List<Speciality> fetchAviable(final Department department);

	List<Speciality> fetchSuggestions(final Person person, final boolean extramural, final int year);

	Speciality findLike(final Speciality sample);
}
