package tpgroup.view.cli;

public abstract class CliViewState {
	protected CliViewImpl machine;

	CliViewState(CliViewImpl machine){
		this.machine = machine;
	}

	public abstract void show();

}

