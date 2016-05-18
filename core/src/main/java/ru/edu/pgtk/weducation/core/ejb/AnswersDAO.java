package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Answer;
import ru.edu.pgtk.weducation.core.entity.Question;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для работы с вариантами ответов
 * Created by admin on 18.05.2016.
 */
public interface AnswersDAO extends WeakEntityDAO<Answer> {

    List<Answer> fetchForQuestion(final Question question);
}
