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

	private POIController() {
		super();
	}

	public static List<PointOfInterest> getAllPOI(){
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getAll();
	}

	private static List<POIBean> getPOIFiltered(List<Tag> tags){
		return getPOIFiltered().stream().filter(poi -> poi.getTags().containsAll(tags)).toList();
	}

	private static List<POIBean> getPOIFiltered(){
		String destCountry = Session.getInstance().getEnteredRoom().getTrip().getCountry();
		String destCity = Session.getInstance().getEnteredRoom().getTrip().getMainCity();
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getFiltered(poi -> poi.getCountry().equals(destCountry) && poi.getCity().equals(destCity)).stream().map(poi -> new POIBean(poi)).toList();
	}

	private static List<POIBean> getPOIFiltered(Rating minRating, List<Tag> tags){
		return getPOIFiltered(tags).stream().filter(poi -> poi.getRating().ordinal() >= minRating.ordinal()).toList();
	}

	private static List<POIBean> getPOIFiltered(Rating minRating, Rating maxRating, List<Tag> tags){
		return getPOIFiltered(minRating, tags).stream().filter(poi -> poi.getRating().ordinal() < maxRating.ordinal()).toList();
	}

	public static List<POIBean> getPOIFiltered(POIFilterBean filters){
		return getPOIFiltered(filters.getMinRating(), filters.getMaxRating(), filters.getChosenTags());
	}
	
	public static boolean isValidCountry(String country){
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getAll().stream().anyMatch(poi -> poi.getCountry().equals(country));
	}

	public static boolean isValidCity(String city){
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getAll().stream().anyMatch(poi -> poi.getCity().equals(city));
	}

    public static List<String> getAllCountries() {
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getAll().stream().map(poi -> poi.getCountry()).distinct().toList();
    }

    public static List<String> getAllCities(String fromCountry) {
		if(!isValidCountry(fromCountry))
			throw new IllegalArgumentException();
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getAll().stream().map(poi -> poi.getCountry()).distinct().toList();
    }
}
