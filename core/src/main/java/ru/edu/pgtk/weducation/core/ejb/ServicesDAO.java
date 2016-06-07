package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.StudyPlan;

/**
 * Интерфейс сервисного корпоративного компонента
 * Created by leonid on 07.06.16.
 */
public interface ServicesDAO {

    void copyPlan(final StudyPlan source, final StudyPlan destination);

}
