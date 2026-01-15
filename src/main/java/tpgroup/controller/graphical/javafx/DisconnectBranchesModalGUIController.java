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
	private ComboBox<BranchBean> DisParentBranchCmBox;

	@FXML
	private ComboBox<BranchBean> DisChildBranchCmBox;

	@FXML
	private Text outLogTxt;

	@FXML
	public void initialize() {
		List<BranchBean> disBranches = TripController.getBranches();
		DisParentBranchCmBox.getItems().addAll(disBranches);
	}

	@FXML
	public void onParentSelected() {
		BranchBean parent = DisParentBranchCmBox.getValue();
		if (parent != null) {
			DisChildBranchCmBox.getItems().clear();
			TripBean trip = TripController.getTrip();
			List<BranchBean> connectedBranches = trip.getTripGraph().getConnectionsMapping()
					.getOrDefault(parent, java.util.Collections.emptySet())
					.stream().toList();
			
			DisChildBranchCmBox.getItems().addAll(connectedBranches);
		}
	}

	@FXML
	public void onDisconnect() {
		BranchBean parent = DisParentBranchCmBox.getValue();
		BranchBean child = DisChildBranchCmBox.getValue();

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
