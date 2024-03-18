package telran.probes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telran.probes.dto.Range;
import telran.probes.model.RangeDoc;
import telran.probes.repo.SensorRangeProviderRepo;
import telran.probes.service.SensorRangeProviderService;

@SpringBootTest
class SensorRangeProviderServiceTests {
	@Autowired
	SensorRangeProviderService sensorRangeProviderService;
	@Autowired
	SensorRangeProviderRepo sensorRangeProviderRepo;
	
	private final long ID_EXISTS = 123;
	private final long ID_NOT_EXISTS = 124;
	private final double MIN_VALUE = 100;
	private final double MAX_VALUE = 200;
	private final Range RANGE = new Range(MIN_VALUE, MAX_VALUE);
	private final RangeDoc RANGE_DOC = new RangeDoc(ID_EXISTS, RANGE);
	
	@BeforeEach
	void setUp() {
		sensorRangeProviderRepo.deleteAll();
		sensorRangeProviderRepo.insert(RANGE_DOC);
	}
	
	@Test
	void getSensorRange_correctFlow_success() {
		assertEquals(RANGE, sensorRangeProviderService.getSensorRange(ID_EXISTS));
	}
	
	@Test
	void getSensorRange_idNotExists_throwsException() {
		assertThrowsExactly(IllegalStateException.class, () -> sensorRangeProviderService.getSensorRange(ID_NOT_EXISTS));
	}

}
