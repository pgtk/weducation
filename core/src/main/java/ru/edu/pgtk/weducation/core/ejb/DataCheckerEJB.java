package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
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
        // Оценки за семестр
        q = em.createQuery(
                "SELECT sp FROM StudyPlan sp WHERE (sp.id IN (SELECT smk.subject.plan.id " +
                        "FROM SemesterMark smk WHERE (smk.card = :crd))) AND (sp.id <> :id)",
                StudyPlan.class);
        q.setParameter("crd", card);
        q.setParameter("id", card.getPlan().getId());
        resultSet.addAll(q.getResultList());
        // Оценки за курсовые
        q = em.createQuery(
                "SELECT sp FROM StudyPlan sp WHERE (sp.id IN (SELECT cmk.subject.plan.id " +
                        "FROM CourseWorkMark cmk WHERE (cmk.card = :crd))) AND (sp.id <> :id)",
                StudyPlan.class);
        q.setParameter("crd", card);
        q.setParameter("id", card.getPlan().getId());
        resultSet.addAll(q.getResultList());
        // Оценки за ГОС экзамены
        q = em.createQuery(
                "SELECT sp FROM StudyPlan sp WHERE (sp.id IN (SELECT gmk.subject.plan.id " +
                        "FROM GOSMark gmk WHERE (gmk.card = :crd))) AND (sp.id <> :id)",
                StudyPlan.class);
        q.setParameter("crd", card);
        q.setParameter("id", card.getPlan().getId());
        resultSet.addAll(q.getResultList());
        // Оценки за практику
        q = em.createQuery(
                "SELECT sp FROM StudyPlan sp WHERE (sp.id IN (SELECT pmk.practic.plan.id " +
                        "FROM PracticMark pmk WHERE (pmk.card = :crd))) AND (sp.id <> :id)",
                StudyPlan.class);
        q.setParameter("crd", card);
        q.setParameter("id", card.getPlan().getId());
        resultSet.addAll(q.getResultList());
        return resultSet;
    }

    @Override
    public List<Person> findLike(Person person) {
        return null;
    }
}
