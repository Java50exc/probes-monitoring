package telran.probes.service;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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

	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public ProbeData getProbeData() {
		ProbeData probeData = null;
		Range range = null;
		long sensorId = 0;
		dbCaching();
		
		try {
			lock.readLock().lock();
			sensorId = sensorIds[ThreadLocalRandom.current().nextInt(sensorIds.length)];
			range = cache.get(sensorId);
		} finally {
			lock.readLock().unlock();
			log.trace("creating probeData with id {} in range {}", sensorId, range);
			probeData = getRandomProbeData(range, sensorId);
			log.debug("probeData created: {}", probeData);
		}
		return probeData;
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
		try {
			lock.writeLock().lock();
			if (cache.size() < probesRepo.count()) {
				log.warn("adding from db {} items to cache with {} items", probesRepo.count(), cache.size());
				probesRepo.findAll().stream().forEach(e -> cache.put(e.getId(), e.getRange()));
				sensorIds = cache.keySet().toArray(Long[]::new);
			}
		} finally {
			lock.writeLock().unlock();
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

	@Override
	public void updateCache(long id, Range range) {
		try {
			lock.writeLock().lock();
			Range prevRange = cache.put(id, range);
			sensorIds = cache.keySet().toArray(Long[]::new);
			log.warn("Updated range for sensor {}, previous range {}, new range {}", id, prevRange, range);
		} finally {
			lock.writeLock().unlock();
		}
	}

}
