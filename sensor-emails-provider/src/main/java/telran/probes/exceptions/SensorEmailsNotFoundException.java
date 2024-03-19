package telran.probes.exceptions;

import static telran.probes.messages.ErrorMessages.*;

@SuppressWarnings("serial")
public class SensorEmailsNotFoundException extends NotFoundException {

	public SensorEmailsNotFoundException() {
		super(MISSING_EMAILS);
	}

}
