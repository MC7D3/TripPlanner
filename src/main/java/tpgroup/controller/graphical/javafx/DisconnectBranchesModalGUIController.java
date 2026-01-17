package tpgroup.controller.graphical.javafx;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.TripBean;

public class DisconnectBranchesModalGUIController extends FxController {

	@FXML
	private ComboBox<BranchBean> disParentBranchCmBox;

	@FXML
	private ComboBox<BranchBean> disChildBranchCmBox;

	@FXML
	private Text outLogTxt;

	@FXML
	public void initialize() {
		List<BranchBean> disBranches = TripController.getAllBranches();
		disParentBranchCmBox.getItems().addAll(disBranches);
	}

	@FXML
	public void onParentSelected() {
		BranchBean parent = disParentBranchCmBox.getValue();
		if (parent != null) {
			disChildBranchCmBox.getItems().clear();
			TripBean trip = TripController.getTrip();
			Map<BranchBean, Set<BranchBean>> connMapping = trip.getTripGraph().getConnectionsMapping();
			List<BranchBean> connectedBranches = connMapping
					.getOrDefault(parent, java.util.Collections.emptySet())
					.stream().filter(branch -> connMapping.get(branch).isEmpty()).toList();
			
			disChildBranchCmBox.getItems().addAll(connectedBranches);
		}
	}

	@FXML
	public void onDisconnect() {
		BranchBean parent = disParentBranchCmBox.getValue();
		BranchBean child = disChildBranchCmBox.getValue();

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
