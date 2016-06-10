package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import java.util.List;
import java.util.Set;

/**
 * Интерфейс сервисного класса для проверки данных
 * <p>
 * Created by leonid on 24.05.16.
 */
public interface DataCheckerDAO {

    /**
     * Производит поиск других возможных учебных планов по оценкам
     *
     * @param card карточка, для которой надо искать учебные планы
     * @return Множество учебных планов
     */
    Set<StudyPlan> getUniquePlans(final StudyCard card);

    /**
     * Производит поиск дубликатов персоны по фамилии, имени и отчеству.
     * Для каждой найденной персоны вычисляет степень похожести на основании совпадения других данных.
     *
     * @param person персона, для которой ищем дубликаты
     * @return Список персон-дубликатов
     */
    List<Person> findLike(final Person person);
}
