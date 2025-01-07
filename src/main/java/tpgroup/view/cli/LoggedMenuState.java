package tpgroup.view.cli;

public class LoggedMenuState extends CliViewState{

	public LoggedMenuState(CliView sm) {
		super(sm);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		System.out.println("im logged great!");
		this.machine.setState(new UnloggedMenuState(this.machine));
	}

}

