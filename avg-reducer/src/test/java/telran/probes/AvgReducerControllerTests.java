package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import telran.probes.dto.ProbeData;
import telran.probes.service.AvgReducerService;
import static telran.probes.TestsConstants.*;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AvgReducerControllerTests {
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	@MockBean
	AvgReducerService reducerService;
	@Value("${app.avg.reducer.consumer.binding.name}")
	String consumerBindingName;
	@Value("${app.avg.reducer.producer.binding.name}")
	String producerBindingName;
	
	ObjectMapper mapper = new ObjectMapper();
	

	@Test
	void probeDataReducing_nullFromService_none() {
		when(reducerService.getAvgValue(any(ProbeData.class))).thenReturn(null);
		
		producer.send(new GenericMessage<ProbeData>(PROBE_DATA), consumerBindingName);
		Message<byte[]> message = consumer.receive(100, producerBindingName);
		assertNull(message);
	}
	
	@Test
	void probeDataReducing_avgDataFromService_avgDataSent() throws Exception {
		when(reducerService.getAvgValue(any(ProbeData.class))).thenReturn(VALUE);
		
		producer.send(new GenericMessage<ProbeData>(PROBE_DATA), consumerBindingName);
		Message<byte[]> message = consumer.receive(100, producerBindingName);
		assertNotNull(message);
		ProbeData probeAvg = mapper.readValue(message.getPayload(), ProbeData.class);
		assertEquals(PROBE_DATA.id(), probeAvg.id());
		assertEquals(PROBE_DATA.value(), probeAvg.value());
	}

}
