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
	private ComboBox<BranchBean> branchCmBox;

	@FXML
	private ComboBox<EventBean> eventCmBox;

	@FXML
	private Text outLogTxt;

	@FXML
	public void initialize() {
		// Load branches
		List<BranchBean> branches = TripController.getBranches();
		branchCmBox.getItems().addAll(branches);
	}

	@FXML
	public void onBranchSelected() {
		BranchBean selectedBranch = branchCmBox.getValue();
		if (selectedBranch != null) {
			eventCmBox.getItems().clear();
			eventCmBox.getItems().addAll(selectedBranch.getEvents());
		}
	}

	@FXML
	public void onSubmit() {
		BranchBean selectedBranch = branchCmBox.getValue();
		EventBean selectedEvent = eventCmBox.getValue();

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
