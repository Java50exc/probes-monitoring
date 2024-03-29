package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.probes.exceptions.SensorEmailsNotFoundException;
import telran.probes.repo.SensorEmailsProviderRepo;
import telran.probes.service.SensorEmailsProviderService;

@SpringBootTest
public class SensorEmailsProviderServiceTests {	
	@Autowired
	SensorEmailsProviderService sensorEmailsProviderService;
	@Autowired
	SensorEmailsProviderRepo sensorEmailsProviderRepo;
	@Autowired
	TestDb testDb;
	
	
	@BeforeEach
	void setUp() {
		testDb.createDb();
	}
	
	@Test
	void getSensorEmails_correctFlow_success() {
		assertArrayEquals(TestDb.EMAILS, sensorEmailsProviderService.getSensorEmails(TestDb.ID));
	}
	
	@Test
	void getSensorEmails_emailsNotExists_throwsException() {
		assertThrowsExactly(SensorEmailsNotFoundException.class, () -> sensorEmailsProviderService.getSensorEmails(TestDb.ID_NOT_EXISTS));
	}

}
