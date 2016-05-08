package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Practic;
import ru.edu.pgtk.weducation.core.entity.StudyCard;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;
import ru.edu.pgtk.weducation.core.entity.StudyModule;
import ru.edu.pgtk.weducation.core.entity.StudyPlan;
import ru.edu.pgtk.weducation.core.entity.Subject;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("studyModulesEJB")
public class StudyModulesEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  @Inject
  private StudyPlansDAO plans;
  @Inject
  private SubjectsDAO subjects;
  @Inject
  private PracticsEJB practics;

  public StudyModule get(final int id) {
    StudyModule result = em.find(StudyModule.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("StudyModule not found with id " + id);
  }

  public List<StudyModule> fetchAll(final StudyPlan plan) {
    TypedQuery<StudyModule> q = em.createQuery(
            "SELECT sm FROM StudyModule sm WHERE (sm.plan = :pln) ORDER BY sm.name", StudyModule.class);
    q.setParameter("pln", plan);
    return q.getResultList();
  }
  
  public List<StudyModule> fetch(final StudyGroup group, final int course, final int semester) {
    TypedQuery<StudyModule> q = em.createQuery(
            "SELECT m FROM StudyModule m WHERE (m.plan = :pln) AND "
            + "((SELECT COUNT(sl) FROM SubjectLoad sl WHERE (sl.subject.module = m) AND (sl.course = :c) AND (sl.semester = :s)) > 0 )"
            + " ORDER BY m.name", StudyModule.class);
    q.setParameter("pln", group.getPlan());
    q.setParameter("c", course);
    q.setParameter("s", semester);
    return q.getResultList();
  }
  

  public List<StudyModule> fetchForCard(final StudyCard card) {
    TypedQuery<StudyModule> q = em.createQuery(
            "SELECT sp FROM StudyModule sp WHERE (sp.plan = :pln) AND "
            + "(sp.id NOT IN (SELECT fm.module.id FROM FinalMark fm WHERE (fm.card = :c) AND (fm.subject IS NULL))) ORDER BY sp.name", StudyModule.class);
    q.setParameter("pln", card.getPlan());
    q.setParameter("c", card);
    return q.getResultList();
  }
  
  public void copy(final StudyModule source, final StudyModule destination) {
    if (null == source) {
      throw new IllegalArgumentException("Модуль-источник равен null! Копирование невозможно!");
    }
    if (null == destination) {
      throw new IllegalArgumentException("Модуль-назначение равен null! Копирование невозможно!");
    }
    if (!subjects.fetchForModule(destination).isEmpty()) {
      throw new IllegalArgumentException("Модуль-назначение уже содержит дисциплины. Копирование невозможно!");
    }
    if (!practics.fetch(destination).isEmpty()) {
      throw new IllegalArgumentException("Модуль-назначение уже содержит практики. Копирование невозможно!");
    }
    // Копируем практики
    for (Practic p: practics.fetch(source)) {
      Practic copy = new Practic(p);
      copy.setModule(destination);
      copy.setPlan(destination.getPlan());
      practics.save(copy);
    }
    // Копируем дисциплины
    for (Subject s: subjects.fetchForModule(source)) {
      Subject copy = new Subject(s);
      copy.setModule(destination);
      copy.setPlan(destination.getPlan());
      subjects.save(copy);
      subjects.copy(s, copy);
    }
  }

  public StudyModule save(StudyModule item) {
    item.setPlan(plans.get(item.getPlanCode()));
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(final StudyModule item) {
    StudyModule sm = em.find(StudyModule.class, item.getId());
    if (null != sm) {
      em.remove(sm);
    }
  }
}
