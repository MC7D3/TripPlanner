package tpgroup.controller.graphical.javafx;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.exception.BranchConnectionException;

public class ConnectBranchesModalGUIController extends FxController {

	@FXML
	private ComboBox<BranchBean> parentBranchCmBox;

	@FXML
	private ComboBox<BranchBean> childBranchCmBox;

	@FXML
	private Text outLogTxt;

	@FXML
	public void initialize() {
		List<BranchBean> branches = TripController.getBranches();
		parentBranchCmBox.getItems().addAll(branches);
		childBranchCmBox.getItems().addAll(branches);
	}

	@FXML
	public void onConnect() {
		BranchBean parent = parentBranchCmBox.getValue();
		BranchBean child = childBranchCmBox.getValue();

		if (parent == null || child == null) {
			outLogTxt.setText("Please select both parent and child branches");
			return;
		}

		if (parent.equals(child)) {
			outLogTxt.setText("Cannot connect a branch to itself");
			return;
		}

		try {
			TripController.connectBranches(parent, child);
			outLogTxt.setText("Branches connected successfully!");
			Stage stage = (Stage) outLogTxt.getScene().getWindow();
			stage.close();
		} catch (BranchConnectionException e) {
			outLogTxt.setText("Error: " + e.getMessage());
		}
	}

	@FXML
	public void onCancel() {
		Stage stage = (Stage) outLogTxt.getScene().getWindow();
		stage.close();
	}
}
