package tpgroup;

import tpgroup.view.cli.CliView;
import tpgroup.view.cli.CliViewImpl;

public class App {

	private static void start(){
		Boolean res;
		//TODO astrarre il concetto di view in modo da modificarlo parametricamente
		CliView view = new CliViewImpl();
		do{
			res = view.show();
		}while(res);
	}

	public static void main(String[] args) {
		start();
	}
}
