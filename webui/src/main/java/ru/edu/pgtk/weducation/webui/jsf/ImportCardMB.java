package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.importcards.ImportCardDAO;
import ru.edu.pgtk.weducation.core.importcards.OldCardsDAO;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

@ViewScoped
@Named("importCardMB")
public class ImportCardMB implements Serializable {

    long serialVersionUID = 0L;

    @EJB
    private transient ImportCardDAO importCard;
    @EJB
    private transient OldCardsDAO oldCards;
    private String groupCode;
    private boolean success = false;

    public Map<String, String> getGroups() {
        return oldCards.getGroups();
    }

    public void importGroup() {
        if ((null != groupCode) && (!groupCode.isEmpty())) {
            try {
                importCard.importGroup(groupCode);
                success = true;
            } catch (Exception e) {
                Utils.addMessage(e);
            }
        }
    }

    public void importAll() {
        try {
            importCard.importAll();
            success = true;
        } catch (Exception e) {
            Utils.addMessage("Ошибка при импорте групп. " + e.getMessage());
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
