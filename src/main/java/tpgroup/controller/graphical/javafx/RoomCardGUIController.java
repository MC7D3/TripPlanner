package tpgroup.controller.graphical.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import tpgroup.model.bean.RoomBean;

public class RoomCardGUIController {

	private RoomBean room;

    @FXML
    private Text roomNameTxt;

    @FXML
    private Text roomMembersTxt;

    @FXML
    private Button abbandonRoomBtn;

    private void setCardData(String name, Integer numMembers) {
        roomNameTxt.setText(name);
        roomMembersTxt.setText(numMembers + "members");
    }

    public Button getAbandonButton() {
        return abbandonRoomBtn;
    }

	public RoomBean getRoom() {
		return room;
	}

	public void setRoom(RoomBean room) {
		this.room = room;
		setCardData(room.getName(), room.getMembers().size());
	}

}
