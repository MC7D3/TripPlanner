package tpgroup.model.domain;

import java.util.Arrays;
import java.util.List;

import tpgroup.model.exception.EnumNotFoundException;

public enum Rating{
	ONESTAR("one star", "one"),
	TWOSTAR("two star", "two"),
	THREESTAR("three star", "tree"),
	FOURSTAR("four star", "four"),
	FIVESTAR("five star", "five");

	private final List<String> humanReadableNames;

	Rating(String... humanReadableNames) {
		this.humanReadableNames = Arrays.asList(humanReadableNames);
	}

	public static Rating getRatingFromName(String name) throws EnumNotFoundException{
		for(Rating rat : values()){
			if(rat.humanReadableNames.contains(name)){
				return rat;
			}
		}
		throw new EnumNotFoundException("rating inserito non valido");
	}
}
