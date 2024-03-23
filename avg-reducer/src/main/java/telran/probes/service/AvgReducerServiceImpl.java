package telran.probes.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;
import telran.probes.model.ProbesList;
import telran.probes.repo.ProbesListRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvgReducerServiceImpl implements AvgReducerService {
	final ProbesListRepo probesListRepo;
	@Value("${app.reducing.size}")
	int reducingSize;

	@Override
	public Double getAvgValue(ProbeData probeData) {
		Double avgValue = null;
		Long sensorId = probeData.id();
		ProbesList probesList = probesListRepo.findById(sensorId).orElse(null);
		
		if (probesList == null || probesList.getValue() == null) {
			probesList = new ProbesList(sensorId);
			log.debug("probesList is null");
		}
		log.debug("received probe data of sensor {} with value {}", probeData.id(), probeData.value());		
		List<Double> listProbeValues = probesList.getValue();
		listProbeValues.add(probeData.value());
		
		if (listProbeValues.size() >= reducingSize) {
			avgValue = listProbeValues.stream().mapToDouble(v -> v).average().getAsDouble();
			log.debug("received average value for id {}, value {}, reducing size {}, current size {}", sensorId, avgValue, reducingSize, listProbeValues.size());
			listProbeValues.clear();
		}
		probesListRepo.save(probesList);
		return avgValue;
	}

}
