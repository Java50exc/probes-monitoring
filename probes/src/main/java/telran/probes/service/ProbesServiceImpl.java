package telran.probes.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.config.ProbesConfig;
import telran.probes.dto.*;
import telran.probes.model.SensorEmailsDoc;
import telran.probes.model.SensorRangeDoc;
import telran.probes.repo.EmailsRepo;
import telran.probes.repo.ProbesRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProbesServiceImpl implements ProbesService {
	private final ProbesConfig probesConfig;
	private final ProbesRepo probesRepo;
	private final EmailsRepo emailsRepo;
	private HashMap<Long, Range> cache = new HashMap<>();
	private Long[] sensorIds;

	@Override
	public ProbeData getProbeData() {
		dbCaching();

		long sensorId = sensorIds[ThreadLocalRandom.current().nextInt(sensorIds.length)];
		Range range = cache.get(sensorId);
		log.trace("creating probeData with id {} in range {}", sensorId, range);
		ProbeData probeData = getRandomProbeData(range, sensorId);
		log.debug("probeData created: {}", probeData);
		return probeData;
	}
	
	@Override
	public void updateCache(long id, Range range) {
		Range prevRange = cache.put(id, range);
		sensorIds = cache.keySet().toArray(Long[]::new);
		log.warn("Updated range for sensor {}, previous range {}, new range {}", id, prevRange, range);
	}

	private ProbeData getRandomProbeData(Range range, Long sensorId) {
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		double value;

		if (rand.nextDouble() <= probesConfig.getDeviationProb()) {
			value = rand.nextDouble() <= probesConfig.getLessProb() ? range.minValue() - rand.nextDouble(100)
					: range.maxValue() + rand.nextDouble(100);
			log.warn("creating probeData with deviation, value: {}", value);
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
			long initialId = probesRepo.findAll().stream().mapToLong(e -> e.getId()).max()
					.orElse(probesConfig.getInitialSensorId());
			insertDoc(probesConfig.getNSensors() - curCount, initialId + 1, probesConfig.getMinValue(),
					probesConfig.getMaxValue());
		}
	}

	private void dbCaching() {
		if (cache.size() < probesRepo.count()) {
			log.warn("adding from db {} items to cache with {} items", probesRepo.count(), cache.size());
			probesRepo.findAll().stream().forEach(e -> cache.put(e.getId(), e.getRange()));
			sensorIds = cache.keySet().toArray(Long[]::new);
		}
	}

	private void insertDoc(long docsCount, long initId, double minValue, double maxValue) {
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		if (docsCount > 0) {
			insertDoc(docsCount - 1, initId + 10, minValue, maxValue);
			SensorRangeDoc doc = new SensorRangeDoc(rand.nextLong(initId, initId + 10),
					getRandomRange(minValue, maxValue));

			probesRepo.save(doc);
			log.debug("sensor range {} with id {}, succesfully saved to db", doc.getRange(), doc.getId());
			String email = getEmail(probesConfig.getDefaultEmails()[0], doc.getId());
			SensorEmailsDoc emailsDoc = new SensorEmailsDoc(doc.getId(), new String[] { email });
			emailsRepo.save(emailsDoc);
			log.debug("email {} for sensor {} was succesfully saved to db", Arrays.toString(emailsDoc.getEmails()),
					emailsDoc.getId());
		}
	}

	String getEmail(String initial, long id) {
		String[] fragments = initial.split("@");
		return String.format("%s+%d@%s", fragments[0], id, fragments[1]);
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
