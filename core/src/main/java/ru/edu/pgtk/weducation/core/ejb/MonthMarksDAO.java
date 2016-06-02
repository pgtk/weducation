package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.MonthMark;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.Subject;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для оценок за месяц.
 * Created by leonid on 02.06.16.
 */
public interface MonthMarksDAO extends WeakEntityDAO<MonthMark> {

    MonthMark get(final StudyCard card, final Subject subject, final int year, final int month);

    List<MonthMark> fetchAll(final StudyGroup group, final Subject subject, final int year, final int month);

}
