package tpgroup.controller.graphical.javafx;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.EventBean;
import tpgroup.model.bean.IntervalBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class ProposeUpdateModalGUIController extends FxController {

	@FXML
	private ComboBox<BranchBean> branchCmBox;

	@FXML
	private ComboBox<EventBean> eventCmBox;

	@FXML
	private TextField newStartTimeTxt;

	@FXML
	private TextField newEndTimeTxt;

	@FXML
	private Text outLogTxt;

	@FXML
	public void initialize() {
		// Load branches
		List<BranchBean> branches = TripController.getAllBranches();
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
		String newStartTime = newStartTimeTxt.getText();
		String newEndTime = newEndTimeTxt.getText();

		if (selectedBranch == null) {
			outLogTxt.setText("Please select a branch");
			return;
		}

		if (selectedEvent == null) {
			outLogTxt.setText("Please select an event");
			return;
		}

		if (newStartTime.isEmpty() || newEndTime.isEmpty()) {
			outLogTxt.setText("Please provide new start and end times");
			return;
		}

		try {
			IntervalBean newInterval = new IntervalBean(newStartTime, newEndTime);
			boolean success = TripController.createUpdateProposal(selectedBranch, selectedEvent, newInterval);

			if (success) {
				outLogTxt.setText("Update proposal submitted successfully!");
				Stage stage = (Stage) outLogTxt.getScene().getWindow();
				stage.close();
			} else {
				outLogTxt.setText("Failed to create proposal - check for conflicts");
			}
		} catch (InvalidBeanParamException e) {
			outLogTxt.setText("Error: " + e.getMessage());
		}
	}

	@FXML
	public void onCancel() {
		Stage stage = (Stage) outLogTxt.getScene().getWindow();
		stage.close();
	}
}
