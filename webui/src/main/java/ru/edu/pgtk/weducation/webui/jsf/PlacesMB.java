package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.PlacesEJB;
import ru.edu.pgtk.weducation.core.entity.Place;
import ru.edu.pgtk.weducation.core.entity.PlaceType;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@Named("placesMB")
public class PlacesMB extends GenericBean<Place> implements Serializable {

	long serialVersionUID = 0L;
	private List<Place> list;

	@EJB
	private transient PlacesEJB ejb;

	@PostConstruct
	private void updateList() {
		list = ejb != null ? ejb.fetchAll() : null;
	}

	public List<Place> getList() {
		return list;
	}

	public boolean isEmptyList() {
		return list == null || list.isEmpty();
	}

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
