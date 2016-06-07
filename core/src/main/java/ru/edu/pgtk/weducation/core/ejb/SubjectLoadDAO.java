package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.StudyPlan;
import ru.edu.pgtk.weducation.core.entity.Subject;
import ru.edu.pgtk.weducation.core.entity.SubjectLoad;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для нагрузки по дисциплине
 * Created by leonid on 07.06.16.
 */
public interface SubjectLoadDAO extends WeakEntityDAO<SubjectLoad> {

    List<SubjectLoad> fetchAll(final Subject subject);

    List<SubjectLoad> fetch(final StudyPlan plan, final int course, final int semester);
}
