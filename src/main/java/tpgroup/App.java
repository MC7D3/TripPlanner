package tpgroup;

import java.io.IOException;

import tpgroup.model.ConfigReader;
import tpgroup.model.exception.InvalidPersistenceTypeException;
import tpgroup.model.exception.InvalidViewTypeException;
import tpgroup.persistence.factory.DAOFactory;
import tpgroup.view.ViewElement;
import tpgroup.view.ViewFactory;

public class App {

	private static void start() {
		try {
			ConfigReader confRd = new ConfigReader("configuration.properties");
			DAOFactory.initDAOFactory(confRd.readPersistenceType());
			ViewElement view = ViewFactory.getInstance().getView(confRd.readViewType());

			do{
				view.show();
			}while(true);

		}catch(IOException | InvalidPersistenceTypeException | InvalidViewTypeException e){
			System.err.println(e.toString());
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		start();
	}
}
