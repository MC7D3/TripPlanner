package tpgroup.model.domain;

import java.util.Arrays;
import java.util.List;

import tpgroup.model.exception.EnumNotFoundException;

public enum Rating{
	ONESTAR("one star", "one", "one stars"),
	TWOSTAR("two star", "two", "two stars"),
	THREESTAR("three star", "three", "three stars"),
	FOURSTAR("four star", "four", "four stars"),
	FIVESTAR("five star", "five", "five stars");

	private final List<String> humanReadableNames;

	Rating(String... humanReadableNames) {
		this.humanReadableNames = Arrays.asList(humanReadableNames);
	}

	public static Rating getRatingFromName(String name) throws EnumNotFoundException{
		String normalized = name.toLowerCase().trim();
		for(Rating rat : values()){
			if(rat.humanReadableNames.contains(normalized)){
				return rat;
			}
		}
		throw new EnumNotFoundException("rating inserito non valido");
	}
}
