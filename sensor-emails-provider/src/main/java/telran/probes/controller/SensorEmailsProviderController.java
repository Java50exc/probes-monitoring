package telran.probes.controller;

import static telran.probes.UrlConstants.*;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.service.SensorEmailsProviderService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SensorEmailsProviderController {
	final SensorEmailsProviderService sensorEmailsProviderService;
	
	@GetMapping(EMAILS_PATH + "{sensorId}")
	//FIXME app.emails.provider.url
	String[] getSensorEmails(@PathVariable("sensorId") long sensorId) {
		String[] emails = sensorEmailsProviderService.getSensorEmails(sensorId);
		log.debug("SensorEmailsProviderController: received emails {} for id {}", emails, sensorId);
		return emails;
	}

}
