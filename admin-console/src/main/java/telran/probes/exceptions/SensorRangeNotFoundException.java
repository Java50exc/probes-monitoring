package telran.probes.exceptions;

import static telran.probes.messages.ErrorMessages.*;

@SuppressWarnings("serial")
public class SensorRangeNotFoundException extends NotFoundException {

	public SensorRangeNotFoundException() {
		super(MISSING_RANGE);
	}

}
