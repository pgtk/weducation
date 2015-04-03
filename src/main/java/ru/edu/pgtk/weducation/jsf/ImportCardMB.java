package ru.edu.pgtk.weducation.jsf;

import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.utils.ImportCardEJB;
import ru.edu.pgtk.weducation.utils.Utils;

@ViewScoped
@ManagedBean(name = "importCardMB")
public class ImportCardMB {

  @EJB
  private transient ImportCardEJB ejb;
  private String groupCode;
  private boolean success = false;

  public Map<String, String> getGroups() {
    return ejb.getGroups();
  }

  public void importGroup() {
    if ((null != groupCode) && (!groupCode.isEmpty())) {
      try {
        ejb.importGroup(groupCode);
        success = true;
      } catch (Exception e) {
        Utils.addMessage(e);
      }
    }
  }
  
  public void importAll() {
    Map<String, String> groups = ejb.getGroups();
    for(String key: groups.keySet()) {
      try {
      ejb.importGroup(key);
      } catch (Exception e) {
      Utils.addMessage("Ошибка при импорте группы с кодом " + key);
      }
    }
  }
  
  public void reset() {
    success = false;
  }

  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  public boolean isSuccess() {
    return success;
  }
}
