package telran.probes;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;
import static telran.probes.TestDb.*;
import telran.probes.dto.SensorUpdateData;
import telran.probes.exceptions.*;
import telran.probes.model.*;
import telran.probes.service.AdminConsoleService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class AdminConsoleServiceTests {
	@Autowired
	AdminConsoleService adminConsoleService;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	TestDb testDb;
	@Autowired
	OutputDestination consumer;
	@Autowired
	ObjectMapper mapper;

	private String producerBindingName = "adminConsole-out-0";

	@BeforeEach
	void setUp() {
		testDb.createDb();
	}

	@Test
	void addSensorRange_correctFlow_success() {
		assertNull(mongoTemplate.findById(ID_NOT_EXISTS, RangeDoc.class, RANGE_COLLECTION));
		assertEquals(SENSOR_RANGE_NOT_EXISTS, adminConsoleService.addSensorRange(SENSOR_RANGE_NOT_EXISTS));
		assertEquals(SENSOR_RANGE_NOT_EXISTS,
				mongoTemplate.findById(ID_NOT_EXISTS, RangeDoc.class, RANGE_COLLECTION).build());
	}

	@Test
	void addSensorEmails_correctFlow_success() {
		assertNull(mongoTemplate.findById(ID_NOT_EXISTS, SensorEmailsDoc.class, EMAIL_COLLECTION));
		assertEquals(SENSOR_EMAILS_NOT_EXISTS, adminConsoleService.addSensorEmails(SENSOR_EMAILS_NOT_EXISTS));
		assertEquals(SENSOR_EMAILS_NOT_EXISTS,
				mongoTemplate.findById(ID_NOT_EXISTS, SensorEmailsDoc.class, EMAIL_COLLECTION).build());
	}

	@Test
	void updateSensorRange_correctFlow_success() throws Exception {
		assertEquals(SENSOR_RANGE, mongoTemplate.findById(ID, RangeDoc.class, RANGE_COLLECTION).build());
		assertEquals(SENSOR_RANGE_UPDATED, adminConsoleService.updateSensorRange(SENSOR_RANGE_UPDATED));
		assertEquals(SENSOR_RANGE_UPDATED, mongoTemplate.findById(ID, RangeDoc.class, RANGE_COLLECTION).build());

		Message<byte[]> message = consumer.receive(100, producerBindingName);
		assertEquals(SENSOR_UPDATE_RANGE_DATA, mapper.readValue(message.getPayload(), SensorUpdateData.class));
	}

	@Test
	void updateSensorEmails_correctFlow_success() throws Exception {
		assertEquals(SENSOR_EMAILS, mongoTemplate.findById(ID, SensorEmailsDoc.class, EMAIL_COLLECTION).build());
		assertEquals(SENSOR_EMAILS_UPDATED, adminConsoleService.updateSensorEmails(SENSOR_EMAILS_UPDATED));
		assertEquals(SENSOR_EMAILS_UPDATED,
				mongoTemplate.findById(ID, SensorEmailsDoc.class, EMAIL_COLLECTION).build());

		Message<byte[]> message = consumer.receive(100, producerBindingName);
		assertEquals(SENSOR_UPDATE_EMAILS_DATA, mapper.readValue(message.getPayload(), SensorUpdateData.class));
	}

	@Test
	void addSensorRange_alreadyExists_throwsException() {
		assertThrowsExactly(SensorRangeIllegalStateException.class,
				() -> adminConsoleService.addSensorRange(SENSOR_RANGE_UPDATED));
		assertEquals(SENSOR_RANGE, mongoTemplate.findById(ID, RangeDoc.class, RANGE_COLLECTION).build());
	}

	@Test
	void addSensorEmails_alreadyExists_throwsException() {
		assertThrowsExactly(SensorEmailsIllegalStateException.class,
				() -> adminConsoleService.addSensorEmails(SENSOR_EMAILS_UPDATED));
		assertEquals(SENSOR_EMAILS, mongoTemplate.findById(ID, SensorEmailsDoc.class, EMAIL_COLLECTION).build());
	}

	@Test
	void updateSensorRange_notExists_throwsException() {
		assertNull(mongoTemplate.findById(ID_NOT_EXISTS, RangeDoc.class, RANGE_COLLECTION));
		assertThrowsExactly(SensorRangeNotFoundException.class,
				() -> adminConsoleService.updateSensorRange(SENSOR_RANGE_NOT_EXISTS));
		assertNull(mongoTemplate.findById(ID_NOT_EXISTS, RangeDoc.class, RANGE_COLLECTION));

		Message<byte[]> message = consumer.receive(100, producerBindingName);
		assertNull(message);
	}

	@Test
	void updateSensorEmails_notExists_throwsException() {
		assertNull(mongoTemplate.findById(ID_NOT_EXISTS, SensorEmailsDoc.class, EMAIL_COLLECTION));
		assertThrowsExactly(SensorEmailsNotFoundException.class,
				() -> adminConsoleService.updateSensorEmails(SENSOR_EMAILS_NOT_EXISTS));
		assertNull(mongoTemplate.findById(ID_NOT_EXISTS, SensorEmailsDoc.class, EMAIL_COLLECTION));

		Message<byte[]> message = consumer.receive(100, producerBindingName);
		assertNull(message);
	}

}
