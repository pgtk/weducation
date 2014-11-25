package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Speciality;

/**
 * Корпоративный бин для специальностей
 *
 * @author Воронин Леонид
 */
@Stateless
public class SpecialityEJB {

  @PersistenceContext(unitName = "weducationPU")
  EntityManager em;

  public Speciality get(final int id) throws DataException {
    Speciality item = em.find(Speciality.class, id);
    if (null != item) {
      return item;
    }
    throw new DataException("Speciality not found with id " + id);
  }

  public List<Speciality> fetchAll() {
    TypedQuery<Speciality> query = em.createQuery("SELECT s FROM Speciality s", Speciality.class);
    return query.getResultList();
  }

  public void save(Speciality speciality) {
    if (speciality.getId() == 0) {
      em.persist(speciality);
    } else {
      em.merge(speciality);
    }
  }

  public void delete(final Speciality speciality) {
    Speciality item = em.find(Speciality.class, speciality.getId());
    if (null != item) {
      em.remove(item);
    }
  }
}
