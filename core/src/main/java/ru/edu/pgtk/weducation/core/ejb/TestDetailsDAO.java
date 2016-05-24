package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Question;
import ru.edu.pgtk.weducation.core.entity.TestDetail;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для выбранных пользователем ответов.
 * @author Voronin Leonid
 * @since 18.05.2016
 */
public interface TestDetailsDAO extends WeakEntityDAO<TestDetail> {

	List<TestDetail> fetchForQuestion(final Question question);

}
