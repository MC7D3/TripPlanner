package tpgroup.controller.graphical.javafx;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;

public class DeleteBranchModalGUIController extends FxController {

	@FXML
	private ComboBox<BranchBean> branchCmBox;

	@FXML
	private Text warningTxt;

	@FXML
	private Text outLogTxt;

	@FXML
	public void initialize() {
		List<BranchBean> branches = TripController.getDeletionCandidates();
		branchCmBox.getItems().addAll(branches);
	}

	@FXML
	public void onDelete() {
		BranchBean selectedBranch = branchCmBox.getValue();

		if (selectedBranch == null) {
			outLogTxt.setText("Please select a branch");
			return;
		}

		TripController.removeBranch(selectedBranch);
		outLogTxt.setText("Branch deleted successfully!");
		Stage stage = (Stage) outLogTxt.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void onCancel() {
		Stage stage = (Stage) outLogTxt.getScene().getWindow();
		stage.close();
	}
}
