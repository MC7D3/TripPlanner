package tpgroup.controller.graphical.javafx;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class FxController {

	protected void redirect(String fxmlName, Stage stage) {
		try {
			Parent par = FXMLLoader.load(FxController.class.getResource("/" + fxmlName));
			stage.getScene().setRoot(par);
			stage.sizeToScene();
			stage.show();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	protected void open(String fxmlName, Stage parentStage) {
		try {
			Parent par = FXMLLoader.load(FxController.class.getResource("/" + fxmlName));
			par.setUserData(this);
			Stage newStage = new Stage();
			newStage.setScene(new Scene(par));
			newStage.setTitle(parentStage.getTitle());
			newStage.initModality(Modality.APPLICATION_MODAL);
			newStage.initOwner(parentStage);
			newStage.sizeToScene();
			newStage.showAndWait();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
}
