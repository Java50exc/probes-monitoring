package telran.probes;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.probes.dto.Range;
import telran.probes.model.RangeDoc;
import telran.probes.repo.SensorRangeProviderRepo;

@Component
@RequiredArgsConstructor
public class TestDb {
	final SensorRangeProviderRepo sensorRangeProviderRepo;
	
	static final String URL_PATH = "http://localhost:8080/";
	
	static final long ID = 123;
	static final long ID_NOT_EXISTS = 124;
	static final Double MIN_VALUE = 100d;
	static final Double MAX_VALUE = 200d;
	static final Range RANGE = new Range(MIN_VALUE, MAX_VALUE);
	
	static final RangeDoc RANGE_DOC = new RangeDoc(ID, RANGE);
	
	void createDb() {
		sensorRangeProviderRepo.deleteAll();
		sensorRangeProviderRepo.insert(RANGE_DOC);
	}

}
