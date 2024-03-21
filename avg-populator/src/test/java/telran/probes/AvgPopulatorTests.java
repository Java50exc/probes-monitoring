package telran.probes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;
import telran.probes.dto.ProbeData;
import telran.probes.model.ProbeDataDoc;
import telran.probes.repo.AvgPopulatorRepo;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class AvgPopulatorTests {
	
	@Value("${app.avg.populator.consumer.binding.name}")
	private String consumerBindingName;
	
	private static final long ID = 123;
	private static final long TIMESTAMP = System.currentTimeMillis();
	private static final double AVG_VALUE = 1000;
	private static final ProbeData PROBE_DATA = new ProbeData(ID, AVG_VALUE, TIMESTAMP);
	private static final ProbeDataDoc PROBE_DATA_DOC = new ProbeDataDoc(PROBE_DATA);
	
	@Autowired
	InputDestination producer;
	@Autowired
	AvgPopulatorRepo avgPopulatorRepo;
	
	@Test
	void avgPopulation_correctFlow_success() throws Exception {
		assertEquals(0, avgPopulatorRepo.count());
		producer.send(new GenericMessage<ProbeData>(PROBE_DATA), consumerBindingName);
		Thread.sleep(100);
		List<ProbeDataDoc> probeDocs = avgPopulatorRepo.findBySensorId(ID);
		assertEquals(1, avgPopulatorRepo.count());
		assertEquals(1, probeDocs.size());
		assertEquals(PROBE_DATA_DOC, probeDocs.get(0));	
	}

}
