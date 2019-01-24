package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Question;
import ru.edu.pgtk.weducation.core.entity.Test;

import java.util.List;

/**
 * Интерфейс для корпоративного компонента управления вопросами
 *
 * @author Voronin Leonid
 * @since 17.05.2016
 */
public interface QuestionsDAO extends WeakEntityDAO<Question> {

    List<Question> fetchForTest(final Test test);
}
