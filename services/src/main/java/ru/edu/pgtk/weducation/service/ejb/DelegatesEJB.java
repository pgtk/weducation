package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.AccountRole;
import ru.edu.pgtk.weducation.data.entity.Delegate;
import ru.edu.pgtk.weducation.data.entity.Person;
import ru.edu.pgtk.weducation.service.interceptors.Restricted;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class DelegatesEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  public Delegate get(final int id) {
    Delegate result = em.find(Delegate.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("Delegate not found with id " + id);
  }

  public List<Delegate> fetchAll(final Person person) {
    TypedQuery<Delegate> q = em.createQuery("SELECT d FROM Delegate d WHERE (d.person = :p)", Delegate.class);
    q.setParameter("p", person);
    return q.getResultList();
  }

  @Restricted(allowedRoles = {AccountRole.DEPARTMENT, AccountRole.RECEPTION})
  public Delegate save(Delegate item) {
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  @Restricted(allowedRoles = {AccountRole.DEPARTMENT, AccountRole.RECEPTION})
  public void delete(final Delegate item) {
    Delegate d = em.find(Delegate.class, item.getId());
    if (null != d) {
      em.remove(d);
    }
  }
}
