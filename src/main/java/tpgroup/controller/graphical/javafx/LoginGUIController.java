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

public class LoginGUIController extends FxController {

	@FXML
	private TextField emailTxt;

	@FXML
	private PasswordField pwdTxt;

	@FXML
	private Text outLogTxt;

	@FXML
	private Button loginBtn;

	private final AuthController authCtrl = new AuthController();

	@FXML
	public void onLogin() {
		try {
			String email = emailTxt.getText();
			String pwd = pwdTxt.getText();
			if (email.isEmpty() || pwd.isEmpty()) {
				outLogTxt.setText("all the fields are required");
				return;
			}
			if (authCtrl.validateCredentials(new UserBean(email, pwd))) {
				redirect("loggedMenu.fxml", (Stage) outLogTxt.getScene().getWindow());
				return;
			}
			outLogTxt.setText("invalid email or password provided");
		} catch (InvalidBeanParamException e) {
			outLogTxt.setText("malformed email or password");
		}
	}

	@FXML
	public void redirectRegistration() {
		redirect("registration.fxml", (Stage) outLogTxt.getScene().getWindow());
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
					loginBtn.fire();
					event.consume();
				}
			});
		});
	}
}
