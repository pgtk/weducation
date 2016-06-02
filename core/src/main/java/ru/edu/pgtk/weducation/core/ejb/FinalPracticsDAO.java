package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.FinalPractic;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import java.util.List;

/**
 * Интерфейс корпоративного компонента для работы с итоговыми практиками
 * Created by leonid on 02.06.16.
 */
public interface FinalPracticsDAO extends WeakEntityDAO<FinalPractic> {

    List<FinalPractic> fetchAll(final StudyPlan plan);

    List<FinalPractic> fetchForCard(final StudyCard card);

}
