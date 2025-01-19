package tpgroup.view;

import tpgroup.view.cli.CliView;
import tpgroup.model.exception.InvalidViewTypeException;

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
				//TODO javafx view;
			default:
				throw new InvalidViewTypeException();
		}
	}

	public static ViewFactory getInstance(){
		if(instance == null) instance = new ViewFactory();
		return instance;
	}

}
