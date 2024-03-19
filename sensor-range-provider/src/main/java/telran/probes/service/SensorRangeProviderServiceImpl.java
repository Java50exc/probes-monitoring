package telran.probes.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.Range;
import telran.probes.model.RangeDoc;
import telran.probes.repo.SensorRangeProviderRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorRangeProviderServiceImpl implements SensorRangeProviderService {
	final SensorRangeProviderRepo sensorRangeProviderRepo;

	@Override
	public Range getSensorRange(long sensorId) {
		//FIXME add proper exception (update tests)
		RangeDoc rangeDoc = sensorRangeProviderRepo.findById(sensorId).orElseThrow(() -> new IllegalStateException("Range not found"));
		log.debug("found range {} for id {}", rangeDoc, sensorId);
		return rangeDoc.getRange();
	}

}
