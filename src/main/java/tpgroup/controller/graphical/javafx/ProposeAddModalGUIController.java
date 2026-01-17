package tpgroup.controller.graphical.javafx;
import java.time.LocalDate;
/*
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.POIController;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.IntervalBean;
import tpgroup.model.bean.POIBean;
import tpgroup.model.bean.POIFilterBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class ProposeAddModalGUIController extends FxController {

	@FXML
	private ComboBox<BranchBean> branchCmBox;

	@FXML
	private ComboBox<String> minRatingCmBox;

	@FXML
	private ComboBox<String> maxRatingCmBox;

	@FXML
	private ComboBox<POIBean> poiCmBox;

	@FXML
	private TextField startTimeTxt;

	@FXML
	private TextField endTimeTxt;

	@FXML
	private Text outLogTxt;

	@FXML
	private CheckBox isFun;

	@FXML
	private CheckBox isCulture;

	@FXML
	private CheckBox isForFamilies;

	@FXML
	private CheckBox isFood;

	@FXML
	private CheckBox isGastronomy;

	@FXML
	private CheckBox isRomantic;

	private List<POIBean> availablePOIs;

	@FXML
	public void initialize() {
		List<BranchBean> branches = TripController.getAllBranches();
		branchCmBox.getItems().addAll(branches);

		minRatingCmBox.getItems().addAll("one star", "two star", "three star", "four star", "five star");
		maxRatingCmBox.getItems().addAll("one star", "two star", "three star", "four star", "five star");

		availablePOIs = POIController.getAllPOI();
		poiCmBox.getItems().addAll(availablePOIs);

		minRatingCmBox.setOnAction(e -> filterPOIs());
		maxRatingCmBox.setOnAction(e -> filterPOIs());
		isFun.setOnAction(e -> filterPOIs());
		isCulture.setOnAction(e -> filterPOIs());
		isForFamilies.setOnAction(e -> filterPOIs());
		isFood.setOnAction(e -> filterPOIs());
		isGastronomy.setOnAction(e -> filterPOIs());
		isRomantic.setOnAction(e -> filterPOIs());

	}

	private void filterPOIs() {
		String minRating = minRatingCmBox.getValue();
		String maxRating = maxRatingCmBox.getValue();

		if(minRating == null || maxRating == null){
			return;
		}

		List<String> tags = new ArrayList<>();

		if(isFun.isSelected()){
			tags.add("fun");
		}
		if(isFood.isSelected()){
			tags.add("food");
		}
		if(isCulture.isSelected()){
			tags.add("culture");
		}
		if(isRomantic.isSelected()){
			tags.add("romantic");
		}
		if(isGastronomy.isSelected()){
			tags.add("gastronomy");
		}
		if(isForFamilies.isSelected()){
			tags.add("families");
		}

		try {
			POIFilterBean filters = new POIFilterBean(minRating, maxRating, tags);
			availablePOIs = POIController.getPOIFiltered(filters);
			poiCmBox.getItems().clear();
			poiCmBox.getItems().addAll(availablePOIs);
		} catch (InvalidBeanParamException e) {
			outLogTxt.setText("Invalid filter values");
		}
	}

	@FXML
	public void onPOISelected() {
		// Optional: Show POI details when selected
	}

	@FXML
	public void onSubmit() {
		BranchBean selectedBranch = branchCmBox.getValue();
		POIBean selectedPOI = poiCmBox.getValue();
		String startTime = startTimeTxt.getText();
		String endTime = endTimeTxt.getText();

		if (selectedBranch == null) {
			outLogTxt.setText("Please select a branch");
			return;
		}

		if (selectedPOI == null) {
			outLogTxt.setText("Please select a POI");
			return;
		}

		if (startTime.isEmpty() || endTime.isEmpty()) {
			outLogTxt.setText("Please provide start and end times");
			return;
		}

		try {
			IntervalBean interval = new IntervalBean(startTime, endTime);
			boolean success = TripController.createAddProposal(selectedBranch, selectedPOI, interval);

			if (success) {
				outLogTxt.setText("Proposal submitted successfully!");
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
*/
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.POIController;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.IntervalBean;
import tpgroup.model.bean.POIBean;
import tpgroup.model.bean.POIFilterBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class ProposeAddModalGUIController extends FxController {

    @FXML
    private ComboBox<BranchBean> branchCmBox;

    @FXML
    private ComboBox<String> minRatingCmBox;

    @FXML
    private ComboBox<String> maxRatingCmBox;

    @FXML
    private ComboBox<POIBean> poiCmBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private Spinner<Integer> startHourSpinner;

    @FXML
    private Spinner<Integer> startMinuteSpinner;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Spinner<Integer> endHourSpinner;

    @FXML
    private Spinner<Integer> endMinuteSpinner;

    @FXML
    private Text outLogTxt;

    @FXML
    private CheckBox isFun;

    @FXML
    private CheckBox isCulture;

    @FXML
    private CheckBox isForFamilies;

    @FXML
    private CheckBox isFood;

    @FXML
    private CheckBox isGastronomy;

    @FXML
    private CheckBox isRomantic;

    private List<POIBean> availablePOIs;

    @FXML
    public void initialize() {
        List<BranchBean> branches = TripController.getAllBranches();
        branchCmBox.getItems().addAll(branches);

        minRatingCmBox.getItems().addAll("one star", "two star", "three star", "four star", "five star");
        maxRatingCmBox.getItems().addAll("one star", "two star", "three star", "four star", "five star");

        availablePOIs = POIController.getAllPOI();
        poiCmBox.getItems().addAll(availablePOIs);

        initializeSpinners();
        
        startDatePicker.setValue(LocalDate.now().plusDays(1));
        endDatePicker.setValue(LocalDate.now().plusDays(1));

        minRatingCmBox.setOnAction(e -> filterPOIs());
        maxRatingCmBox.setOnAction(e -> filterPOIs());
        isFun.setOnAction(e -> filterPOIs());
        isCulture.setOnAction(e -> filterPOIs());
        isForFamilies.setOnAction(e -> filterPOIs());
        isFood.setOnAction(e -> filterPOIs());
        isGastronomy.setOnAction(e -> filterPOIs());
        isRomantic.setOnAction(e -> filterPOIs());
    }

    private void initializeSpinners() {
        SpinnerValueFactory<Integer> startHourFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9);
        startHourSpinner.setValueFactory(startHourFactory);
        
        SpinnerValueFactory<Integer> startMinuteFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 55, 0, 5);
        startMinuteSpinner.setValueFactory(startMinuteFactory);
        
        SpinnerValueFactory<Integer> endHourFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 17);
        endHourSpinner.setValueFactory(endHourFactory);
        
        SpinnerValueFactory<Integer> endMinuteFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 55, 0, 5);
        endMinuteSpinner.setValueFactory(endMinuteFactory);
        
    }

    private void filterPOIs() {
        String minRating = minRatingCmBox.getValue();
        String maxRating = maxRatingCmBox.getValue();

        if(minRating == null || maxRating == null){
            return;
        }

        List<String> tags = new ArrayList<>();

        if(isFun.isSelected()){
            tags.add("fun");
        }
        if(isFood.isSelected()){
            tags.add("food");
        }
        if(isCulture.isSelected()){
            tags.add("culture");
        }
        if(isRomantic.isSelected()){
            tags.add("romantic");
        }
        if(isGastronomy.isSelected()){
            tags.add("gastronomy");
        }
        if(isForFamilies.isSelected()){
            tags.add("families");
        }

        try {
            POIFilterBean filters = new POIFilterBean(minRating, maxRating, tags);
            availablePOIs = POIController.getPOIFiltered(filters);
            poiCmBox.getItems().clear();
            poiCmBox.getItems().addAll(availablePOIs);
        } catch (InvalidBeanParamException e) {
            outLogTxt.setText("Invalid filter values");
        }
    }

    @FXML
    public void onSubmit() {
        BranchBean selectedBranch = branchCmBox.getValue();
        POIBean selectedPOI = poiCmBox.getValue();

        if (selectedBranch == null) {
            outLogTxt.setText("Please select a branch");
            return;
        }

        if (selectedPOI == null) {
            outLogTxt.setText("Please select a POI");
            return;
        }

        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            outLogTxt.setText("Please select start and end dates");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            
            String startTime = LocalDateTime.of(
                startDatePicker.getValue(),
                LocalTime.of(startHourSpinner.getValue(),
                startMinuteSpinner.getValue())
            ).format(formatter);
            
            String endTime = LocalDateTime.of(
                endDatePicker.getValue(),
                LocalTime.of(endHourSpinner.getValue(),
                endMinuteSpinner.getValue())
            ).format(formatter);
            
            IntervalBean interval = new IntervalBean(startTime, endTime);
            
            if (!LocalDateTime.parse(startTime, formatter).isBefore(
                LocalDateTime.parse(endTime, formatter))) {
                outLogTxt.setText("End time must be after start time");
                return;
            }
            
            boolean success = TripController.createAddProposal(selectedBranch, selectedPOI, interval);

            if (success) {
                outLogTxt.setText("Proposal submitted successfully!");
                Stage stage = (Stage) outLogTxt.getScene().getWindow();
                stage.close();
            } else {
                outLogTxt.setText("Failed to create proposal - check for conflicts");
            }
        } catch (InvalidBeanParamException e) {
            outLogTxt.setText("Error: " + e.getMessage());
        } catch (Exception e) {
            outLogTxt.setText("Invalid date/time format. Please check your input.");
        }
    }

    @FXML
    public void onCancel() {
        Stage stage = (Stage) outLogTxt.getScene().getWindow();
        stage.close();
    }
}
