package tpgroup.controller.graphical.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tpgroup.controller.RoomController;

public class RoomDeleteConfModalGUIController extends FxController {

	@FXML
	private Button proceedBtn;

	private final RoomController roomCtrl = new RoomController();

	@FXML
	private void onSubmit() {
		roomCtrl.deleteRoom();
		Stage confStage = (Stage) proceedBtn.getScene().getWindow();
		Stage roomStage = (Stage) confStage.getOwner();
		confStage.close();
		redirect("loggedMenu.fxml", roomStage);
	}

	@FXML
	private void onAbort() {
		Stage confStage = (Stage) proceedBtn.getScene().getWindow();
		confStage.close();
	}

}
