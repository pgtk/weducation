package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.WeekMissing;

import java.util.List;

/**
 * Интерфейс для корпоративного компонента недельных пропусков.
 * Created by leonid on 02.06.16.
 */
public interface WeekMissingsDAO extends WeakEntityDAO<WeekMissing> {

    WeekMissing get(final StudyCard card, final int year, final int month, final int week);

    WeekMissing get(final StudyCard card, final int year, final int month);

    List<WeekMissing> fetchAll(final StudyGroup group, final int year, final int month, final int week);

}
