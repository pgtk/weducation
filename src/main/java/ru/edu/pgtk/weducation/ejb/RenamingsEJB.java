package ru.edu.pgtk.weducation.ejb;

import ru.edu.pgtk.weducation.entity.Renaming;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * EJB для работы с переименованиями
 *
 * @author Воронин Леонид
 */
@Stateless
@Named("renamingsEJB")
public class RenamingsEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  public Renaming get(int id) {
    Renaming result = em.find(Renaming.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("Renaming not found with id " + id);
  }

  public List<Renaming> fetchAll() {
    TypedQuery<Renaming> q = em.createQuery("SELECT r FROM Renaming r ORDER BY r.date", Renaming.class);
    return q.getResultList();
  }

  public List<Renaming> findByDates(final Date beginDate, final Date endDate) {
    TypedQuery<Renaming> q = em.createQuery(
        "SELECT r FROM Renaming r WHERE (r.date >= :bdate) AND (r.date <= :edate) ORDER BY r.date", Renaming.class);
    q.setParameter("bdate", beginDate);
    q.setParameter("edate", endDate);
    return q.getResultList();
  }

  public Renaming save(Renaming item) {
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(final Renaming item) {
    Renaming r = em.find(Renaming.class, item.getId());
    if (null != r) {
      em.remove(r);
    }
  }
}
