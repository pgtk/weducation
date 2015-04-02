package ru.edu.pgtk.weducation.jsf;

import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.utils.ImportCardEJB;

@ViewScoped
@ManagedBean(name = "importCardMB")
public class ImportCardMB {
  
  @EJB
  private transient ImportCardEJB ejb;
  private String groupCode;
  
  public Map<String, String> getGroups() {
    return ejb.getGroups();
  }
  
  public void importGroup() {
    if ((null != groupCode) && (!groupCode.isEmpty())) {
      ejb.importGroup(groupCode);
    }
  }

  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }
}
