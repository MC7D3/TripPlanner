package tpgroup.model.domain;

import java.util.Arrays;
import java.util.List;

import tpgroup.model.exception.EnumNotFoundException;

public enum Tag {
	FUN("fun"),
	CULTURE("culture"),
	FORFAMILIES("families"),
	FOOD("food"),
	GASTRONOMY("gastronomy"),
	ROMANTIC("romantic");

	private List<String> humanReadableNames;

	private Tag(String... humanReadableNames) {
		this.humanReadableNames = Arrays.asList(humanReadableNames);
	}

	public static Tag getTagFromName(String name) throws EnumNotFoundException{
		for(Tag tag : values()){
			if(tag.humanReadableNames.contains(name))
				return tag;
		}
		throw new EnumNotFoundException("nome del tag incorretto");
	}

}
