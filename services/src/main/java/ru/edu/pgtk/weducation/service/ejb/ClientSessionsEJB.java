package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.ClientSession;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ClientSessionsEJB {

  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;

  public ClientSession get(final int id) {
    ClientSession result = em.find(ClientSession.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("ClientSession not found with id " + id);
  }

  public List<ClientSession> fetchAll() {
    TypedQuery<ClientSession> q = em.createQuery(
            "SELECT cs FROM ClientSession cs ORDER BY cs.creationTime", ClientSession.class);
    return q.getResultList();
  }

  public ClientSession add(ClientSession item) {
    em.persist(item);
    return item;
  }

  public ClientSession update(ClientSession item) {
    return em.merge(item);
  }

  public ClientSession save(ClientSession item) {
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }

  public void delete(final ClientSession item) {
    ClientSession cs = em.find(ClientSession.class, item.getId());
    if (null != cs) {
      em.remove(cs);
    }
  }
}
