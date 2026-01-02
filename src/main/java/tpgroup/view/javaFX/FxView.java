package tpgroup.view.javaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tpgroup.view.ViewElement;

public class FxView extends Application implements ViewElement{
	private boolean launched;

	public FxView() {
		super();
		launched = false;
	}

	@Override
	public void show() {
		if(!launched){
			launch();
		}
	}

	@Override
	public void start(Stage mainStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene = new Scene(root);
		mainStage.setTitle("TripPlanner");
        mainStage.setScene(scene);
		mainStage.setOnCloseRequest(event -> System.exit(0));
        mainStage.show();
	}
	
}
