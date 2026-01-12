package tpgroup.controller.graphical.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.RoomController;
import tpgroup.model.bean.RoomBean;

public class RoomCardGUIController extends FxController{

	private RoomBean room;
	private LoggedMenuGUIController parentController;

	@FXML
	private Text roomNameTxt;

	@FXML
	private Text roomCodeTxt;

	@FXML
	private Text roomMembersTxt;

	@FXML
	private Text adminStatusTxt;

	@FXML
	private Button enterRoomBtn;

	@FXML
	private Button abbandonRoomBtn;

	public void setRoom(RoomBean room) {
		this.room = room;
		updateCardDisplay();
	}

	public void setParentController(LoggedMenuGUIController parentController) {
		this.parentController = parentController;
	}

	private void updateCardDisplay() {
		if (room == null) {
			return;
		}

		roomNameTxt.setText(room.getName());

		roomCodeTxt.setText("Code: " + room.getCode());

		int memberCount = room.getMembers().size();
		roomMembersTxt.setText(memberCount + " member" + (memberCount != 1 ? "s" : ""));

		if (RoomController.amIAdmin()) {
			adminStatusTxt.setText("(Admin)");
			adminStatusTxt.setVisible(true);
			adminStatusTxt.setManaged(true);
		} else {
			adminStatusTxt.setVisible(false);
			adminStatusTxt.setManaged(false);
		}
	}

	@FXML
	public void onEnterRoom() {
		if (room == null) {
			return;
		}

		if (RoomController.enterRoom(room)) {
			Stage stage = (Stage) roomNameTxt.getScene().getWindow();
			redirect("roomView.fxml", stage);
		} else {
			if (parentController != null) {
				parentController.setOutLogTxt("Error: Unable to enter room. It may no longer exist.");
				parentController.buildUI();
			}
		}
	}

    @FXML
    public void onAbandonRoom() {
        if (room == null || parentController == null) {
            return;
        }

        boolean success = RoomController.abbandonRoom(room);

        if (success) {
            parentController.buildUI();
            parentController.setOutLogTxt("Successfully left room: " + room.getName());
        } else {
            parentController.setOutLogTxt("Error: Unable to leave room.");
        }
    }

	public RoomBean getRoom() {
		return room;
	}

	public LoggedMenuGUIController getParentController() {
		return parentController;
	}

	public Button getAbbandonRoomBtn() {
		return abbandonRoomBtn;
	}

	public Button getEnterButton(){
		return enterRoomBtn;
	}

}
