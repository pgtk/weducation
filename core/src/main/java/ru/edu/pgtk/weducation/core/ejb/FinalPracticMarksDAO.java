package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.FinalPracticMark;
import ru.edu.pgtk.weducation.core.entity.StudyCard;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для управления оценками за итоговую практику.
 * Created by leonid on 02.06.16.
 */
public interface FinalPracticMarksDAO extends WeakEntityDAO<FinalPracticMark> {

    List<FinalPracticMark> fetchAll(final StudyCard card);

    float getSummaryLoad(final StudyCard card);

}
