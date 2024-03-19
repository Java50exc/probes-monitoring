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

import telran.probes.service.SensorEmailsProviderService;

@WebMvcTest
class SensorEmailsProviderControllerTests {
	private static final String URL_PATH = "http://localhost:8080/";
	
	private static final long ID = 123;
	private static final String EMAIL1 = "email1@gmail.com";
	private static final String EMAIL2 = "email2@gmail.com";
	private static final String[] EMAILS = {EMAIL1, EMAIL2};
	
	@MockBean
	SensorEmailsProviderService sensorEmailsProviderService;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	
	@Test
	void getSensorEmails_correctFlow_success() throws Exception {
		when(sensorEmailsProviderService.getSensorEmails(ID)).thenReturn(EMAILS);
		String expectedJson = mapper.writeValueAsString(EMAILS);
		String response = mockMvc.perform(get(URL_PATH + EMAILS_PATH + ID)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(expectedJson, response);
	}
	



}
