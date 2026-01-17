package tpgroup.controller.graphical.javafx;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.EventBean;

public class ProposeRemoveModalGUIController extends FxController {

	@FXML
	private ComboBox<BranchBean> delBranchCmBox;

	@FXML
	private ComboBox<EventBean> delEventCmBox;

	@FXML
	private Text outLogTxt;

	@FXML
	public void initialize() {
		List<BranchBean> delBranches = TripController.getAllBranches();
		delBranchCmBox.getItems().addAll(delBranches);
	}

	@FXML
	public void onBranchSelected() {
		BranchBean selectedBranch = delBranchCmBox.getValue();
		if (selectedBranch != null) {
			delEventCmBox.getItems().clear();
			delEventCmBox.getItems().addAll(selectedBranch.getEvents());
		}
	}

	@FXML
	public void onSubmit() {
		BranchBean selectedBranch = delBranchCmBox.getValue();
		EventBean selectedEvent = delEventCmBox.getValue();

		if (selectedBranch == null) {
			outLogTxt.setText("Please select a branch");
			return;
		}

		if (selectedEvent == null) {
			outLogTxt.setText("Please select an event");
			return;
		}

		boolean success = TripController.createRemoveProposal(selectedBranch, selectedEvent);

		if (success) {
			outLogTxt.setText("Removal proposal submitted successfully!");
			Stage stage = (Stage) outLogTxt.getScene().getWindow();
			stage.close();
		} else {
			outLogTxt.setText("Failed to create proposal");
		}
	}

	@FXML
	public void onCancel() {
		Stage stage = (Stage) outLogTxt.getScene().getWindow();
		stage.close();
	}
}
