package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.*;

import java.util.List;

/**
 * Интерфейс корпоративного компонента семестровых оценок.
 * Created by leonid on 07.06.16.
 */
public interface SemesterMarksDAO extends WeakEntityDAO<SemesterMark> {

    SemesterMark get(final StudyCard card, final Subject subject, final int course, final int semester);

    SemesterMark get(final StudyCard card, final StudyModule module, final int course, final int semester);

    List<SemesterMark> fetchAll(final StudyGroup group, final Subject subject, final int course, final int semester);

    List<SemesterMark> fetchAll(final StudyGroup group, final StudyModule module, final int course, final int semester);

}
