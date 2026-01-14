package tpgroup.controller.graphical.javafx;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.exception.NodeConflictException;

public class DeleteBranchModalGUIController extends FxController {

	@FXML
	private ComboBox<BranchBean> branchCmBox;

	@FXML
	private Text warningTxt;

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
			List<BranchBean> candidates = TripController.getDeletionCandidates(selectedBranch);
			
			if (candidates.isEmpty()) {
				warningTxt.setText("⚠ This branch has no orphan children that need deletion.");
			} else {
				warningTxt.setText("⚠ Warning: " + candidates.size() + 
					" child branch(es) will also be deleted to prevent orphans.");
			}
		}
	}

	@FXML
	public void onDelete() {
		BranchBean selectedBranch = branchCmBox.getValue();

		if (selectedBranch == null) {
			outLogTxt.setText("Please select a branch");
			return;
		}

		try {
			TripController.removeBranch(selectedBranch);
			outLogTxt.setText("Branch deleted successfully!");
			Stage stage = (Stage) outLogTxt.getScene().getWindow();
			stage.close();
		} catch (NodeConflictException e) {
			outLogTxt.setText("Error: " + e.getMessage());
		}
	}

	@FXML
	public void onCancel() {
		Stage stage = (Stage) outLogTxt.getScene().getWindow();
		stage.close();
	}
}
