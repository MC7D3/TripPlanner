package tpgroup.view.cli.stateMachine;

import tpgroup.view.ViewElement;
import tpgroup.view.cli.UnloggedMenuState;

public class CliView implements ViewElement{
	CliViewState state;

	public CliView() {
		setState(new UnloggedMenuState());
	}

	@Override
	public void show(){
		state.present();
	}

	public void setState(CliViewState state) {
		state.machine = this;
		this.state = state;
	}

}

