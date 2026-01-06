package tpgroup.controller.graphical.javafx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.AuthController;
import tpgroup.model.bean.UserBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class RegistrationGUIController extends FxController {

	@FXML
	private TextField emailTxt;

	@FXML
	private PasswordField pwdTxt;

	@FXML
	private PasswordField confPwdTxt;

	@FXML
	private Text outLogTxt;

	@FXML
	private Button registerBtn;

	@FXML
	public void onRegistration() {
		try {
			if (AuthController.executeRegistration(new UserBean(emailTxt.getText(), pwdTxt.getText(), confPwdTxt.getText()))) {
				redirect("loggedMenu.fxml", (Stage) outLogTxt.getScene().getWindow());
			} else {
				outLogTxt.setText("the account already exists");
			}
		} catch (InvalidBeanParamException e) {
			outLogTxt.setText(e.getMessage());
		}
	}

	@FXML
	public void redirectLogin() {
		redirect("login.fxml", (Stage) outLogTxt.getScene().getWindow());
	}

	@FXML
	public void onFocus(KeyEvent event) {
		outLogTxt.setText("");
	}

	public void initialize() {
		Platform.runLater(() -> {
			Scene curScene = outLogTxt.getScene();
			curScene.setOnKeyPressed(event -> {
				if (event.getCode() == KeyCode.ENTER) {
					registerBtn.fire();
					event.consume();
				}
			});
		});
	}
}
