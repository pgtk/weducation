package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Account;
import ru.edu.pgtk.weducation.core.entity.AccountRole;
import ru.edu.pgtk.weducation.core.interceptors.Restricted;
import ru.edu.pgtk.weducation.core.interceptors.WithLog;
import ru.edu.pgtk.weducation.core.utils.Utils;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@Named("accountsEJB")
public class AccountsEJB extends AbstractEJB implements AccountsDAO {

	@Override
	@Restricted(allowedRoles = {}) // Неявно разрешено только администратору
	public Account get(final int id) {
		Account result = em.find(Account.class, id);
		if (null != result) {
			return result;
		}
		throw new EJBException("Account not found with id " + id);
	}

	@Override
	public Account get(final String login, final String password) {
		TypedQuery<Account> q = em.createQuery(
				"SELECT a FROM Account a WHERE (a.login = :l) AND (a.passwordHash = :p)",
				Account.class);
		q.setParameter("l", login);
		q.setParameter("p", Utils.getHash(password));
		try {
			return q.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	@Restricted(allowedRoles = {}) // Неявно разрешено только администратору
	public List<Account> fetchAll() {
		TypedQuery<Account> q = em.createQuery(
				"SELECT a FROM Account a ORDER BY a.fullName", Account.class);
		return q.getResultList();
	}

	@Override
	public boolean hasAdmins() {
		TypedQuery<Long> q = em.createQuery(
				"SELECT COUNT(a) FROM Account a WHERE (a.role = :r)", Long.class);
		q.setParameter("r", AccountRole.ADMIN);
		return q.getSingleResult() > 0;
	}

	@Override
	@WithLog
	@Restricted(allowedRoles = {}) // Неявно разрешено только админу
	public Account save(Account item) {
		if (item.getId() == 0) {
			em.persist(item);
			return item;
		} else {
			return em.merge(item);
		}
	}

	@Override
	@WithLog
	@Restricted(allowedRoles = {}) // Неявно разрешено только админу
	public void delete(final Account item) {
		Account a = em.find(Account.class, item.getId());
		if (null != a) {
			em.remove(a);
		}
	}
}
