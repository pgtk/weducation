package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.FinalMark;
import ru.edu.pgtk.weducation.entity.StudyCard;

@Stateless
@Named("finalMarksEJB")
public class FinalMarksEJB {

  @EJB
  private SubjectsEJB subjects;
  @EJB
  private StudyModulesEJB modules;
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  public FinalMark get(final int id) {
    FinalMark result = em.find(FinalMark.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("FinalMark not found with id " + id);
  }

  public List<FinalMark> fetchAll(final StudyCard card) {
    TypedQuery<FinalMark> q = em.createQuery(
            "SELECT fm FROM FinalMark fm WHERE (fm.card = :c) ORDER BY fm.module.name, fm.subject.fullName", FinalMark.class);
    q.setParameter("c", card);
    return q.getResultList();
  }

  public List<FinalMark> fetchModules(final StudyCard card) {
    TypedQuery<FinalMark> q = em.createQuery(
            "SELECT fm FROM FinalMark fm WHERE (fm.card = :c) AND (fm.subject IS NULL)"
            + "ORDER BY fm.module.name", FinalMark.class);
    q.setParameter("c", card);
    return q.getResultList();
  }

  public List<FinalMark> fetchSubjects(final StudyCard card) {
    TypedQuery<FinalMark> q = em.createQuery(
            "SELECT fm FROM FinalMark fm WHERE (fm.card = :c) AND (fm.subject IS NOT NULL)"
            + "ORDER BY fm.subject.fullName", FinalMark.class);
    q.setParameter("c", card);
    return q.getResultList();
  }

  public FinalMark save(FinalMark item) {
    if (item.getSubjectCode() > 0) {
      item.setSubject(subjects.get(item.getSubjectCode()));
    }
    if (item.getModuleCode() > 0) {
      item.setModule(modules.get(item.getModuleCode()));
    }
    if (0 == item.getId()) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(final FinalMark item) {
    FinalMark fm = em.find(FinalMark.class, item.getId());
    if (null != fm) {
      em.remove(fm);
    }
  }
}
