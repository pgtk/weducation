package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.ClientSession;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("clientSessionsEJB")
public class ClientSessionsEJB extends AbstractEJB implements ClientSessionsDAO {

    @Override
    public ClientSession get(final int id) {
        ClientSession result = em.find(ClientSession.class, id);
        if (null != result) {
            return result;
        }
        throw new EJBException("ClientSession not found with id " + id);
    }

    @Override
    public List<ClientSession> fetchAll() {
        TypedQuery<ClientSession> q = em.createQuery(
                "SELECT cs FROM ClientSession cs ORDER BY cs.creationTime", ClientSession.class);
        return q.getResultList();
    }

    @Override
    public ClientSession add(ClientSession item) {
        em.persist(item);
        return item;
    }

    @Override
    public ClientSession update(ClientSession item) {
        return em.merge(item);
    }

    @Override
    public ClientSession save(ClientSession item) {
        if (item.getId() == 0) {
            em.persist(item);
            return item;
        } else {
            return em.merge(item);
        }
    }

    @Override
    public void delete(final ClientSession item) {
        ClientSession cs = em.find(ClientSession.class, item.getId());
        if (null != cs) {
            em.remove(cs);
        }
    }
}
