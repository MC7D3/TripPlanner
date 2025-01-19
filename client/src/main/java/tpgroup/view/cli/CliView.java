package tpgroup.view.cli;

import tpgroup.view.ViewElement;

public class CliView implements ViewElement{
	CliViewState state;

	public CliView() {
		state = new UnloggedMenuState(this);
	}

	@Override
	public void show(){
		state.show();
	}

	public void setState(CliViewState state) {
		this.state = state;
	}
	
}

