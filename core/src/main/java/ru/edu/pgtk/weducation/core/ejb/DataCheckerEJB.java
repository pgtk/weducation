package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

/**
 * Сервисный класс для проверки данных
 * <p>
 * Created by leonid on 24.05.16.
 */
@Named("dataCheckerEJB")
@Stateless
public class DataCheckerEJB extends AbstractEJB implements DataCheckerDAO {

    @Override
    public Set<StudyPlan> getUniquePlans(StudyCard card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot check data for NULL StudyCard!");
        }
        Set<StudyPlan> resultSet = new HashSet<>();
        // оценки за месяц
        TypedQuery<StudyPlan> q = em.createQuery(
                "SELECT sp FROM StudyPlan sp WHERE (sp.id IN (SELECT mmk.subject.plan.id " +
                        "FROM MonthMark mmk WHERE (mmk.card = :crd))) AND (sp.id <> :id)",
                StudyPlan.class);
        q.setParameter("crd", card);
        q.setParameter("id", card.getPlan().getId());
        resultSet.addAll(q.getResultList());
        // TODO Добавить поиск планов по другим оценкам
        return resultSet;
    }
}
