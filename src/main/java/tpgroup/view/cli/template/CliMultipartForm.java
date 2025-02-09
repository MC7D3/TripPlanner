package tpgroup.view.cli.template;

import tpgroup.model.FormData;
import tpgroup.view.cli.CliView;
import tpgroup.view.cli.CliViewState;

public abstract class CliMultipartForm extends CliViewState {
	protected final FormData formData;

	protected CliMultipartForm(CliView sm) {
		super(sm);
		this.formData = new FormData();
	}

	protected CliMultipartForm(CliView sm, FormData previousData){
		super(sm);
		this.formData = previousData;
	}

}
