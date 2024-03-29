package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import telran.probes.model.ProbesList;
import telran.probes.repo.ProbesListRepo;
import telran.probes.service.AvgReducerService;
import static telran.probes.TestsConstants.*;

@SpringBootTest
class AvgReducerServiceTests {
	@Autowired
	AvgReducerService reducerService;
	@MockBean
	ProbesListRepo probesListRepo;
	HashMap<Long, ProbesList> redisMockMap = new HashMap<>();
	
	
	@BeforeEach
	void mockSetUp() {
		when(probesListRepo.findById(any(Long.class))).then(new Answer<Optional<ProbesList>>() {
			@Override
			public Optional<ProbesList> answer(InvocationOnMock invocation) throws Throwable {
				Long sensorId = invocation.getArgument(0);
				return Optional.ofNullable(redisMockMap.get(sensorId));
			}
		});
		
		when(probesListRepo.save(any(ProbesList.class))).then(new Answer<ProbesList>() {
			@Override
			public ProbesList answer(InvocationOnMock invocation) throws Throwable {
				ProbesList probesList = invocation.getArgument(0);
				redisMockMap.put(probesList.getSensorId(), probesList);
				return probesList;
			}
		});
	}

	@Test
	void getAvgValue_correctFlow_success() {
		Double avgValue = reducerService.getAvgValue(PROBE_DATA);
		assertNull(avgValue);
		avgValue = reducerService.getAvgValue(PROBE_DATA);
		assertNotNull(avgValue);
		assertEquals(VALUE, avgValue);
		
		avgValue = reducerService.getAvgValue(PROBE_DATA);
		assertNull(avgValue);
		avgValue = reducerService.getAvgValue(PROBE_DATA);
		assertNotNull(avgValue);
		assertEquals(VALUE, avgValue);
	}

}
