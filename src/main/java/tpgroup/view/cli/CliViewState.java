package tpgroup.view.cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class CliViewState {
	protected CliView machine;
	protected BufferedReader in;

	public CliViewState(CliView machine){
		this.machine = machine;
		this.in = new BufferedReader(new InputStreamReader(System.in));
	}

	public abstract void show();

}

