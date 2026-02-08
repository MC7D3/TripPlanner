package tpgroup.controller.graphical.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.OptionsController;
import tpgroup.model.bean.UserBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class OptionsGUIController extends FxController {

	@FXML
	private TextField newPwdTxt;

	@FXML
	private TextField confNewPwdTxt;

	@FXML
	private Text outLogTxt;

	private final OptionsController optionsCtrl = new OptionsController();

	@FXML
	public void updatePassword(){
		try {
			String password = newPwdTxt.getText();
			String confPwd = confNewPwdTxt.getText();
			if(!password.isEmpty() && !confPwd.isEmpty()){
				UserBean passwordBean = new UserBean("updatepwd@invalidprovider.com", password, confPwd);
				optionsCtrl.updatePassword(passwordBean);
				Stage optionStage = (Stage) outLogTxt.getScene().getWindow();
				Stage rootStage = (Stage) optionStage.getOwner();
				optionStage.close();
				redirect("login.fxml", rootStage);
			}
		} catch (InvalidBeanParamException e) {
			outLogTxt.setText(e.getMessage());
		}

	}

	@FXML
	public void deleteAccount(){
		open("modalConfirmationDelete.fxml", (Stage) newPwdTxt.getScene().getWindow());
	}

	@FXML
	public void onFocus(KeyEvent event){
		outLogTxt.setText("");
	}
}
