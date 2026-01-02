package tpgroup.controller.graphical.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tpgroup.controller.OptionsController;
import tpgroup.model.PwdBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class OptionsGUIController extends FxController {

	@FXML
	private TextField newPwdTxt;

	@FXML
	private TextField confNewPwdTxt;

	@FXML
	private Text outLogTxt;

	@FXML
	public void updatePassword(){
		try {
			String password = newPwdTxt.getText();
			String confPwd = confNewPwdTxt.getText();
			if(!password.isEmpty() && !confPwd.isEmpty()){
				PwdBean passwordBean = new PwdBean(password, confPwd);
				OptionsController.updatePassword(passwordBean);
				((Stage) newPwdTxt.getScene().getWindow()).close();
				setParentOutTxt("Password Updated Succesfully!");
			}
		} catch (InvalidBeanParamException e) {
			outLogTxt.setText(e.getMessage());
		}

	}

	private void setParentOutTxt(String text){
		Stage modalStage = (Stage) newPwdTxt.getScene().getWindow();
		Object parentCtrl = ((Stage) modalStage.getOwner()).getScene().getRoot().getUserData();
		if(parentCtrl instanceof LoggedMenuGUIController loggedMenuCtrl){
			loggedMenuCtrl.setOutLogTxt(text);
		}
		
	}
	
	@FXML
	public void deleteAccount(){
		open("modalConfirmationDelete.fxml", (Stage) newPwdTxt.getScene().getWindow());
	}
}
