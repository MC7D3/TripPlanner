package tpgroup.controller.graphical.javafx;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.bean.ProposalBean;
import tpgroup.model.domain.ProposalType;

public class ViewProposalsModalGUIController extends FxController {

	private static final String LABEL_STYLE = "-fx-font-size: 11; -fx-text-fill: #636e72;";

	@FXML
	private VBox proposalsContainer;

	@FXML
	private CheckBox showMyOnlyChk;

	@FXML
	private Text outLogTxt;

	private final TripController tripCtrl = new TripController();
	private final RoomController roomCtrl = new RoomController();

	@FXML
	public void initialize() {
		loadProposals();
	}

	@FXML
	public void onFilterChanged() {
		loadProposals();
	}

	private void loadProposals() {
		proposalsContainer.getChildren().clear();

		List<ProposalBean> proposals = new ArrayList<>(showMyOnlyChk.isSelected()
				? tripCtrl.getLoggedUserProposals()
				: tripCtrl.getAllProposals());

		if (proposals.isEmpty()) {
			String emptyMessage = showMyOnlyChk.isSelected()
					? "You haven't created any proposals yet"
					: "No proposals yet";
			Label emptyLabel = new Label(emptyMessage);
			emptyLabel.setStyle("-fx-text-fill: #636e72; -fx-font-style: italic;");
			proposalsContainer.getChildren().add(emptyLabel);
			return;
		}

		proposals.sort((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes()));

		for (ProposalBean proposal : proposals) {
			VBox proposalCard = createProposalCard(proposal);
			proposalsContainer.getChildren().add(proposalCard);
		}
	}

	private VBox createProposalCard(ProposalBean proposal) {
		VBox card = new VBox(8);
		card.setStyle(
				"-fx-background-color: #f8f9fa; -fx-padding: 12; -fx-border-color: #dfe6e9; -fx-border-radius: 4; -fx-background-radius: 4;");

		HBox header = new HBox(10);
		Label typeLabel = new Label(proposal.getProposalType().toString());
		typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");

		Label likesLabel = new Label("❤ " + proposal.getLikes() + " likes");
		likesLabel.setStyle("-fx-text-fill: #6c5ce7; -fx-font-weight: bold;");

		header.getChildren().addAll(typeLabel, likesLabel);

		Label creatorLabel = new Label("By: " + proposal.getCreator().getEmail());
		creatorLabel.setStyle(LABEL_STYLE);

		Label eventLabel = new Label("Event: " + proposal.getEvent().getInfo().getName());
		eventLabel.setStyle("-fx-font-size: 12;");
		eventLabel.setWrapText(true);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
		Label startEndLabel = new Label("start: " + formatter.format(proposal.getEvent().getStart()) + " end: "
				+ formatter.format(proposal.getEvent().getEnd()));
		startEndLabel.setStyle(LABEL_STYLE);

		Label newStartEndLabel = null;
		if (proposal.getProposalType().equals(ProposalType.UPDATE) && proposal.getUpdateEvent().isPresent()) {
			newStartEndLabel = new Label("new start: " + formatter.format(proposal.getUpdateEvent().get().getStart()) + " new end: "
					+ formatter.format(proposal.getUpdateEvent().get().getEnd()));
			startEndLabel.setStyle(LABEL_STYLE);
		}
		Label branchLabel = new Label("Branch: " + proposal.getNode());
		branchLabel.setStyle(LABEL_STYLE);


		HBox actionsBox = new HBox(8);
		actionsBox.setStyle("-fx-alignment: center_left;");

		Button likeBtn = new Button("Like");
		likeBtn.setStyle("-fx-font-size: 11; -fx-padding: 4 12;");
		likeBtn.getStyleClass().add("button-primary");
		likeBtn.setOnAction(e -> {
			tripCtrl.likeProposal(proposal);
			loadProposals();
			outLogTxt.setText("Proposal liked/unliked");
		});
		actionsBox.getChildren().add(likeBtn);

		if (roomCtrl.amIAdmin()) {
			Button acceptBtn = new Button("Accept");
			acceptBtn.getStyleClass().add("button-primary");
			acceptBtn.setStyle("-fx-font-size: 11; -fx-padding: 4 12; -fx-background-color: #27ae60;");
			acceptBtn.setOnAction(e -> {
				boolean success = tripCtrl.acceptProposal(proposal);
				if (success) {
					loadProposals();
					outLogTxt.setText("Proposal accepted successfully!");
				} else {
					outLogTxt.setText("Failed to accept proposal");
				}
			});
			actionsBox.getChildren().add(acceptBtn);
		}

		if (tripCtrl.getLoggedUserProposals().contains(proposal)) {
			Region spacer = new Region();
			HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
			actionsBox.getChildren().add(spacer);

			Button removeBtn = new Button("Remove");
			removeBtn.getStyleClass().add("button-cancel");
			removeBtn.setStyle("-fx-font-size: 11; -fx-padding: 4 12;");
			removeBtn.setOnAction(e -> {
				tripCtrl.removeProposal(proposal);
				loadProposals();
				outLogTxt.setText("Proposal removed");
			});
			actionsBox.getChildren().add(removeBtn);
		}

		card.getChildren().addAll(header, creatorLabel, eventLabel, startEndLabel);
		if(newStartEndLabel != null){
			card.getChildren().add(newStartEndLabel);
		}
		card.getChildren().addAll(branchLabel, actionsBox);

		VBox.setMargin(card, new Insets(0, 0, 5, 0));

		return card;
	}

	@FXML
	public void onClose() {
		Stage stage = (Stage) outLogTxt.getScene().getWindow();
		stage.close();
	}
}
