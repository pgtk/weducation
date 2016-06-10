package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.StudyModule;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для работы с модулями учебного плана
 * Created by Voronin Leonid on 01.06.16.
 */
public interface StudyModulesDAO extends WeakEntityDAO<StudyModule> {

    List<StudyModule> fetchAll(final StudyPlan plan);

    List<StudyModule> fetch(final StudyGroup group, final int course, final int semester);

    List<StudyModule> fetchForCard(final StudyCard card);

    void copy(final StudyModule source, final StudyModule destination);
}
