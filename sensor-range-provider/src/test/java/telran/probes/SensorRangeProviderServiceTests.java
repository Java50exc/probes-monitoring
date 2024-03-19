package telran.probes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telran.probes.repo.SensorRangeProviderRepo;
import telran.probes.service.SensorRangeProviderService;

@SpringBootTest
class SensorRangeProviderServiceTests {
	@Autowired
	SensorRangeProviderService sensorRangeProviderService;
	@Autowired
	SensorRangeProviderRepo sensorRangeProviderRepo;
	@Autowired
	TestDb testDb;
	
	@BeforeEach
	void setUp() {
		testDb.createDb();
	}
	
	@Test
	void getSensorRange_correctFlow_success() {
		assertEquals(TestDb.RANGE, sensorRangeProviderService.getSensorRange(TestDb.ID));
	}
	
	@Test
	void getSensorRange_idNotExists_throwsException() {
		assertThrowsExactly(IllegalStateException.class, () -> sensorRangeProviderService.getSensorRange(TestDb.ID_NOT_EXISTS));
	}

}
