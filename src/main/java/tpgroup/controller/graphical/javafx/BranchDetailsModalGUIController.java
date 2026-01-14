package tpgroup.controller.graphical.javafx;

import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.EventBean;

public class BranchDetailsModalGUIController extends FxController {

	@FXML
	private Text branchTitleTxt;

	@FXML
	private Label branchInfoTxt;

	@FXML
	private VBox eventsContainer;

	private BranchBean branch;

	public void setBranch(BranchBean branch) {
		this.branch = branch;
		System.out.println(branch);
		loadBranchDetails();
	}

	private void loadBranchDetails() {
		if (branch == null) {
			return;
		}


		branchTitleTxt.setText("Branch " + branch);

		int eventCount = branch.getEvents().size();
		branchInfoTxt.setText(eventCount + " event(s)");

		eventsContainer.getChildren().clear();

		if (branch.getEvents().isEmpty()) {
			Label emptyLabel = new Label("No events in this branch");
			emptyLabel.setStyle("-fx-text-fill: #636e72; -fx-font-style: italic;");
			eventsContainer.getChildren().add(emptyLabel);
			return;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

		for (EventBean event : branch.getEvents()) {
			VBox eventCard = new VBox(6);
			eventCard.setStyle(
					"-fx-background-color: #f8f9fa; -fx-padding: 10; -fx-border-color: #dfe6e9; -fx-border-radius: 4; -fx-background-radius: 4;");

			Label poiLabel = new Label(event.getInfo().getName());
			poiLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
			poiLabel.setWrapText(true);

			Label descLabel = new Label(event.getInfo().getDescription());
			descLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #636e72;");
			descLabel.setWrapText(true);
			descLabel.setMaxWidth(450);

			Label timeLabel = new Label(
					formatter.format(event.getStart()) + " → " + formatter.format(event.getEnd()));
			timeLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #6c5ce7;");

			Label locationLabel = new Label("📍 " + event.getInfo().getCity() + ", " + event.getInfo().getCountry());
			locationLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #636e72;");

			String tagsStr = event.getInfo().getTags().isEmpty() ? "No tags"
					: String.join(", ", event.getInfo().getTags().stream().map(Object::toString).toList());
			Label tagsLabel = new Label("⭐ " + event.getInfo().getRating() + " | " + tagsStr);
			tagsLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #636e72;");

			eventCard.getChildren().addAll(poiLabel, descLabel, timeLabel, locationLabel, tagsLabel);

			VBox.setMargin(eventCard, new Insets(0, 0, 5, 0));

			eventsContainer.getChildren().add(eventCard);
		}
	}

	@FXML
	public void onClose() {
		Stage stage = (Stage) branchTitleTxt.getScene().getWindow();
		stage.close();
	}
}
