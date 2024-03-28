package telran.probes.exceptions;

import static telran.probes.messages.ErrorMessages.*;

@SuppressWarnings("serial")
public class AccountNotFoundException extends NotFoundException {
	public AccountNotFoundException() {
		super(MISSING_ACCOUNT);
	}

}
