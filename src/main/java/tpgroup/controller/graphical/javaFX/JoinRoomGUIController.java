package tpgroup.controller.graphical.javaFX;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.RoomController;
import tpgroup.model.RoomCodeBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class JoinRoomGUIController extends FxController{
	
	@FXML
	private TextField roomCodeTxt;

	@FXML
	private Text outLogTxt;

	@FXML
	public void joinRoom(){
		try {
			String roomCode = roomCodeTxt.getText();
			if(roomCode.isEmpty()){
				outLogTxt.setText("room's code not provided");
				return;
			}
			if(RoomController.joinRoom(new RoomCodeBean(roomCode))){
				Stage joinRoomStage = (Stage) outLogTxt.getScene().getWindow();
				Stage rootStage = (Stage) joinRoomStage.getOwner();
				joinRoomStage.close();
				redirect("loggedMenu.fxml", rootStage);
				return;
			}
			outLogTxt.setText("no room with such code found");
		} catch (InvalidBeanParamException e) {
			outLogTxt.setText("invalid room code, the format is \"full-room-name-numericCode\"");
		}

	}

	@FXML
	public void onFocus(KeyEvent event){
		outLogTxt.setText("");
	}
}
