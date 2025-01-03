package tpgroup.view.cli;

public abstract class CliViewState {
	protected CliView machine;

	CliViewState(CliView machine){
		this.machine = machine;
	}

	public abstract void show();

}

