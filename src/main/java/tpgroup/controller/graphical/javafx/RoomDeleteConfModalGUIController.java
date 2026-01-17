package tpgroup.controller.graphical.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tpgroup.controller.OptionsController;

public class RoomDeleteConfModalGUIController extends FxController{

	@FXML
	private Button proceedBtn;

	@FXML
	private void onSubmit(){
		OptionsController.deleteAccount();
		Stage confStage = (Stage) proceedBtn.getScene().getWindow();
		Stage roomStage = (Stage) confStage.getOwner();
		confStage.close();
		redirect("loggedMenu.fxml", roomStage);
	}

	@FXML
	private void onAbort(){
		Stage confStage = (Stage) proceedBtn.getScene().getWindow();
		confStage.close();
	}
	
}
