package telran.probes.exceptions;
import static telran.probes.messages.ErrorMessages.*;

@SuppressWarnings("serial")
public class SensorEmailsIllegalStateException extends IllegalStateException {
	public SensorEmailsIllegalStateException() {
		super(SENSOR_EMAILS_ALREADY_EXISTS);
	}

}
