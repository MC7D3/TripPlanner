package tpgroup.controller.graphical.cli;

import java.util.List;
import java.util.stream.Collectors;

import tpgroup.controller.OptionsController;
import tpgroup.controller.POIController;
import tpgroup.controller.RoomController;
import tpgroup.model.PwdBean;
import tpgroup.model.RoomBean;
import tpgroup.model.Session;
import tpgroup.model.domain.Room;
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
import tpgroup.view.cli.stateMachine.CliViewState;

public class LoggedMenuGController {

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
			case "logout": {
				Session.resetSession();
				new UnloggedMenuState();
			}
			default:
				System.exit(0);
				return null;
		}

	}

	public static CliViewState createNewRoom(String name, String country, String city) {
		CliViewState ret = new NewRoomFormState();
		if (name.isEmpty() || country.isEmpty() || city.isEmpty()) {
			return new LoggedMenuState();
		}
		try {
			RoomBean newRoom = new RoomBean(name, country, city);
			boolean created = false;
			for (int attempt = 0; attempt < 3; attempt++) {
				try {
					RoomController.createRoom(newRoom);
					ret = new RoomAdminMenuState();
					ret.setOutLogTxt("room created successfully!");
					created = true;
					break;
				} catch (RoomGenConflictException e) {
					// it does another attempt, no actions needed
				}
			}
			if (!created) {
				ret.setOutLogTxt("ERROR: too many rooms with this name are present, try another one");
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt("ERROR: " + e.getMessage());
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
			return new UnloggedMenuState();
		}
		try {
			if (RoomController.joinRoom(new RoomBean(roomCode))) {
				System.out.println("room joined successfully!");
				ret = new RoomMemberMenuState();
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt("ERROR: " + e.getMessage());
		}
		return ret;
	}

	public static CliViewState enterRoom(Room chosen) {
		if (chosen == null) {
			return new LoggedMenuState();
		}
		RoomController.enterRoom(new RoomBean(chosen));
		if (RoomController.amIAdmin()) {
			return new RoomAdminMenuState();
		} else {
			return new RoomMemberMenuState();
		}

	}

	public static List<Room> getJoinedRooms() {
		return RoomController.getJoinedRooms().stream().map(bean -> bean.getRoom()).collect(Collectors.toList());
	}

	public static CliViewState abbandonRoom(Room chosen) {
		if (chosen == null) {
			return new LoggedMenuState();
		}
		RoomController.abbandonRoom(new RoomBean(chosen));
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
				PwdBean passwordBean = new PwdBean(password, confPwd);
				OptionsController.updatePassword(passwordBean);
				ret.setOutLogTxt("password updated succesfully!");
			}
		} catch (InvalidBeanParamException e) {
			ret.setOutLogTxt("ERROR: " + e.getMessage());
		}
		return ret;

	}

    public static CliViewState processDeleteRequest(boolean answer) {
		CliViewState ret = new OptionsMenuState();
		if(answer){
			OptionsController.deleteAccount();
			ret = new UnloggedMenuState();
			ret.setOutLogTxt("account deleted succesfully!");
		}
		return ret;
    }
}
