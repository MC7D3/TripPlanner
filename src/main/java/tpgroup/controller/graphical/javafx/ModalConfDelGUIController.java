package tpgroup.controller.graphical.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tpgroup.controller.OptionsController;

public class ModalConfDelGUIController extends FxController{

	@FXML
	private Button proceedBtn;

	private final OptionsController optionsCtrl = new OptionsController();

	@FXML
	private void onSubmit(){
		optionsCtrl.deleteAccount();
		Stage confStage = (Stage) proceedBtn.getScene().getWindow();
		Stage optionsStage = (Stage) confStage.getOwner();
		Stage rootStage = (Stage) optionsStage.getOwner();
		confStage.close();
		optionsStage.close();
		redirect("login.fxml", rootStage);
	}

	@FXML
	private void onAbort(){
		Stage confStage = (Stage) proceedBtn.getScene().getWindow();
		confStage.close();
	}
	
}
