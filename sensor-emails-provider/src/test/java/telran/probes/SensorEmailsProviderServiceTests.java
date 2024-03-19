package telran.probes;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
		assertThrowsExactly(IllegalStateException.class, () -> sensorEmailsProviderService.getSensorEmails(TestDb.ID_NOT_EXISTS));
	}

}
