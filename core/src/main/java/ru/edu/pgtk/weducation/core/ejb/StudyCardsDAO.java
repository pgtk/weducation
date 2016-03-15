package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.Speciality;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;

import java.util.List;

/**
 * Интерфейс для корпоративного компонента учебных карточек
 * @author Voronin Leonid
 * @since 08.04.2016
 */
public interface StudyCardsDAO extends EntityDAO<StudyCard> {

	List<StudyCard> findByPerson(final Person person);

	List<StudyCard> findByGroup(final StudyGroup group);

	StudyCard get(final Speciality speciality, boolean extramural, String biletNumber);

}
