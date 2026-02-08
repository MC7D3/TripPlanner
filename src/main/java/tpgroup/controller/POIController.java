package tpgroup.controller;

import java.util.List;

import tpgroup.model.Session;
import tpgroup.model.bean.POIBean;
import tpgroup.model.bean.POIFilterBean;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.domain.Rating;
import tpgroup.model.domain.Tag;
import tpgroup.persistence.DAO;
import tpgroup.persistence.factory.DAOFactory;

public class POIController {

	public POIController() {
		super();
	}

	public List<POIBean> getAllPOI(){
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getAll().stream().map(poi -> new POIBean(poi)).toList();
	}

	private List<POIBean> getPOIFiltered(List<Tag> tags){
		return getPOIFiltered().stream().filter(poi -> poi.getTags().containsAll(tags)).toList();
	}

	private List<POIBean> getPOIFiltered(){
		String destCountry = Session.getInstance().getEnteredRoom().getTrip().getCountry();
		String destCity = Session.getInstance().getEnteredRoom().getTrip().getMainCity();
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getFiltered(poi -> poi.getCountry().equals(destCountry) && poi.getCity().equals(destCity)).stream().map(poi -> new POIBean(poi)).toList();
	}

	private List<POIBean> getPOIFiltered(Rating minRating, List<Tag> tags){
		return getPOIFiltered(tags).stream().filter(poi -> poi.getRating().ordinal() >= minRating.ordinal()).toList();
	}

	private List<POIBean> getPOIFiltered(Rating minRating, Rating maxRating, List<Tag> tags){
		return getPOIFiltered(minRating, tags).stream().filter(poi -> poi.getRating().ordinal() <= maxRating.ordinal()).toList();
	}

	public List<POIBean> getPOIFiltered(POIFilterBean filters){
		return getPOIFiltered(filters.getMinRating(), filters.getMaxRating(), filters.getChosenTags());
	}
	
	public boolean isValidCountry(String country){
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getAll().stream().anyMatch(poi -> poi.getCountry().equals(country));
	}

	public boolean isValidCity(String city){
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getAll().stream().anyMatch(poi -> poi.getCity().equals(city));
	}

    public List<String> getAllCountries() {
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getAll().stream().map(poi -> poi.getCountry()).distinct().toList();
    }

    public List<String> getAllCities(String fromCountry) {
		if(!isValidCountry(fromCountry))
			throw new IllegalArgumentException();
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getAll().stream().map(poi -> poi.getCity()).distinct().toList();
    }
}
