package tpgroup.controller.graphical.javaFX;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tpgroup.controller.OptionsController;

public class modalConfDelGUIController extends FxController{

	@FXML
	private Button proceedbBtn;

	@FXML
	private void onSubmit(){
		OptionsController.deleteAccount();
		Stage confStage = (Stage) proceedbBtn.getScene().getWindow();
		Stage optionsStage = (Stage) confStage.getOwner();
		Stage rootStage = (Stage) optionsStage.getOwner();
		confStage.close();
		optionsStage.close();
		redirect("login.fxml", rootStage);
	}

	@FXML
	private void onAbort(){
		Stage confStage = (Stage) proceedbBtn.getScene().getWindow();
		confStage.close();
	}
	
}
