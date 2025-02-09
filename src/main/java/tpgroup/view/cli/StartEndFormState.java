package tpgroup.view.cli;

import java.io.IOException;

import tpgroup.model.FormData;
import tpgroup.model.IntervalBean;
import tpgroup.model.exception.InvalidBeanParamException;

public class StartEndFormState extends CliViewState {
	private final FormData formData;

	protected StartEndFormState(CliView sm, FormData previousData) {
		super(sm);
		this.formData = previousData;
	}

	@Override
	public void show() {
		boolean complete = false;
		do {
			try {
				System.out.println(
						"NOTE: u can keep the start time empty, that way the event will put after the last one in the node");
				System.out.print("start time (es 23-01-2025 14:30):");
				String startTimeTxt = in.readLine().trim();
				System.out.print("end time (es 23-01-2025 15:30):");
				String endTimeTxt = in.readLine().trim();
				IntervalBean newInterval = new IntervalBean(startTimeTxt, endTimeTxt);
				formData.put("intervalBean", newInterval);
				complete = true;
			} catch (IOException e) {
				System.err.println("ERROR: unable to process inserted credentials");
			} catch (InvalidBeanParamException e) {
				System.err.println("ERROR: " + e.getMessage()); 
			}
		} while (!complete);

		this.machine.setState(new SubmitAddProposal(this.machine, formData));

	}

}
