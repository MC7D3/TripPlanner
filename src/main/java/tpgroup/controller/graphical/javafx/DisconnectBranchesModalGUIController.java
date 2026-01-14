package tpgroup.controller.graphical.javafx;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.TripBean;

public class DisconnectBranchesModalGUIController extends FxController {

	@FXML
	private ComboBox<BranchBean> parentBranchCmBox;

	@FXML
	private ComboBox<BranchBean> childBranchCmBox;

	@FXML
	private Text outLogTxt;

	@FXML
	public void initialize() {
		// Load all branches
		List<BranchBean> branches = TripController.getBranches();
		parentBranchCmBox.getItems().addAll(branches);
	}

	@FXML
	public void onParentSelected() {
		BranchBean parent = parentBranchCmBox.getValue();
		if (parent != null) {
			childBranchCmBox.getItems().clear();
			
			// Get connected branches from the trip graph
			TripBean trip = TripController.getTrip();
			List<BranchBean> connectedBranches = trip.getTripGraph().getConnectionsMapping()
					.getOrDefault(parent, java.util.Collections.emptySet())
					.stream().toList();
			
			childBranchCmBox.getItems().addAll(connectedBranches);
		}
	}

	@FXML
	public void onDisconnect() {
		BranchBean parent = parentBranchCmBox.getValue();
		BranchBean child = childBranchCmBox.getValue();

		if (parent == null || child == null) {
			outLogTxt.setText("Please select both parent and child branches");
			return;
		}

		TripController.disconnectBranches(parent, child);
		outLogTxt.setText("Branches disconnected successfully!");
		Stage stage = (Stage) outLogTxt.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void onCancel() {
		Stage stage = (Stage) outLogTxt.getScene().getWindow();
		stage.close();
	}
}
