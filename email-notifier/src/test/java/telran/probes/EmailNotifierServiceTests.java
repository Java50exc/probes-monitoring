package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static telran.probes.TestsConstants.*;
import org.junit.jupiter.api.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.client.RestTemplate;
import telran.probes.dto.SensorUpdateData;
import telran.probes.service.EmailsProviderClientService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmailNotifierServiceTests {
	@Autowired
	InputDestination producer;
	@Autowired
	EmailsProviderClientService providerService;
	@MockBean
	RestTemplate restTemplate;
	@Value("${app.update.emails.consumer.binding.name}")
	String updateBindingName;

	@Test
	@Order(1)
	void getMails_noCache_sendsRequest() {
		ResponseEntity<String[]> reponseEntity = new ResponseEntity<>(EMAILS, HttpStatus.OK);
		when(restTemplate.exchange(getUrl(SENSOR_ID), HttpMethod.GET, null, String[].class)).thenReturn(reponseEntity);
		assertArrayEquals(EMAILS, providerService.getMails(SENSOR_ID));
	}

	@SuppressWarnings("unchecked")
	@Test
	@Order(2)
	void getMails_withCache_returnsCache() {
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
				.thenAnswer(new Answer<ResponseEntity<?>>() {

					@Override
					public ResponseEntity<?> answer(InvocationOnMock invocation) throws Throwable {
						fail("method exchange should not be called");
						return null;
					}
				});
		assertArrayEquals(EMAILS, providerService.getMails(SENSOR_ID));
	}

	@Test
	@Order(3)
	void getMails_sensorNotFound_defaultEmails() {
		ResponseEntity<String> reponseEntity = new ResponseEntity<>(SENSOR_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
		when(restTemplate.exchange(getUrl(SENSOR_ID_NOT_FOUND), HttpMethod.GET, null, String.class))
				.thenReturn(reponseEntity);
		assertArrayEquals(EMAILS_DEFAULT, providerService.getMails(SENSOR_ID_NOT_FOUND));
	}

	@Test
	@Order(4)
	void getMails_requestAfterSendingDefault_defaultNotInCache() {
		ResponseEntity<String[]> reponseEntity = new ResponseEntity<>(EMAILS, HttpStatus.OK);
		when(restTemplate.exchange(getUrl(SENSOR_ID_NOT_FOUND), HttpMethod.GET, null, String[].class))
				.thenReturn(reponseEntity);
		assertArrayEquals(EMAILS, providerService.getMails(SENSOR_ID_NOT_FOUND));
	}

	@SuppressWarnings("unchecked")
	@Test
	void getMails_remoteServiceUnavailable_defaultEmails() {
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
				.thenThrow(new IllegalStateException("Service is unavailable"));

		assertArrayEquals(EMAILS_DEFAULT, providerService.getMails(SENSOR_ID_UNAVAILABLE));
	}

	@Test
	void updateMails_gotMessageFromBroker_emailsInCacheUpdated() throws Exception {
		producer.send(new GenericMessage<SensorUpdateData>(new SensorUpdateData(SENSOR_ID, null, EMAILS_UPDATED)),
				updateBindingName);
		Thread.sleep(100);
		assertArrayEquals(EMAILS_UPDATED, providerService.getMails(SENSOR_ID));
	}

	private String getUrl(long sensorId) {
		return URL + sensorId;
	}

}
