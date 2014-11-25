package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.StudyForm;

/**
 * Корпоративный бин для форм обучения
 *
 * @author Воронин Леонид
 */
@Stateless
public class StudyFormEJB {

  @PersistenceContext(unitName = "weducationPU")
  EntityManager em;

  public StudyForm get(final int id) throws DataException {
    StudyForm item = em.find(StudyForm.class, id);
    if (null != item) {
      return item;
    }
    throw new DataException("StudyForm not found with id " + id);
  }

  public List<StudyForm> fetchAll() {
    TypedQuery<StudyForm> query = em.createQuery("SELECT sf FROM StudyForm sf", StudyForm.class);
    return query.getResultList();
  }

  public void save(StudyForm studyForm) {
    if (studyForm.getId() == 0) {
      em.persist(studyForm);
    } else {
      em.merge(studyForm);
    }
  }

  public void delete(final StudyForm studyForm) {
    StudyForm item = em.find(StudyForm.class, studyForm.getId());
    if (null != item) {
      em.remove(item);
    }
  }
}
