package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import java.util.Set;

/**
 * Интерфейс сервисного класса для проверки данных
 * <p>
 * Created by leonid on 24.05.16.
 */
public interface DataCheckerDAO {

    Set<StudyPlan> getUniquePlans(final StudyCard card);
}
