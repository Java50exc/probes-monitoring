package telran.probes.controller;

import static telran.probes.messages.ErrorMessages.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.AccountDto;
import telran.probes.service.AccountProviderService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountProviderController {
	final AccountProviderService providerService;
	//grtehrhthrthrhnhnrtn
	
	@GetMapping("${app.account.provider.path}" + "/{email}")
	AccountDto getAccount(@PathVariable("email") @Email(message = WRONG_EMAIL_FORMAT) String email) {
		log.debug("AccountProviderController: getAccount: received {}", email);
		return providerService.getAccount(email);
	}
	
	

}
