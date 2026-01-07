package tpgroup.model.bean;

import java.util.List;

import tpgroup.model.domain.Rating;
import tpgroup.model.domain.Tag;
import tpgroup.model.exception.EnumNotFoundException;
import tpgroup.model.exception.InvalidBeanParamException;

public class POIFilterBean {
	private final Rating minRating;
	private final Rating maxRating;
	private final List<Tag> chosenTags;

	public POIFilterBean(String minRatingTxt, String maxRatingTxt, List<String> tagsTxt)
			throws InvalidBeanParamException {
		try {
			this.minRating = minRatingTxt.isEmpty()? Rating.ONESTAR : Rating.getRatingFromName(minRatingTxt);
		} catch (EnumNotFoundException e) {
			throw new InvalidBeanParamException("min rating");
		}
		try {
			this.maxRating = maxRatingTxt.isEmpty()? Rating.FIVESTAR : Rating.getRatingFromName(maxRatingTxt);
		} catch (EnumNotFoundException e) {
			throw new InvalidBeanParamException("max rating");
		}
		try{
			this.chosenTags = tagsTxt.stream().map(str -> {
				return Tag.getTagFromName(str);
			}).toList();
		}catch(RuntimeException e){
			throw new InvalidBeanParamException("tags");
		}
	}

	public Rating getMinRating() {
		return minRating;
	}

	public Rating getMaxRating() {
		return maxRating;
	}

	public List<Tag> getChosenTags() {
		return chosenTags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((minRating == null) ? 0 : minRating.hashCode());
		result = prime * result + ((maxRating == null) ? 0 : maxRating.hashCode());
		result = prime * result + ((chosenTags == null) ? 0 : chosenTags.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		POIFilterBean other = (POIFilterBean) obj;
		if (minRating != other.minRating)
			return false;
		if (maxRating != other.maxRating)
			return false;
		if (chosenTags == null) {
			if (other.chosenTags != null)
				return false;
		} else if (!chosenTags.equals(other.chosenTags))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "POIFilterBean{minRating=" + minRating + ", maxRating=" + maxRating + ", chosenTags=" + chosenTags + "}";
	}

}
