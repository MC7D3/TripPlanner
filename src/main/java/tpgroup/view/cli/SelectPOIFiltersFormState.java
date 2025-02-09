package tpgroup.view.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tpgroup.model.FormData;
import tpgroup.model.POIFilterBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class SelectPOIFiltersFormState extends CliViewState{
	private final FormData formData;

	protected SelectPOIFiltersFormState(CliView machine) {
		super(machine);
		this.formData = new FormData();
	}

	protected SelectPOIFiltersFormState(CliView machine, FormData previousData) {
		super(machine);
		this.formData = previousData;
	}

	@Override
	public void show() {
		boolean result;
		do{
			result = false;
			try {
				System.out.println("insert values only if u want to specify that filter, else keep empty");
				System.out.print("min rating filter (one/two/three/four/five stars):");
				String minRatingTxt = in.readLine();
				System.out.print("max rating filter (one/two/three/four/five stars):");
				String maxRatingTxt = in.readLine();
				System.out.println("select tags filters (fun/families/food/culture/gastronomy):");
				List<String> chosenTags = new ArrayList<>();
				while(true){
					String strTag;
					System.out.print(">");
					if((strTag = in.readLine()).isEmpty()){
						break;
					}
					chosenTags.add(strTag);
				}
				formData.put("filtersBean", new POIFilterBean(minRatingTxt, maxRatingTxt, chosenTags));
				result = true;
			} catch (IOException e) {
				System.err.println("ERROR: unable to process inserted credentials");
			} catch (InvalidBeanParamException e){
				System.err.println("ERROR: " + e.getMessage());
				continue;
			} 
		}while(!result);
		this.machine.setState(new SelectPOIFormState(this.machine, formData));
	}
	
}
