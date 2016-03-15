package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.GroupSemestersDAO;
import ru.edu.pgtk.weducation.core.ejb.StudyGroupsDAO;
import ru.edu.pgtk.weducation.core.entity.GroupSemester;
import ru.edu.pgtk.weducation.core.entity.StudyGroup;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@Named("groupSemestersMB")
@ViewScoped
public class GroupSemestersMB extends GenericBean<GroupSemester> implements Serializable {

	long serialVersionUID = 0L;

	@EJB
	private transient GroupSemestersDAO ejb;
	@EJB
	private transient StudyGroupsDAO groups;
	private int groupCode;
	private StudyGroup group;

	public void loadGroup() {
		try {
			if (groupCode > 0) {
				group = groups.get(groupCode);
			} else {
				group = null;
			}
		} catch (Exception e) {
			addMessage(e);
		}
	}

	public List<GroupSemester> getSemesters() {
		return ejb.fetchAll(group);
	}

	public int getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(int groupCode) {
		this.groupCode = groupCode;
	}

	public StudyGroup getGroup() {
		return group;
	}

	@Override
	public void newItem() {
		if (group != null) {
			item = new GroupSemester();
			item.setGroup(group);
		} else {
			addMessage("You can't add semesters for unknown group!");
		}
	}

	@Override
	public void deleteItem() {
		if ((null != item) && delete) {
			ejb.delete(item);
		}
	}

	@Override
	public void saveItem() {
		ejb.save(item);
	}
}
