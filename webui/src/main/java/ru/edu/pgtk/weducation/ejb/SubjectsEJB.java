package ru.edu.pgtk.weducation.ejb;

import ru.edu.pgtk.weducation.entity.ExamForm;
import ru.edu.pgtk.weducation.entity.StudyCard;
import ru.edu.pgtk.weducation.entity.StudyGroup;
import ru.edu.pgtk.weducation.entity.StudyModule;
import ru.edu.pgtk.weducation.entity.StudyPlan;
import ru.edu.pgtk.weducation.entity.Subject;
import ru.edu.pgtk.weducation.entity.SubjectLoad;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("subjectsEJB")
public class SubjectsEJB extends AbstractEJB implements SubjectsDAO {

  @Inject
  private SubjectLoadEJB load;

  @Override
  public Subject get(final int id) {
    Subject result = em.find(Subject.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("Subject not found with id " + id);
  }

  @Override
  public int getMaxLoad(final Subject subject) {
    TypedQuery<Long> q = em.createQuery(
      "SELECT SUM(sl.maximumLoad) FROM SubjectLoad sl WHERE (sl.subject = :s)", Long.class);
    q.setParameter("s", subject);
    return q.getSingleResult().intValue();
  }

  @Override
  public int getAudLoad(final Subject subject) {
    TypedQuery<Long> q = em.createQuery(
      "SELECT SUM(sl.auditoryLoad) FROM SubjectLoad sl WHERE (sl.subject = :s)", Long.class);
    q.setParameter("s", subject);
    return q.getSingleResult().intValue();
  }

  @Override
  public List<Subject> fetchAll(final StudyPlan plan) {
    TypedQuery<Subject> q = em.createQuery(
      "SELECT s FROM Subject s WHERE (s.plan = :pln) ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", plan);
    return q.getResultList();
  }

  @Override
  public List<Subject> fetchForCard(final StudyCard card) {
    TypedQuery<Subject> q = em.createQuery(
      "SELECT s FROM Subject s WHERE (s.plan = :pln) AND "
      + "(s.id NOT IN (SELECT fm.subject.id FROM FinalMark fm WHERE (fm.card = :c))) ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", card.getPlan());
    q.setParameter("c", card);
    return q.getResultList();
  }

  @Override
  public List<Subject> fetchForModule(final StudyModule module) {
    TypedQuery<Subject> q = em.createQuery(
      "SELECT s FROM Subject s WHERE (s.plan = :pln) AND "
      + "(s.module = :mod) ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", module.getPlan());
    q.setParameter("mod", module);
    return q.getResultList();
  }

  @Override
  public List<Subject> fetchNoModules(final StudyPlan plan) {
    TypedQuery<Subject> q = em.createQuery(
      "SELECT s FROM Subject s WHERE (s.plan = :pln) AND "
      + "(s.module is null) ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", plan);
    return q.getResultList();
  }

  @Override
  public List<Subject> fetchCourseWorksForCard(final StudyCard card) {
    TypedQuery<Subject> q = em.createQuery(
      "SELECT s FROM Subject s WHERE (s.plan = :pln) AND "
      + "((SELECT COUNT(sl) FROM SubjectLoad sl WHERE (sl.subject = s) AND (sl.courseProjectLoad > 0)) > 0 )"
      + " ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", card.getPlan());
    return q.getResultList();
  }

  @Override
  public List<Subject> fetchCourseWorks(final StudyGroup group, final int course, final int semester) {
    TypedQuery<Subject> q = em.createQuery(
      "SELECT s FROM Subject s WHERE (s.plan = :pln) AND ((SELECT COUNT(sl) FROM SubjectLoad sl "
      + "WHERE (sl.subject = s) AND (sl.course = :c) AND (sl.semester = :s) AND (sl.courseProjectLoad > 0)) > 0 )"
      + " ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", group.getPlan());
    q.setParameter("c", course);
    q.setParameter("s", semester);
    return q.getResultList();
  }

  @Override
  public List<Subject> fetchExams(final StudyGroup group, final int course, final int semester) {
    TypedQuery<Subject> q = em.createQuery(
      "SELECT s FROM Subject s WHERE (s.plan = :pln) AND ((SELECT COUNT(sl) FROM SubjectLoad sl "
      + "WHERE (sl.subject = s) AND (sl.course = :c) AND (sl.semester = :s) AND (sl.examForm = :f)) > 0 )"
      + " ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", group.getPlan());
    q.setParameter("c", course);
    q.setParameter("s", semester);
    q.setParameter("f", ExamForm.EXAM);
    return q.getResultList();
  }

  @Override
  public List<Subject> fetchZachets(final StudyGroup group, final int course, final int semester) {
    TypedQuery<Subject> q = em.createQuery(
      "SELECT s FROM Subject s WHERE (s.plan = :pln) AND ((SELECT COUNT(sl) FROM SubjectLoad sl "
      + "WHERE (sl.subject = s) AND (sl.course = :c) AND (sl.semester = :s) AND ((sl.examForm = :f1) OR (sl.examForm = :f2))) > 0 )"
      + " ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", group.getPlan());
    q.setParameter("c", course);
    q.setParameter("s", semester);
    q.setParameter("f1", ExamForm.DIFZACHET);
    q.setParameter("f2", ExamForm.ZACHET);
    return q.getResultList();
  }

  @Override
  public List<Subject> fetch(final StudyGroup group, final int course, final int semester) {
    TypedQuery<Subject> q = em.createQuery(
      "SELECT s FROM Subject s WHERE (s.plan = :pln) AND "
      + "((SELECT COUNT(sl) FROM SubjectLoad sl WHERE (sl.subject = s) AND (sl.course = :c) AND (sl.semester = :s)) > 0 )"
      + " ORDER BY s.fullName", Subject.class);
    q.setParameter("pln", group.getPlan());
    q.setParameter("c", course);
    q.setParameter("s", semester);
    return q.getResultList();
  }

  /**
   * Копирует учебную нагрузку по семестрам из одной дисциплины в другую.
   *
   * @param source дисциплина-источник - та, из которой будет копироваться нагрузка.
   * @param destination дисциплина-назначение - та, в которую будет копироваться нагрузка.
   */
  @Override
  public void copy(final Subject source, final Subject destination) {
    if (null == source) {
      throw new IllegalArgumentException("Дисциплина-источник не может быть null! Копирование невозможно.");
    }
    if (null == destination) {
      throw new IllegalArgumentException("Дисциплина-назначение не может быть null! Копирование невозможно.");
    }
    if (!load.fetchAll(destination).isEmpty()) {
      throw new IllegalArgumentException("Дисциплина-назначение уже имеет какую-то нагрузку. Копирование невозможно!");
    }
    for (SubjectLoad sl: load.fetchAll(source)) {
      SubjectLoad copy = new SubjectLoad(sl);
      copy.setSubject(destination);
      load.save(copy);
    }
  }

  @Override
  public Subject save(Subject item) {
    if (item.getModuleCode() > 0) {
      StudyModule m = em.find(StudyModule.class, item.getModuleCode());
      if (null != m) {
        item.setModule(m);
      }
    } else {
      item.setModule(null);
    }
    if (item.getPlanCode() > 0) {
      StudyPlan p = em.find(StudyPlan.class, item.getPlanCode());
      if (null != p) {
        item.setPlan(p);
      }
    }
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  @Override
  public void delete(Subject item) {
    Subject s = em.find(Subject.class, item.getId());
    if (null != s) {
      em.remove(s);
    }
  }
}
