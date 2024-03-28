package telran.probes.controller;

import static telran.probes.messages.ErrorMessages.EMAIL_REGEX;
import static telran.probes.messages.ErrorMessages.INVALID_EMAIL_FORMAT;
import static telran.probes.messages.ErrorMessages.RANGE_MIN_GREATER_THEN_MAX;

import java.util.Arrays;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.*;
import telran.probes.exceptions.SensorEmailsIllegalStateException;
import telran.probes.exceptions.SensorRangeIllegalStateException;
import telran.probes.service.AdminConsoleService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminConsoleController {
	final AdminConsoleService adminConsoleService;
	
	@PostMapping("${app.admin.console.range.url}")
	SensorRange addSensorRange(@RequestBody @Valid SensorRange sensorRange) {
		log.debug("AdminConsoleController: addSensorRange: received {}", sensorRange);
		rangeValidation(sensorRange.range());
		SensorRange range = adminConsoleService.addSensorRange(sensorRange);
		log.debug("AdminConsoleController: addSensorRange: range {} successfully added", range);
		return range;
	}
	
	@PostMapping("${app.admin.console.email.url}")
	SensorEmails addSensorEmails(@RequestBody @Valid SensorEmails sensorEmails) {
		log.debug("AdminConsoleController: addSensorEmails: received {}", sensorEmails);
		emailsValidation(sensorEmails.mails());
		SensorEmails emails = adminConsoleService.addSensorEmails(sensorEmails);
		log.debug("AdminConsoleController: addSensorEmails: emails {} successfully added", emails);
		return emails;
	}
	
	@PutMapping("${app.admin.console.range.url}")
	SensorRange updateSensorRange(@RequestBody @Valid SensorRange sensorRange) {
		log.debug("AdminConsoleController: updateSensorRange: received {}", sensorRange);
		rangeValidation(sensorRange.range());
		SensorRange range = adminConsoleService.updateSensorRange(sensorRange);
		log.debug("AdminConsoleController: updateSensorRange: range {} successfully updated", range);
		return range;
	}
	
	@PutMapping("${app.admin.console.email.url}")
	SensorEmails updateSensorEmails(@RequestBody @Valid SensorEmails sensorEmails) {
		log.debug("AdminConsoleController: updateSensorEmails: received {}", sensorEmails);
		emailsValidation(sensorEmails.mails());
		SensorEmails emails = adminConsoleService.updateSensorEmails(sensorEmails);
		log.debug("AdminConsoleController: updateSensorEmails: emails {} successfully updated", emails);
		return emails;
	}
	
	private void rangeValidation(Range range) {
		if (range.maxValue() < range.minValue()) {
			throw new SensorRangeIllegalStateException(RANGE_MIN_GREATER_THEN_MAX);
		}
	}
	
	private void emailsValidation(String[] emails) { 
       Arrays.stream(emails).forEach(e -> {
    	   if (!e.matches(EMAIL_REGEX)) {
    		   throw new SensorEmailsIllegalStateException(INVALID_EMAIL_FORMAT + e);
    	   }
       });
	}

	

}
