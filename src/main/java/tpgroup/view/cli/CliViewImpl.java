package tpgroup.view.cli;

public class CliViewImpl implements CliView{
	CliViewState state;

	public CliViewImpl() {
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

