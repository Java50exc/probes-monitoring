package telran.probes.service;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.config.ProbesConfig;
import telran.probes.dto.*;
import telran.probes.model.SensorRangeDoc;
import telran.probes.repo.ProbesRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProbesServiceImpl implements ProbesService {
	private final ProbesConfig probesConfig;
	private final ProbesRepo probesRepo;
	private HashMap<Long, Range> cache = new HashMap<>();
	private Long[] sensorIds;



	@Override
	public ProbeData getProbeData() {
		dbCaching();
		int index = ThreadLocalRandom.current().nextInt(sensorIds.length);
		long sensorId = sensorIds[index];

		log.trace("creating probeData with id {}", sensorId);

		ProbeData probeData = getRandomProbeData(cache.get(sensorId), sensorId);
		log.debug("probeData created: {}", probeData);

		return probeData;
	}
	
	private ProbeData getRandomProbeData(Range range, Long sensorId) {
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		double value;
		
		if (rand.nextDouble() <= probesConfig.getDeviationProb()) {
			value = rand.nextDouble() <= probesConfig.getLessProb() ? range.minValue() - rand.nextDouble(100)
					: range.maxValue() + rand.nextDouble(100);
			log.debug("creating probeData with deviation, value: {}", value);
		} else {
			value = rand.nextDouble(range.minValue(), range.maxValue());
			log.debug("creating normal probeData, value: {}", value);
		}
		ProbeData probeData = new ProbeData(sensorId, value, System.currentTimeMillis());
		return probeData;
	}

	@PostConstruct
	private void dbPopulation() {
		long curCount = probesRepo.count();
		if (curCount < probesConfig.getNSensors()) {
			long initialId = probesRepo.findAll().stream().mapToLong(e -> e.getId()).max().orElse(probesConfig.getInitialSensorId());
			insertDoc(probesConfig.getNSensors() - curCount, initialId + 1, probesConfig.getMinValue(), probesConfig.getMaxValue());
		}
	}
	
	private void dbCaching() {
		if (cache.size() < probesRepo.count()) {
			probesRepo.findAll().stream().forEach(e -> cache.put(e.getId(), e.getRange()));
			sensorIds = cache.keySet().toArray(Long[]::new);
		}
	}
	
	private void insertDoc(long docsCount, long initId, double minValue, double maxValue) {
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		if (docsCount > 0) {
			insertDoc(docsCount - 1, initId + 10, minValue, maxValue);
			SensorRangeDoc doc = new SensorRangeDoc(rand.nextLong(initId, initId + 10), getRandomRange(minValue, maxValue));
			probesRepo.save(doc);
			log.debug("sensor range {} with id {}, succesfully saved to db", doc.getRange(), doc.getId());
		}
	}
	
	private Range getRandomRange(double minValue, double maxValue) {
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		double dif = maxValue - minValue;
		double step = dif / 2 - dif / 10;
		minValue -= rand.nextDouble(step) - rand.nextDouble(step);
		maxValue -= rand.nextDouble(step) - rand.nextDouble(step);
		return new Range(minValue, maxValue);	
	}

}
