package tpgroup.view;

import tpgroup.model.exception.InvalidViewTypeException;
import tpgroup.view.cli.statemachine.CliView;
import tpgroup.view.javafx.FxView;

public class ViewFactory {
	private static ViewFactory instance = null;

	private ViewFactory() {
		super();
	}

	public ViewElement getView(ViewType type) throws InvalidViewTypeException{
		switch(type){
			case CLI:
				return new CliView();
			case GUI:
				return new FxView();
			default:
				throw new InvalidViewTypeException();
		}
	}

	public static ViewFactory getInstance(){
		if(instance == null) instance = new ViewFactory();
		return instance;
	}

}
