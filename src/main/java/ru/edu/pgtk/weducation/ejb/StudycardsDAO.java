package ru.edu.pgtk.weducation.ejb;

import ru.edu.pgtk.weducation.entity.Person;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;

import java.util.List;

/**
 * Интерфейс для корпоративного компонента учебных карточек
 * @author Voronin Leonid
 * @since 08.04.2016
 */
public interface StudycardsDAO extends EntityDAO<StudyCard> {

	List<StudyCard> findByPerson(final Person person);

	List<StudyCard> findByGroup(final StudyGroup group);

}
