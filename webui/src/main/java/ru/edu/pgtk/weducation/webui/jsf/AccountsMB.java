package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.AccountsDAO;
import ru.edu.pgtk.weducation.core.ejb.DepartmentsDAO;
import ru.edu.pgtk.weducation.core.entity.Account;
import ru.edu.pgtk.weducation.core.entity.AccountRole;
import ru.edu.pgtk.weducation.core.entity.Department;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@Named("accountsMB")
@ViewScoped
public class AccountsMB extends GenericBean<Account> implements Serializable {

	long serialVersionUID = 0L;
	private List<Account> accountList;
	@EJB
	private AccountsDAO ejb;
	@EJB
	private DepartmentsDAO departmentsDao;

	private void updateList() {
		accountList = ejb != null ? ejb.fetchAll() : null;
	}

	public List<Department> getDepartmentsList() {
		return departmentsDao != null ? departmentsDao.fetchAll() : Collections.EMPTY_LIST;
	}

	@PostConstruct
	private void checkRestrictions() {
		if ((null == user) || (!user.isAdmin())) {
			error = true;
		} else {
			updateList();
		}
	}

	public boolean isEmptyList() {
		return accountList == null || accountList.isEmpty();
	}

	public List<Account> getAccountList() {
		return accountList;
	}

	public AccountRole[] getRoles() {
		return AccountRole.values();
	}

	public void changePassword() {
		if (null != item) {
			try {
				item.updatePassword();
				ejb.save(item);
				resetState();
				updateList();
			} catch (Exception e) {
				addMessage(e);
			}
		}
	}

	@Override
	public void newItem() {
		checkRestrictions();
		item = new Account();
	}

	@Override
	public void deleteItem() {
		if ((null != item) && delete) {
			ejb.delete(item);
			updateList();
		}
	}

	@Override
	public void saveItem() {
		if (item.getId() == 0) {
			item.updatePassword();
		}
		ejb.save(item);
		updateList();
	}
}
