package tpgroup.persistence;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tpgroup.model.ConfigReader;
import tpgroup.model.domain.PointOfInterest;
import tpgroup.model.exception.PropertyNotFoundException;
import tpgroup.persistence.factory.DAOFactory;

/**
 * Idea: guardare la property persistenceType ma anche una nuova property
 * clean_and_reload,
 * se il primo e demo oppure non è demo ma l'altro è true allora procedi facendo
 * opportuni cleanup se richiesti
 */
public class POIDataLoader {

	private POIDataLoader(){
	}

	public static void load(String dataPath) throws IOException {
		ConfigReader reader = new ConfigReader("configuration.properties");
		boolean reloadPOI;
		try {
			reloadPOI = reader.readReloadPOI();
		} catch (PropertyNotFoundException e) {
			reloadPOI = false;
		}

		if (!reloadPOI) {
			return;
		}

		Gson gson = new Gson();
		try(FileReader poiReader = new FileReader(dataPath)){

			Type listType = new TypeToken<List<PointOfInterest>>(){}.getType();

			List<PointOfInterest> parsedPois = gson.fromJson(poiReader, listType);

			DAO<PointOfInterest> poiDao = DAOFactory.getInstance().getDAO(PointOfInterest.class);
			for (PointOfInterest poi : parsedPois) {
				if (reloadPOI) {
					poiDao.save(poi);
				} else {
					poiDao.add(poi);
				}
			}
		}


	}

}
