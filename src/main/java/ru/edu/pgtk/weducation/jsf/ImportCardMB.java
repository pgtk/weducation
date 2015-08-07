package ru.edu.pgtk.weducation.jsf;

import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ru.edu.pgtk.weducation.utils.ImportCardEJB;

@ViewScoped
@Named("importCardMB")
public class ImportCardMB {

  long serialVersionUID = 0L;
  
  @Inject
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
    try {
      ejb.importAll();
      success = true;
    } catch (Exception e) {
      Utils.addMessage("Ошибка при импорте групп!");
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
