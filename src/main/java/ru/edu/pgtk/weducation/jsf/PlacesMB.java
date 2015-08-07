package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ru.edu.pgtk.weducation.ejb.PlacesEJB;
import ru.edu.pgtk.weducation.entity.Place;
import ru.edu.pgtk.weducation.entity.PlaceType;

@ViewScoped
@Named("placesMB")
public class PlacesMB extends GenericBean<Place> implements Serializable {

  long serialVersionUID = 0L;

  @Inject
  private transient PlacesEJB ejb;

  public PlaceType[] getPlaceTypes() {
    return PlaceType.values();
  }

  @Override
  public void newItem() {
    item = new Place();
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
