package tpgroup.controller.graphical.javafx;

import java.io.IOException;
import java.util.List;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tpgroup.controller.RoomController;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.RoomBean;
import tpgroup.model.bean.StagingBranchBean;
import tpgroup.model.bean.TripBean;

public class RoomViewGUIController extends FxController {

	@FXML
	private Text roomNameTxt;

	@FXML
	private Text roomCodeTxt;

	@FXML
	private Text destinationTxt;

	@FXML
	private Pane graphPane;

	@FXML
	private VBox stagingContainer;

	@FXML
	private HBox adminActionsBox;

	@FXML
	private Text outLogTxt;

	@FXML
	private Button proposeAddBtn;

	@FXML
	private Button proposeRemoveBtn;

	@FXML
	private Button proposeUpdateBtn;

	@FXML
	private Button viewProposalsBtn;

	@FXML
	private Button createBranchBtn;

	@FXML
	private Button connectBranchBtn;

	@FXML
	private Button disconnectBranchBtn;

	@FXML
	private Button splitBranchBtn;

	@FXML
	private Button deleteBranchBtn;

	@FXML
	private Button deleteRoomBtn;

	@FXML
	private Button goBackBtn;

	private SmartGraphPanel<String, String> graphView;
	private Digraph<String, String> graph;

	@FXML
	public void initialize() {

		RoomBean currentRoom = RoomController.getEnteredRoom();
		if (currentRoom != null) {
			roomNameTxt.setText(currentRoom.getName());
			roomCodeTxt.setText("Code: " + currentRoom.getCode());
			destinationTxt.setText("Destination: " + currentRoom.getTrip().getMainCity() + ", "
					+ currentRoom.getTrip().getCountry());
		}

		boolean isAdmin = RoomController.amIAdmin();
		adminActionsBox.setVisible(isAdmin);
		adminActionsBox.setManaged(isAdmin);

		loadStagingArea();

		initializeGraph();
	}

	private void initializeGraph() {
		TripBean currentTrip = TripController.getTrip();

		graph = new DigraphEdgeList<>();

		for (BranchBean branch : currentTrip.getTripGraph().getNodes()) {
			String nodeId = branch.getShortId();
			graph.insertVertex(nodeId);
		}

		TripController.getTrip().getTripGraph().getConnectionsMapping().forEach((parent, children) -> {
			String parentId = parent.getShortId();
			children.forEach(child -> {
				String childId = child.getShortId();
				String edgeId = parentId + "->" + childId;
				graph.insertEdge(parentId, childId, edgeId);
			});
		});

		SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
		graphView = new SmartGraphPanel<>(graph, strategy);
		graphView.getStylableVertex(currentTrip.getTripGraph().getRoot().getShortId()).setStyleClass("myVertex");

		graphView.setAutomaticLayout(true);

		graphView.setVertexDoubleClickAction(vertex -> {
			String nodeId = vertex.getUnderlyingVertex().element();
			BranchBean selectedBranch = TripController.getAllBranches().stream()
					.filter(b -> b.getId().toString().startsWith(nodeId))
					.findFirst().get();
			outLogTxt.setText("Selected branch: " + selectedBranch);
			showBranchDetails(selectedBranch);
		});

		graphPane.getChildren().clear();
		graphPane.getChildren().add(graphView);

		graphView.prefWidthProperty().bind(graphPane.widthProperty());
		graphView.prefHeightProperty().bind(graphPane.heightProperty());

		graphPane.applyCss();
		graphPane.layout();
		Platform.runLater(() -> {
			Platform.runLater(() -> {
				graphView.init();
			});
		});
	}

	private void loadStagingArea() {
		stagingContainer.getChildren().clear();

		List<StagingBranchBean> stagingBranches = TripController.getStagingBranches();

		if (stagingBranches.isEmpty()) {
			Label emptyLabel = new Label("No staging branches");
			emptyLabel.setStyle("-fx-text-fill: #636e72; -fx-font-style: italic;");
			stagingContainer.getChildren().add(emptyLabel);
			return;
		}

		for (BranchBean branch : stagingBranches) {
			VBox branchCard = createStagingBranchCard(branch);
			stagingContainer.getChildren().add(branchCard);
		}
	}

	private VBox createStagingBranchCard(BranchBean branch) {
		VBox card = new VBox(5);
		card.setStyle(
				"-fx-background-color: #f8f9fa; -fx-padding: 8; -fx-border-color: #dfe6e9; -fx-border-radius: 4; -fx-background-radius: 4;");

		Label idLabel = new Label("Branch " + branch.getShortId());
		idLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 11;");

		Label eventsLabel = new Label(branch.getEvents().size() + " events");
		eventsLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #636e72;");

		Button viewBtn = new Button("View");
		viewBtn.setStyle("-fx-font-size: 10; -fx-padding: 3 8;");
		viewBtn.setOnAction(e -> {
			showBranchDetails(branch);
		});

		card.getChildren().addAll(idLabel, eventsLabel, viewBtn);
		return card;
	}

	private void showBranchDetails(BranchBean branch) {
		try {
			Stage parentStage = (Stage) graphPane.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(FxController.class.getResource("/branchDetailsModal.fxml"));
			Parent par = loader.load();
			par.setUserData(this);
			Stage newStage = new Stage();
			newStage.setScene(new Scene(par));
			newStage.setTitle(parentStage.getTitle());
			newStage.initModality(Modality.APPLICATION_MODAL);
			newStage.initOwner(parentStage);
			newStage.sizeToScene();
			BranchDetailsModalGUIController controller = loader.getController();
			controller.setBranch(branch);
			newStage.showAndWait();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@FXML
	public void onProposeAdd() {
		open("proposeAddModal.fxml", (Stage) graphPane.getScene().getWindow());
		refreshGraph();
	}

	@FXML
	public void onProposeRemove() {
		open("proposeRemoveModal.fxml", (Stage) graphPane.getScene().getWindow());
		refreshGraph();
	}

	@FXML
	public void onProposeUpdate() {
		open("proposeUpdateModal.fxml", (Stage) graphPane.getScene().getWindow());
		refreshGraph();
	}

	@FXML
	public void onViewProposals() {
		open("viewProposalsModal.fxml", (Stage) graphPane.getScene().getWindow());
		refreshGraph();
	}

	@FXML
	public void onCreateBranch() {
		try {
			TripController.createBranch();
			outLogTxt.setText("Branch created successfully!");
			refreshGraph();
		} catch (Exception e) {
			outLogTxt.setText("Error: " + e.getMessage());
		}
	}

	@FXML
	public void onConnectBranches() {
		open("connectBranchesModal.fxml", (Stage) graphPane.getScene().getWindow());
		refreshGraph();
	}

	@FXML
	public void onDisconnectBranches() {
		open("disconnectBranchesModal.fxml", (Stage) graphPane.getScene().getWindow());
		refreshGraph();
	}

	@FXML
	public void onSplitBranch() {
		open("splitBranchModal.fxml", (Stage) graphPane.getScene().getWindow());
		refreshGraph();
	}

	@FXML
	public void onDeleteBranch() {
		open("deleteBranchModal.fxml", (Stage) graphPane.getScene().getWindow());
		refreshGraph();
	}

	@FXML
	public void onDeleteRoom() {
		open("roomDeleteConfModal.fxml", (Stage) graphPane.getScene().getWindow());
	}

	@FXML
	public void onGoBack() {
		redirect("loggedMenu.fxml", (Stage) graphPane.getScene().getWindow());
	}

	@FXML
	private void refreshGraph() {
		initialize();
	}

	public void setOutLogTxt(String message) {
		outLogTxt.setText(message);
	}
}
