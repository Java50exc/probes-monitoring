package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static telran.probes.TestsConstants.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.probes.dto.DeviationData;
import telran.probes.dto.ProbeData;
import telran.probes.service.RangeProviderClientService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AnalyzerControllerTest {
	@MockBean
	RangeProviderClientService clientService;
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		when(clientService.getRange(SENSOR_ID)).thenReturn(RANGE);
	}

	@Test
	void probeDataAnalyzing_noDeviation_success() {
		producer.send(new GenericMessage<ProbeData>(probeNormalData), consumerBindingName);
		Message<byte[]> message = consumer.receive(100, producerBindingName);
		assertNull(message);
	}

	@Test
	void probeDataAnalyzing_greaterThanMax_positiveDeviation() throws Exception {
		producer.send(new GenericMessage<ProbeData>(probeGreaterMaxData), consumerBindingName);
		Message<byte[]> message = consumer.receive(100, producerBindingName);
		assertNotNull(message);
		DeviationData deviationData = mapper.readValue(message.getPayload(), DeviationData.class);
		assertEquals(SENSOR_ID, deviationData.id());
		assertEquals(DEVIATION_GREATER_MAX, deviationData.deviation());
	}

	@Test
	void probeDataAnalyzing_lessThanMin_negativeDeviation() throws Exception {
		producer.send(new GenericMessage<ProbeData>(probeLessMinData), consumerBindingName);
		Message<byte[]> message = consumer.receive(100, producerBindingName);
		assertNotNull(message);
		DeviationData deviationData = mapper.readValue(message.getPayload(), DeviationData.class);
		assertEquals(SENSOR_ID, deviationData.id());
		assertEquals(DEVIATION_LESS_MIN, deviationData.deviation());
	}

}
