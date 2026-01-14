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

	@FXML
	private TextField roomName;

	@FXML
	private ComboBox<String> countryCmBox;

	@FXML
	private ComboBox<String> cityCmBox;

	@FXML
	private Text outLogTxt;

	@FXML
	public void createRoom(){
		for(int attempt = 0; attempt < ATTEMPTS; attempt++){
			try{
				RoomController.createRoom(new RoomBean(roomName.getText(), countryCmBox.getValue(), cityCmBox.getValue()));
				outLogTxt.setText("room created successfully!");
				((Stage) outLogTxt.getScene().getWindow()).close();
				break;
			}catch(InvalidBeanParamException e){
				outLogTxt.setText("Invalid Room info provided");
				break;
			}catch(RoomGenConflictException e){
				//it does another attempt, no actions needed
			}
		}
	}

	@FXML
	public void refreshCityCmBox(){
		if(countryCmBox.getValue() != null){
			cityCmBox.getItems().clear();
			cityCmBox.getItems().addAll(POIController.getAllCities(countryCmBox.getValue()));
		}
	}

	@FXML
	public void initialize(){
		List<String> availableCountries = POIController.getAllCountries();
		countryCmBox.getItems().clear();
		countryCmBox.getItems().addAll(availableCountries);
	}
	
}
