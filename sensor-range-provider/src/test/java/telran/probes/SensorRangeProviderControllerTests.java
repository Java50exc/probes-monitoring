package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.probes.UrlConstants.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import telran.probes.dto.Range;
import telran.probes.service.SensorRangeProviderService;

@WebMvcTest
class SensorRangeProviderControllerTests {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	SensorRangeProviderService sensorRangeProviderService;
	@Autowired
	ObjectMapper mapper;
	
	private static final String URL_PATH = "http://localhost:8080/";
	
	private static final long ID = 123;
	private static final long MIN_VALUE = 100;
	private static final long MAX_VALUE = 200;
	private static final Range RANGE = new Range(MIN_VALUE, MAX_VALUE);
	

	@Test
	void getSensorRange_correctFlow_success() throws Exception {
		when(sensorRangeProviderService.getSensorRange(ID)).thenReturn(RANGE);
		String expectedJson = mapper.writeValueAsString(RANGE);
		String response = mockMvc.perform(get(URL_PATH + SENSOR_RANGE_PATH + ID)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(expectedJson, response);
	}
	

}
