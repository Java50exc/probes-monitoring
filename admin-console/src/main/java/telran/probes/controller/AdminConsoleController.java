package telran.probes.controller;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.*;
import telran.probes.service.AdminConsoleService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminConsoleController {
	final AdminConsoleService adminConsoleService;
	
	
	@PostMapping("${app.admin.console.range.url}")
	SensorRange addSensorRange(@RequestBody @Valid SensorRange sensorRange) {
		SensorRange range = adminConsoleService.addSensorRange(sensorRange);
		log.debug("Controller: range {} successfully added", range);
		return range;
	}
	
	@PostMapping("${app.admin.console.email.url}")
	SensorEmails addSensorEmails(@RequestBody @Valid SensorEmails sensorEmails) {

		log.debug("Controller: emails {} successfully added", sensorEmails);
		SensorEmails emails = adminConsoleService.addSensorEmails(sensorEmails);
		log.debug("Controller: emails {} successfully added", emails);
		return emails;
	}
	
	@PutMapping("${app.admin.console.range.url}")
	SensorRange updateSensorRange(@RequestBody @Valid SensorRange sensorRange) {
		log.debug("Controller: range {} successfully updated", sensorRange);
		SensorRange range = adminConsoleService.updateSensorRange(sensorRange);
		log.debug("Controller: range {} successfully updated", range);
		return range;
	}
	
	@PutMapping("${app.admin.console.email.url}")
	SensorEmails updateSensorEmails(@RequestBody @Valid SensorEmails sensorEmails) {
		SensorEmails emails = adminConsoleService.updateSensorEmails(sensorEmails);
		log.debug("Controller: emails {} successfully updated", emails);
		return emails;
	}

	

}
