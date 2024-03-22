package telran.probes.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.service.SensorEmailsProviderService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SensorEmailsProviderController {
	final SensorEmailsProviderService sensorEmailsProviderService;
	
	@GetMapping("${app.emails.provider.url}" + "/{sensorId}")
	String[] getSensorEmails(@PathVariable("sensorId") long sensorId) {
		String[] emails = sensorEmailsProviderService.getSensorEmails(sensorId);
		log.debug("SensorEmailsProviderController: received emails {} for id {}", emails, sensorId);
		return emails;
	}

}
