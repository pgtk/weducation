package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.FinalMark;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyModule;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для управления итоговыми оценками
 * Created by leonid on 02.06.16.
 */
public interface FinalMarksDAO extends WeakEntityDAO<FinalMark> {

    float getAverageMark(final StudyCard card);

    List<FinalMark> fetchAll(final StudyCard card);

    List<FinalMark> fetchModules(final StudyCard card);

    List<FinalMark> fetchModuleSubjects(final StudyCard card, final StudyModule module);

    List<FinalMark> fetchOnlySubjects(final StudyCard card);

    List<FinalMark> fetchSubjects(final StudyCard card);

}
