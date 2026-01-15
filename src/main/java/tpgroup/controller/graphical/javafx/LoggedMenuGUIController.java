package tpgroup.controller.graphical.javafx;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.AuthController;
import tpgroup.controller.RoomController;
import tpgroup.model.bean.RoomBean;

public class LoggedMenuGUIController extends FxController {

	@FXML
	private GridPane grid;

	@FXML
	private Text outLogTxt;

	@FXML
	public void joinRoom() {
		open("joinRoomModal.fxml", (Stage) grid.getScene().getWindow());
		buildUI();
	}

	@FXML
	public void createRoom() {
		open("createRoomForm.fxml", (Stage) grid.getScene().getWindow());
		buildUI();
	}

	@FXML
	public void options() {
		open("options.fxml", (Stage) grid.getScene().getWindow());
	}

	@FXML
	public void logout() {
		AuthController.logout();
		redirect("login.fxml", (Stage) grid.getScene().getWindow());
	}

	public void handleRoomExit(RoomCardGUIController ctrl) {
		RoomController.abbandonRoom(ctrl.getRoom());
		buildUI();
	}

	@FXML
	public void initialize() {
		buildUI();
	}

	public void buildUI() {
		try {
			grid.getChildren().clear();

			List<RoomBean> rooms = RoomController.getJoinedRooms();

			int columnsPerRow = 3;
			int col = 0;
			int row = 0;
			

			for (RoomBean room : rooms) {

				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/roomCard.fxml"));

				Parent newCard = loader.load();

				RoomCardGUIController cardCtrl = loader.getController();
				cardCtrl.setRoom(room);
				cardCtrl.setParentController(this);

				grid.add(newCard, col, row);

				col++;
				if (col == columnsPerRow) {
					col = 0;
					row++;
				}
			}

		} catch (IOException e) {
			throw new IllegalStateException("Unable to load roomCard.fxml", e);
		}
	}

	public void setOutLogTxt(String text) {
		outLogTxt.setText(text);
	}

	@FXML
	public void refreshUI() {
		buildUI();
		outLogTxt.setText("refresh complete.");
	}
}
