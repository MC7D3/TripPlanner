package tpgroup.view.cli.statemachine;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class CliViewState {
	protected CliView machine;
	protected BufferedReader in;
	protected String outLogTxt;

	protected CliViewState(){
		this.in = new BufferedReader(new InputStreamReader(System.in));
	}

	public void show(){
		if(this.outLogTxt != null && !this.outLogTxt.isEmpty()){
			System.out.println(this.outLogTxt);
			this.outLogTxt = "";
		}
		present();
	}

	public abstract void present();

	public void setOutLogTxt(String msg){
		this.outLogTxt = msg;
	}

}

