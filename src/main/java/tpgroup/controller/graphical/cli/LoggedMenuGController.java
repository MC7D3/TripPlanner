package tpgroup.controller.graphical.cli;

import java.util.List;

import tpgroup.controller.OptionsController;
import tpgroup.controller.POIController;
import tpgroup.controller.RoomController;
import tpgroup.model.Session;
import tpgroup.model.bean.RoomBean;
import tpgroup.model.bean.UserBean;
import tpgroup.model.exception.InvalidBeanParamException;
import tpgroup.model.exception.RoomGenConflictException;
import tpgroup.view.cli.AbbandonRoomFormState;
import tpgroup.view.cli.DeleteAccountConfForm;
import tpgroup.view.cli.EnterRoomFormState;
import tpgroup.view.cli.JoinRoomFormState;
import tpgroup.view.cli.LoggedMenuState;
import tpgroup.view.cli.NewRoomFormState;
import tpgroup.view.cli.OptionsMenuState;
import tpgroup.view.cli.RoomAdminMenuState;
import tpgroup.view.cli.RoomMemberMenuState;
import tpgroup.view.cli.UnloggedMenuState;
import tpgroup.view.cli.UpdatePwdFormState;
import tpgroup.view.cli.statemachine.CliViewState;

public class LoggedMenuGController {
	private static final String ERROR_PROMPT = "ERROR: ";

	private LoggedMenuGController() {
	}

	public static CliViewState process(String choice) {
		switch (choice) {
			case "create new room":
				return new NewRoomFormState();
			case "join room":
				return new JoinRoomFormState();
			case "enter room":
				return new EnterRoomFormState();
			case "abbandon room":
				return new AbbandonRoomFormState();
			case "options":
				return new OptionsMenuState();
			case "logout":
				Session.resetSession();
				return new UnloggedMenuState();
			default:
				System.exit(0);
				return null;
		}

	}

	private static boolean attemptCreation(RoomBean roomBean, int attempts) throws InvalidBeanParamException{
		for (int attempt = 0; attempt < attempts; attempt++) {
			try {
				RoomController.createRoom(roomBean);
				return true;
			} catch (RoomGenConflictException e) {
				// it does another attempt, no actions needed
			}
		}
		return false;
	}

	public static CliViewState createNewRoom(String name, String country, String city) {
		CliViewState ret = new NewRoomFormState();
		if (name.isEmpty() || country.isEmpty() || city.isEmpty()) {
			return new LoggedMenuState();
		}
		try {
			RoomBean newRoom = new RoomBean(name, country, city);
			boolean created = false;
			if(attemptCreation(newRoom, 3)){
				ret = new RoomAdminMenuState();
				ret.setOutLogTxt("room created successfully!");
				created = true;
			}
			if (!created) {
				ret.setOutLogTxt(ERROR_PROMPT + "too many rooms with this name are present, try another one");
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt(ERROR_PROMPT + e.getMessage());
		}

		return ret;
	}

	public static List<String> getAllCountries() {
		return POIController.getAllCountries();
	}

	public static List<String> getAllCities(String ofCountry) {
		return POIController.getAllCities(ofCountry);
	}

	public static CliViewState joinRoom(String roomCode) {
		CliViewState ret = new JoinRoomFormState();
		if (roomCode.isEmpty()) {
			return new LoggedMenuState();
		}
		try {
			if (RoomController.joinRoom(new RoomBean(roomCode))) {
				ret.setOutLogTxt("room joined successfully!");
				ret = new RoomMemberMenuState();
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt(ERROR_PROMPT + e.getMessage());
		}
		return ret;
	}

	public static CliViewState enterRoom(RoomBean chosen) {
		if (chosen == null) {
			return new LoggedMenuState();
		}
		RoomController.enterRoom(chosen);
		if (RoomController.amIAdmin()) {
			return new RoomAdminMenuState();
		} else {
			return new RoomMemberMenuState();
		}

	}

	public static List<RoomBean> getJoinedRooms() {
		return RoomController.getJoinedRooms();
	}

	public static CliViewState abbandonRoom(RoomBean chosen) {
		if (chosen == null) {
			return new LoggedMenuState();
		}
		RoomController.abbandonRoom(chosen);
		return new LoggedMenuState();
	}

	public static CliViewState processOptionsChoice(String choice) {
		switch (choice) {
			case "change password":
				return new UpdatePwdFormState();
			case "delete account":
				return new DeleteAccountConfForm();
			default:
				return new LoggedMenuState();
		}
	}

	public static CliViewState updatePwd(String password, String confPwd) {
		CliViewState ret = new OptionsMenuState();
		try {
			if (!password.isEmpty() && !confPwd.isEmpty()) {
				OptionsController.updatePassword(new UserBean("updatepwd@invalidprovider.com", password, confPwd));
				ret.setOutLogTxt("password updated succesfully!");
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt(ERROR_PROMPT + e.getMessage());
		}
		return ret;

	}

	public static CliViewState processDeleteRequest(boolean answer) {
		CliViewState ret = new OptionsMenuState();
		if (answer) {
			OptionsController.deleteAccount();
			ret = new UnloggedMenuState();
			ret.setOutLogTxt("account deleted succesfully!");
		}
		return ret;
	}
}
