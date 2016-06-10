package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Practic;
import ru.edu.pgtk.weducation.core.entity.PracticMark;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для оценок по практике
 * <p>
 * Created by leonid on 03.06.16.
 */
public interface PracticMarksDAO extends WeakEntityDAO<PracticMark> {

    PracticMark get(final StudyCard card, final Practic practic);

    List<PracticMark> fetchAll(final StudyGroup group, final Practic practic);
}


