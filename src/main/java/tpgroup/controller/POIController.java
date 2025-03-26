package tpgroup.controller;

import java.util.List;

import tpgroup.model.POIFilterBean;
import tpgroup.model.Session;
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

	private static List<PointOfInterest> getPOIFiltered(List<Tag> tags){
		return getPOIFiltered().stream().filter(poi -> poi.getTags().containsAll(tags)).toList();
	}

	private static List<PointOfInterest> getPOIFiltered(){
		String destination = Session.getInstance().getEnteredRoom().getTrip().getDestination();
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getFiltered(poi -> poi.getLocation().equals(destination));
	}

	private static List<PointOfInterest> getPOIFiltered(Rating minRating, List<Tag> tags){
		return getPOIFiltered(tags).stream().filter(poi -> poi.getRating().ordinal() >= minRating.ordinal()).toList();
	}

	private static List<PointOfInterest> getPOIFiltered(Rating minRating, Rating maxRating, List<Tag> tags){
		return getPOIFiltered(minRating, tags).stream().filter(poi -> poi.getRating().ordinal() < maxRating.ordinal()).toList();
	}

	public static List<PointOfInterest> getPOIFiltered(POIFilterBean filters){
		return getPOIFiltered(filters.getMinRating(), filters.getMaxRating(), filters.getChosenTags());
	}
	
	public static boolean isValidDestination(String destination){
		DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
		return poiDao.getAll().stream().anyMatch(poi -> poi.getLocation().equals(destination));
	}
}
