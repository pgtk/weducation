package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.StudyProfile;

/**
 * Корпоративный бин для профилей обучения
 *
 * @author Воронин Леонид
 */
@Stateless
public class StudyProfileEJB {

  @PersistenceContext
  EntityManager em;

  public StudyProfile get(final int id) throws DataException {
    StudyProfile item = em.find(StudyProfile.class, id);
    if (null != item) {
      return item;
    }
    throw new DataException("StudyProfile not found with id " + id);
  }

  public List<StudyProfile> fetchAll() {
    TypedQuery<StudyProfile> query = em.createQuery("SELECT sp FROM StudyProfile sp", StudyProfile.class);
    return query.getResultList();
  }

  public void save(StudyProfile profile) {
    if (profile.getId() == 0) {
      em.persist(profile);
    } else {
      em.merge(profile);
    }
  }

  public void delete(final StudyProfile profile) {
    StudyProfile item = em.find(StudyProfile.class, profile.getId());
    if (null != item) {
      em.remove(item);
    }
  }
}
