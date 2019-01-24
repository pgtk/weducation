package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.DelegatesEJB;
import ru.edu.pgtk.weducation.core.ejb.PersonsDAO;
import ru.edu.pgtk.weducation.core.entity.Delegate;
import ru.edu.pgtk.weducation.core.entity.Person;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@ViewScoped
@Named("delegatesMB")
public class DelegatesMB extends GenericBean<Delegate> implements Serializable {

    long serialVersionUID = 0L;

    @EJB
    private transient DelegatesEJB ejb;
    @EJB
    private transient PersonsDAO persons;
    private int personCode;
    private int code;
    private Person person;
    private List<Delegate> delegates;

    private void prepareList() {
        if (null != person) {
            delegates = ejb.fetchAll(person);
        } else if (item != null) {
            delegates = ejb.fetchAll(item.getPerson());
        } else {
            delegates = new ArrayList<>();
        }
    }

    public void preparePage() {
        try {
            if (personCode > 0) {
                person = persons.get(personCode);
                prepareList();
            } else {
                person = null;
            }
            if (code > 0) {
                item = ejb.get(code);
                details = true;
            } else {
                item = null;
            }
        } catch (Exception e) {
            person = null;
            item = null;
            addMessage(e);
        }
    }

    public List<Delegate> getDelegates() {
        if (null == delegates) {
            prepareList();
        }
        return delegates;
    }

    public int getPersonCode() {
        return personCode;
    }

    public void setPersonCode(int personCode) {
        this.personCode = personCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public void newItem() {
        item = new Delegate();
        item.setPerson(person);
    }

    @Override
    public void deleteItem() {
        if ((null != item) && delete) {
            ejb.delete(item);
            prepareList();
        }
    }

    @Override
    public void saveItem() {
        ejb.save(item);
        prepareList();
    }
}
