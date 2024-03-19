package telran.probes.exceptions;

import static telran.probes.messages.ErrorMessages.*;

@SuppressWarnings("serial")
public class SensorRangeIllegalStateException extends IllegalStateException {
	public SensorRangeIllegalStateException() {
		super(SENSOR_RANGE_ALREADY_EXISTS);
	}

}
