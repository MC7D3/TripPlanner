package tpgroup.controller.graphical.javafx;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tpgroup.controller.TripController;
import tpgroup.model.bean.BranchBean;
import tpgroup.model.bean.EventBean;
import tpgroup.model.bean.IntervalBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class ProposeUpdateModalGUIController extends FxController {

	@FXML
	private ComboBox<BranchBean> branchCmBox;

	@FXML
	private ComboBox<EventBean> eventCmBox;

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

	private final TripController tripCtrl = new TripController();

	@FXML
	public void initialize() {
		// Load branches
		List<BranchBean> branches = tripCtrl.getAllBranches();
		branchCmBox.getItems().addAll(branches);

		initializeSpinners();

		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		};

		startDatePicker.setConverter(converter);
		endDatePicker.setConverter(converter);

		startDatePicker.setValue(LocalDate.now().plusDays(1));
		endDatePicker.setValue(LocalDate.now().plusDays(1));
	}

	@FXML
	public void onEventSelected() {
		EventBean selectedEvent = eventCmBox.getValue();
		if (selectedEvent != null) {
			fillFormWithEventData(selectedEvent);
		}
	}

	private void fillFormWithEventData(EventBean event) {
		LocalDateTime start = event.getStart();
		LocalDateTime end = event.getEnd();

		startDatePicker.setValue(start.toLocalDate());
		startHourSpinner.getValueFactory().setValue(start.getHour());
		startMinuteSpinner.getValueFactory().setValue(start.getMinute());

		endDatePicker.setValue(end.toLocalDate());
		endHourSpinner.getValueFactory().setValue(end.getHour());
		endMinuteSpinner.getValueFactory().setValue(end.getMinute());
	}

	private void initializeSpinners() {
		SpinnerValueFactory<Integer> startHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9);
		startHourSpinner.setValueFactory(startHourFactory);

		SpinnerValueFactory<Integer> startMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 55, 0,
				5);
		startMinuteSpinner.setValueFactory(startMinuteFactory);

		SpinnerValueFactory<Integer> endHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 17);
		endHourSpinner.setValueFactory(endHourFactory);

		SpinnerValueFactory<Integer> endMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 55, 0, 5);
		endMinuteSpinner.setValueFactory(endMinuteFactory);
	}

	@FXML
	public void onBranchSelected() {
		BranchBean selectedBranch = branchCmBox.getValue();
		if (selectedBranch != null) {
			eventCmBox.getItems().clear();
			eventCmBox.getItems().addAll(selectedBranch.getEvents());
		}
	}

	@FXML
	public void onSubmit() {
		BranchBean selectedBranch = branchCmBox.getValue();
		EventBean selectedEvent = eventCmBox.getValue();

		if (selectedBranch == null) {
			outLogTxt.setText("Please select a branch");
			return;
		}

		if (selectedEvent == null) {
			outLogTxt.setText("Please select an event");
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
							startMinuteSpinner.getValue()))
					.format(formatter);

			String endTime = LocalDateTime.of(
					endDatePicker.getValue(),
					LocalTime.of(endHourSpinner.getValue(),
							endMinuteSpinner.getValue()))
					.format(formatter);

			IntervalBean newInterval = new IntervalBean(startTime, endTime);
			boolean success = tripCtrl.createUpdateProposal(selectedBranch, selectedEvent, newInterval);

			if (success) {
				outLogTxt.setText("Update proposal submitted successfully!");
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
