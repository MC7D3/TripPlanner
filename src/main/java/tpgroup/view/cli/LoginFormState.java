package tpgroup.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import tpgroup.controller.LoginController;

public class LoginFormState extends CliViewState{
	BufferedReader in;
	
	public LoginFormState(CliViewImpl sm) {
		super(sm);
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	private String pwdRead() throws IOException{ //TODO testing funzionalita
		String password = "";
		for(char pwd_char = (char) in.read(); pwd_char != '\n' && ((int) pwd_char) != -1; pwd_char = (char) in.read()){
			System.out.println("\b*");
			password += pwd_char;
		}
		return password;
	}

	@Override
	public void show() {
		do{
			try {
				System.out.print("email:");
				String email = in.readLine();
				System.out.print("password:");
				String password = pwdRead();
			} catch (IOException e) {
				//TODO: handle exception crea expetion personalizzate;
				System.err.println("ERROR processing login credentials");
				System.exit(-1);
			}
		}while(!LoginController.validateCredentials());
		this.machine.setState(new LoggedMenuState(this.machine));
	}
	
}

