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
import tpgroup.model.RoomBean;
import tpgroup.model.domain.Room;

public class LoggedMenuGUIController extends FxController {

	@FXML
	private GridPane grid;

	@FXML
	private Text outLogTxt;

	@FXML
	public void joinRoom(){
		open("joinRoomModal.fxml", (Stage) grid.getScene().getWindow());
		buildUI();
	}

	@FXML
	public void createRoom(){
		open("createRoomForm.fxml", (Stage) grid.getScene().getWindow());
	}

	@FXML
	public void options(){
		open("options.fxml", (Stage) grid.getScene().getWindow());
	}

	@FXML
	public void logout(){
		AuthController.logout();
		redirect("login.fxml", (Stage) grid.getScene().getWindow());
	}

	public void handleRoomExit(RoomCardGUIController ctrl){
		RoomController.abbandonRoom(new RoomBean(ctrl.getRoom()));
		buildUI();
	}
	
	@FXML
	public void initialize(){
		buildUI();
	}

	private void buildUI(){
		try {
			grid.getChildren().clear();
			List<Room> rooms = RoomController.getJoinedRooms().stream().map(bean -> bean.getRoom()).toList();
			int xLocation = 0;
			int yLocation = 0;
			for(Room room : rooms){
				FXMLLoader loader = new FXMLLoader(getClass().getResource("roomCard.fxml"));
				Parent newCard = loader.load();
				RoomCardGUIController cardCtrl = loader.getController();
				cardCtrl.getAbandonButton().setOnAction(event -> handleRoomExit(cardCtrl));
				cardCtrl.setRoom(room);
				grid.add(newCard, (xLocation++) % 3, yLocation++);
			}
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	public void setOutLogTxt(String text){
		outLogTxt.setText(text);
	}

	@FXML
	public void refreshUI(){
		buildUI();
		outLogTxt.setText("refresh complete.");
	}
}
