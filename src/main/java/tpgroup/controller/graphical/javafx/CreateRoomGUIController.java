package tpgroup.controller.graphical.javafx;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.POIController;
import tpgroup.controller.RoomController;
import tpgroup.model.bean.RoomBean;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.model.exception.RoomGenConflictException;

public class CreateRoomGUIController extends FxController {
	private static final Integer ATTEMPTS = 3;

	private final RoomController roomCtrl = new RoomController();
	private final POIController poiCtrl = new POIController();

	@FXML
	private TextField roomName;

	@FXML
	private ComboBox<String> countryCmBox;

	@FXML
	private ComboBox<String> cityCmBox;

	@FXML
	private Text outLogTxt;

	@FXML
	public void createRoom() {
		boolean success = false;
		for (int attempt = 0; attempt < ATTEMPTS && !success; attempt++) {
			try {
				RoomBean roomBean = new RoomBean(roomName.getText(), countryCmBox.getValue(), cityCmBox.getValue()); 
				if (!poiCtrl.isValidCountry(countryCmBox.getValue())) {
					throw new InvalidBeanParamException("country");
				}
				if (!poiCtrl.isValidCity(cityCmBox.getValue())) {
					throw new InvalidBeanParamException("city");
				}
				roomCtrl.createRoom(roomBean);
				outLogTxt.setText("room created successfully!");
				((Stage) outLogTxt.getScene().getWindow()).close();
				success = true;
			} catch (InvalidBeanParamException e) {
				outLogTxt.setText("ERROR: " + e.getMessage());
				break;
			} catch (RoomGenConflictException e) {
				// it does another attempt, no actions needed
			}
		}
	}

	@FXML
	public void refreshCityCmBox() {
		if (countryCmBox.getValue() != null) {
			cityCmBox.getItems().clear();
			cityCmBox.getItems().addAll(poiCtrl.getAllCities(countryCmBox.getValue()));
		}
	}

	@FXML
	public void initialize() {
		List<String> availableCountries = poiCtrl.getAllCountries();
		countryCmBox.getItems().clear();
		countryCmBox.getItems().addAll(availableCountries);
	}

}
