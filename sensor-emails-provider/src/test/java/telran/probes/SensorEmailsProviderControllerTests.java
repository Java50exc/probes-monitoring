package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.probes.UrlConstants.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.probes.exceptions.SensorEmailsNotFoundException;
import telran.probes.service.SensorEmailsProviderService;
import static telran.probes.messages.ErrorMessages.*;

@WebMvcTest
class SensorEmailsProviderControllerTests {
	@MockBean
	SensorEmailsProviderService sensorEmailsProviderService;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	
	@Test
	void getSensorEmails_correctFlow_success() throws Exception {
		when(sensorEmailsProviderService.getSensorEmails(TestDb.ID)).thenReturn(TestDb.EMAILS);
		String expectedJson = mapper.writeValueAsString(TestDb.EMAILS);
		String response = mockMvc.perform(get(TestDb.URL_PATH + EMAILS_PATH + "/" + TestDb.ID)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(expectedJson, response);
	}
	
	@Test 
	void getSensorEmails_idNotExists_throwsException() throws Exception {
		when(sensorEmailsProviderService.getSensorEmails(TestDb.ID_NOT_EXISTS)).thenThrow(new SensorEmailsNotFoundException());
		String response = mockMvc.perform(get(TestDb.URL_PATH + EMAILS_PATH + "/" + TestDb.ID_NOT_EXISTS)).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals(MISSING_EMAILS, response);
	}
	



}
