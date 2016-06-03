package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Practic;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.StudyModule;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для работы с практиками
 * Created by leonid on 03.06.16.
 */
public interface PracticsDAO extends WeakEntityDAO<Practic> {
    List<Practic> findByPlan(final StudyPlan plan);

    List<Practic> fetch(final StudyModule module);

    List<Practic> fetch(final StudyGroup group, final int course, final int semester);

}
