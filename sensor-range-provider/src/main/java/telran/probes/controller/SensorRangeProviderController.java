package telran.probes.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.Range;
import telran.probes.service.SensorRangeProviderService;
import static telran.probes.UrlConstants.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SensorRangeProviderController {
	final SensorRangeProviderService rangeProviderService;
		
	@GetMapping(SENSOR_RANGE_PATH + "{sensorId}")
	Range getSensorRange(@PathVariable("sensorId") long sensorId) {
		
		Range range = rangeProviderService.getSensorRange(sensorId);
		log.debug("SensorRangeProviderController: getSensorRange: received range {} for id {}", range, sensorId);
		return range;
		
	}

}
