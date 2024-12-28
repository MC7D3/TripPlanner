package tpgroup.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RegistrationFormState extends CliViewState{
	BufferedReader in;
	
	public RegistrationFormState(CliViewImpl sm) {
		super(sm);
		in = new BufferedReader(new InputStreamReader(System.in));
	}

	private String pwdRead() throws IOException{ //TODO testing funzionalita
		StringBuilder password = new StringBuilder(); 
		for(char pwd_char = (char) in.read(); pwd_char != '\n' && ((int) pwd_char) != -1; pwd_char = (char) in.read()){
			System.out.println("\b*");
			password.append(pwd_char);
		}
		return password.toString();
	}

	@Override
	public void show() {
		do{
			try {
				System.out.print("email:");
				String email = in.readLine();
				System.out.print("password:");
				String password = pwdRead();
				System.out.print("confirm password:");
				String confirmPwd = pwdRead();
			} catch (IOException e) {
				//TODO: handle exception crea expetion personalizzate;
				System.err.println("ERROR processing registration credentials");
				System.exit(-1);
			}
		}while(!RegistrationController.validateRegistration());
		this.machine.setState(new LoggedMenu(this.machine));
	}
	
}


