package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import telran.probes.service.ProbesService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class ProbesServiceTests {
	@Autowired
	OutputDestination consumer;
	@Autowired
	ProbesService probesService;
	@Value("${app.probes.producer.binding.name}")
	private String producerBindingName;
	
	

	@Test
	void test() throws Exception {
		for (int i = 0; i < 10; i++) {
			Message<byte[]> message = consumer.receive(5000, producerBindingName);	
			assertNotNull(message);
		}

		
	}

}
