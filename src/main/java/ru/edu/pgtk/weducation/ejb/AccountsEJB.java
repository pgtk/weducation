package ru.edu.pgtk.weducation.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import ru.edu.pgtk.weducation.entity.Account;
import ru.edu.pgtk.weducation.entity.AccountRole;
import static ru.edu.pgtk.weducation.utils.Utils.getHash;

@Stateless
@Named("accountsEJB")
public class AccountsEJB {
  
  @PersistenceContext(unitName = "weducationPU")
  private EntityManager em;
  
  public Account get(final int id) {
    Account result = em.find(Account.class, id);
    if (null != result) {
      return result;
    }
    throw new EJBException("Account not found with id " + id);
  }
  
  public Account get(final String login, final String password) {
    TypedQuery<Account> q = em.createQuery(
            "SELECT a FROM Account a WHERE (a.login = :l) AND (a.passwordHash = :p)",
            Account.class);
    q.setParameter("l", login);
    q.setParameter("p", getHash(password));
    try {
      Account result = q.getSingleResult();
      return result;
    } catch (Exception e) {
      return null;
    }
  }
  
  public List<Account> fetchAll() {
    TypedQuery<Account> q = em.createQuery(
            "SELECT a FROM Account a ORDER BY a.fullName", Account.class);
    return q.getResultList();
  }
  
  public List<Account> fetchAdmins() {
    TypedQuery<Account> q = em.createQuery(
            "SELECT a FROM Account a WHERE (a.role = :r) ORDER BY a.fullName", Account.class);
    q.setParameter("r", AccountRole.ADMIN);
    return q.getResultList();
  }
  
  public Account save(Account item) {
    if (item.getId() == 0) {
      em.persist(item);
      return item;
    } else {
      return em.merge(item);
    }
  }
  
  public void delete(final Account item) {
    Account a = em.find(Account.class, item.getId());
    if (null != a) {
      em.remove(a);
    }
  }  
}
