package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.GOSExam;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;
import ru.edu.pgtk.weducation.core.entity.Subject;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для оценок за ГОС экзамены.
 * Created by leonid on 02.06.16.
 */
public interface GOSExamsDAO extends WeakEntityDAO<GOSExam> {

    List<GOSExam> fetchAll(final StudyPlan plan);

    List<Subject> fetchForCard(final StudyCard card);

}
