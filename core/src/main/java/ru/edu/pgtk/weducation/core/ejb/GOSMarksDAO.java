package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.GOSMark;
import ru.edu.pgtk.weducation.core.entity.StudyCard;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для оценок за ГОС экзамены
 * Created by leonid on 02.06.16.
 */
public interface GOSMarksDAO extends WeakEntityDAO<GOSMark> {

    List<GOSMark> fetchAll(final StudyCard card);
}
