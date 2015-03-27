package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.PlacesEJB;
import ru.edu.pgtk.weducation.entity.Place;
import ru.edu.pgtk.weducation.entity.PlaceType;

@ViewScoped
@ManagedBean(name = "placesMB")
public class PlacesMB extends GenericBean<Place> implements Serializable {

  @EJB
  private transient PlacesEJB ejb;

  public PlaceType[] getPlaceTypes() {
    return PlaceType.values();
  }

  public void add() {
    item = new Place();
    edit = true;
  }

  public void save() {
    try {
      ejb.save(item);
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void confirmDelete() {
    try {
      if ((null != item) && delete) {
        ejb.delete(item);
      }
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }
}
