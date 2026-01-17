package tpgroup;

import java.io.IOException;

import tpgroup.model.ConfigReader;
import tpgroup.model.exception.InvalidPersistenceTypeException;
import tpgroup.model.exception.InvalidViewTypeException;
import tpgroup.model.exception.PropertyNotFoundException;
import tpgroup.persistence.MockDataLoader;
import tpgroup.persistence.POIDataLoader;
import tpgroup.persistence.factory.DAOFactory;
import tpgroup.view.ViewElement;
import tpgroup.view.ViewFactory;

public class App {

	private static void start() {
		try {
			ConfigReader confRd = new ConfigReader("configuration.properties");
			DAOFactory.initDAOFactory(confRd.readPersistenceType());
			ViewElement view = ViewFactory.getInstance().getView(confRd.readViewType());
			boolean reloadPOI = confRd.readReloadPOI();
			POIDataLoader.load("rome_poi_data.json", reloadPOI);
			boolean mockData = confRd.readMockData();
			MockDataLoader loader = new MockDataLoader();
			loader.cleanupMockData();
			if (mockData) {
				loader.loadMockData();
			}
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				loader.cleanupMockData();
			}));
			do {
				view.show();
			} while (true);
		} catch (IOException | InvalidPersistenceTypeException | InvalidViewTypeException
				| PropertyNotFoundException e) {
			System.err.println(e.toString());
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		start();
	}
}
