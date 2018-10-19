package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Person;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.*;

/**
 * Сервисный класс для проверки данных, хранящихся в таблице персон.
 * Поскольку разные пользователи могут добавить одного и того же человека, важо как то определить, один это человек или разные.
 * Схожесть устанавливается посредством сравнения полей и вычислении так называемого веса отличия.
 *
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
        List<Person> result = new ArrayList<>();
        TypedQuery<Person> q = em.createQuery(
                "SELECT p FROM Person p WHERE (p.firstName = :fn) AND (p.middleName = :mn) AND (p.lastName = :ln)", Person.class);
        q.setParameter("fn", person.getFirstName());
        q.setParameter("mn", person.getMiddleName());
        q.setParameter("ln", person.getLastName());
        List<Person> candidates = q.getResultList();
        for (Person candidate : candidates) {
            if (matchLevel(person, candidate) > 10) {
                result.add(candidate);
            }
        }
        return result;
    }


    public int matchLevel(Person source, Person aperson) {
        int level = 0; // Степень схожести
        if (source == null || aperson == null) {
            return level;
        }
        if (Objects.equals(source.getFullName(), aperson.getFullName())) {
            level += 5;
        }
        if (source.isForeign() == aperson.isForeign()) {
            level += 1;
        }
        if (source.isOrphan() == aperson.isOrphan()) {
            level += 1;
        }
        if (source.isInvalid() == aperson.isInvalid()) {
            level += 1;
        }
        if (source.getAverageBall() == aperson.getAverageBall()) {
            level += 1;
        }
        if (source.getPlaceCode() == aperson.getPlaceCode()) {
            level += 1;
        }
        if (source.getLanguage() == aperson.getLanguage()) {
            level += 1;
        }
        if (Objects.equals(source.getBirthDate(), aperson.getBirthDate())) {
            level += 5;
        }
        if (Objects.equals(source.getBirthPlace(), aperson.getBirthPlace())) {
            level += 5;
        }
        if (Objects.equals(source.getPassportSeria(), aperson.getPassportSeria())) {
            level += 1;
        }
        if (source.getPassportNumber() != null && Objects.equals(source.getPassportNumber(), aperson.getPassportNumber())) {
            level += 50;
        }
        if (Objects.equals(source.getPassportDate(), aperson.getPassportDate())) {
            level += 5;
        }
        if (Objects.equals(source.getPassportDept(), aperson.getPassportDept())) {
            level += 5;
        }
        if (source.getInn() != null && Objects.equals(source.getInn(), aperson.getInn())) {
            level += 50;
        }
        if (source.getSnils() != null && Objects.equals(source.getSnils(), aperson.getSnils())) {
            level += 50;
        }
        if (Objects.equals(source.getPhones(), aperson.getPhones())) {
            level += 5;
        }
        if (Objects.equals(source.getAddress(), aperson.getAddress())) {
            level += 5;
        }
        return level;
    }
}
