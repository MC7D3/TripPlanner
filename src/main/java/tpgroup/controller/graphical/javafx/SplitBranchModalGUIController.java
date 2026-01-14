package tpgroup.controller.graphical.javafx;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.EventBean;

public class SplitBranchModalGUIController extends FxController {

	@FXML
	private ComboBox<BranchBean> branchCmBox;

	@FXML
	private ComboBox<EventBean> eventCmBox;

	@FXML
	private Text outLogTxt;

	@FXML
	public void initialize() {
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
	public void onSplit() {
		BranchBean selectedBranch = branchCmBox.getValue();
		EventBean pivotEvent = eventCmBox.getValue();

		if (selectedBranch == null) {
			outLogTxt.setText("Please select a branch");
			return;
		}

		if (pivotEvent == null) {
			outLogTxt.setText("Please select a pivot event");
			return;
		}

		boolean success = TripController.splitBranch(selectedBranch, pivotEvent);

		if (success) {
			outLogTxt.setText("Branch split successfully!");
			Stage stage = (Stage) outLogTxt.getScene().getWindow();
			stage.close();
		} else {
			outLogTxt.setText("Failed to split branch");
		}
	}

	@FXML
	public void onCancel() {
		Stage stage = (Stage) outLogTxt.getScene().getWindow();
		stage.close();
	}
}
