package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static telran.probes.TestsConstants.*;
import telran.probes.dto.*;
import org.junit.jupiter.api.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.client.RestTemplate;
import telran.probes.service.RangeProviderClientService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnalyzerServiceTests {
	@Autowired
	InputDestination producer;
	@Autowired
	RangeProviderClientService providerService;
	@MockBean
	RestTemplate restTemplate;

	@Test
	@Order(1)
	void normalFlowNoCache() {
		ResponseEntity<Range> reponseEntity = new ResponseEntity<>(RANGE, HttpStatus.OK);
		when(restTemplate.exchange(getUrl(SENSOR_ID), HttpMethod.GET, null, Range.class)).thenReturn(reponseEntity);
		assertEquals(RANGE, providerService.getRange(SENSOR_ID));
	}

	@SuppressWarnings("unchecked")
	@Test
	@Order(2)
	void normalFlowWithCache() {
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
				.thenAnswer(new Answer<ResponseEntity<?>>() {

					@Override
					public ResponseEntity<?> answer(InvocationOnMock invocation) throws Throwable {
						fail("method exchange should not be called");
						return null;
					}
				});
		assertEquals(RANGE, providerService.getRange(SENSOR_ID));
	}

	@Test
	@Order(3)
	void sensorNotFoundTest() {
		ResponseEntity<String> reponseEntity = new ResponseEntity<>(SENSOR_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
		when(restTemplate.exchange(getUrl(SENSOR_ID_NOT_FOUND), HttpMethod.GET, null, String.class))
				.thenReturn(reponseEntity);
		assertEquals(RANGE_DEFAULT, providerService.getRange(SENSOR_ID_NOT_FOUND));
	}

	@Test
	@Order(4)
	void defaultRangeNotInCache() {
		ResponseEntity<Range> reponseEntity = new ResponseEntity<>(RANGE, HttpStatus.OK);
		when(restTemplate.exchange(getUrl(SENSOR_ID_NOT_FOUND), HttpMethod.GET, null, Range.class))
				.thenReturn(reponseEntity);
		assertEquals(RANGE, providerService.getRange(SENSOR_ID_NOT_FOUND));
	}

	@SuppressWarnings("unchecked")
	@Test
	void remoteWebServiceUnavalilable() {
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
				.thenThrow(new IllegalStateException("Service is unavailable"));

		assertEquals(RANGE_DEFAULT, providerService.getRange(SENSOR_ID_UNAVAILABLE));
	}

	@Test
	void updateRangeSensorInMap() throws Exception {
		producer.send(new GenericMessage<SensorUpdateData>(new SensorUpdateData(SENSOR_ID, RANGE_UPDATED, null)),
				updateBindingName);
		Thread.sleep(100);
		assertEquals(RANGE_UPDATED, providerService.getRange(SENSOR_ID));
	}

	private String getUrl(long sensorId) {
		return URL + sensorId;
	}

}
