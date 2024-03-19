package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.probes.messages.ErrorMessages.*;
import java.util.Arrays;
import static telran.probes.exceptions.controller.WebExceptionsController.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static telran.probes.UrlConstants.*;
import static telran.probes.TestDb.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import telran.probes.exceptions.*;
import telran.probes.service.AdminConsoleService;

@WebMvcTest
public class AdminConsoleControllerTests {
	@MockBean
	AdminConsoleService adminConsoleService;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;

	@Test
	void addSensorRange_correctFlow_success() throws Exception {
		when(adminConsoleService.addSensorRange(SENSOR_RANGE)).thenReturn(SENSOR_RANGE);
		testValidation(SENSOR_RANGE, mapper.writeValueAsString(SENSOR_RANGE), post(PATH + SENSOR_RANGE_PATH), status().isOk());
	}

	@Test
	void addSensorEmails_correctFlow_success() throws Exception {
		when(adminConsoleService.addSensorEmails(SENSOR_EMAILS)).thenReturn(SENSOR_EMAILS);
		testValidation(SENSOR_EMAILS, mapper.writeValueAsString(SENSOR_EMAILS), post(PATH + EMAILS_PATH), status().isOk());
	}

	@Test
	void updateSensorRange_correctFlow_success() throws Exception {
		when(adminConsoleService.updateSensorRange(SENSOR_RANGE)).thenReturn(SENSOR_RANGE);
		testValidation(SENSOR_RANGE, mapper.writeValueAsString(SENSOR_RANGE), put(PATH + SENSOR_RANGE_PATH), status().isOk());
	}

	@Test
	void updateSensorEmails_correctFlow_success() throws Exception {
		when(adminConsoleService.updateSensorEmails(SENSOR_EMAILS)).thenReturn(SENSOR_EMAILS);
		testValidation(SENSOR_EMAILS, mapper.writeValueAsString(SENSOR_EMAILS), put(PATH + EMAILS_PATH), status().isOk());
	}

	@Test
	void addSensorRange_exceptionThrown_errorMessage() throws Exception {
		when(adminConsoleService.addSensorRange(SENSOR_RANGE)).thenThrow(new SensorRangeIllegalStateException());
		testValidation(SENSOR_RANGE, SENSOR_RANGE_ALREADY_EXISTS, post(PATH + SENSOR_RANGE_PATH), status().isBadRequest());
	}

	@Test
	void addSensorEmails_exceptionThrown_errorMessage() throws Exception {
		when(adminConsoleService.addSensorEmails(SENSOR_EMAILS)).thenThrow(new SensorEmailsIllegalStateException());
		testValidation(SENSOR_EMAILS, SENSOR_EMAILS_ALREADY_EXISTS, post(PATH + EMAILS_PATH), status().isBadRequest());
	}

	@Test
	void updateSensorRange_exceptionThrown_errorMessage() throws Exception {
		when(adminConsoleService.updateSensorRange(SENSOR_RANGE)).thenThrow(new SensorRangeNotFoundException());
		testValidation(SENSOR_RANGE, MISSING_RANGE, put(PATH + SENSOR_RANGE_PATH), status().isNotFound());
	}

	@Test
	void updateSensorEmails_exceptionThrown_errorMessage() throws Exception {
		when(adminConsoleService.updateSensorEmails(SENSOR_EMAILS)).thenThrow(new SensorEmailsNotFoundException());
		testValidation(SENSOR_EMAILS, MISSING_EMAILS, put(PATH + EMAILS_PATH), status().isNotFound());
	}

	@Test
	void addSensorRange_idTypeMismatch_throwsException() throws Exception {
		testValidation(SENSOR_RANGE_WRONG_ID, JSON_TYPE_MISMATCH_MESSAGE, post(PATH + SENSOR_RANGE_PATH), status().isBadRequest());
	}

	@Test
	void addSensorRange_rangeValueTypeMismatch_throwsException() throws Exception {
		testValidation(SENSOR_RANGE_WRONG_RANGE, JSON_TYPE_MISMATCH_MESSAGE, post(PATH + SENSOR_RANGE_PATH), status().isBadRequest());
	}

	@Test
	void updateSensorRange_rangeValueTypeMismatch_throwsException() throws Exception {
		testValidation(SENSOR_RANGE_WRONG_RANGE, JSON_TYPE_MISMATCH_MESSAGE, put(PATH + SENSOR_RANGE_PATH), status().isBadRequest());
	}

	@Test
	void updateSensorRange_idTypeMismatch_throwsException() throws Exception {
		testValidation(SENSOR_RANGE_WRONG_ID, JSON_TYPE_MISMATCH_MESSAGE, put(PATH + SENSOR_RANGE_PATH), status().isBadRequest());
	}

	@Test
	void addSensorEmails_idTypeMismatch_throwsException() throws Exception {
		testValidation(SENSOR_EMAILS_WRONG_ID, JSON_TYPE_MISMATCH_MESSAGE, post(PATH + EMAILS_PATH), status().isBadRequest());
	}

	@Test
	void updateSensorEmails_idTypeMismatch_throwsException() throws Exception {
		testValidation(SENSOR_EMAILS_WRONG_ID, JSON_TYPE_MISMATCH_MESSAGE, put(PATH + EMAILS_PATH), status().isBadRequest());
	}

	@Test
	void addSensorEmails_emptyEmails_throwsException() throws Exception {
		testValidation(SENSOR_EMAILS_EMPTY, MISSING_EMAILS, post(PATH + EMAILS_PATH), status().isBadRequest());
	}

	@Test
	void updateSensorEmails_emptyEmails_throwsException() throws Exception {
		testValidation(SENSOR_EMAILS_EMPTY, MISSING_EMAILS, put(PATH + EMAILS_PATH), status().isBadRequest());
	}

	@Test
	void addSensorRange_rangeWithMissingFields_throwsException() throws Exception {
		missingAllFieldsValidation(SENSOR_RANGE_MISSING_RANGE, RANGE_MISSING_MESSAGES, post(PATH + SENSOR_RANGE_PATH));
	}

	@Test
	void updateSensorRange_rangeWithMissingFields_throwsException() throws Exception {
		missingAllFieldsValidation(SENSOR_RANGE_MISSING_RANGE, RANGE_MISSING_MESSAGES, put(PATH + SENSOR_RANGE_PATH));
	}

	@Test
	void addSensorRange_missingAllFields_throwsException() throws Exception {
		missingAllFieldsValidation(SENSOR_RANGE_MISSING_ALL_FIELDS, SENSOR_RANGE_MISSING_MESSAGES, post(PATH + SENSOR_RANGE_PATH));
	}

	@Test
	void updateSensorRange_missingAllFields_throwsException() throws Exception {
		missingAllFieldsValidation(SENSOR_RANGE_MISSING_ALL_FIELDS, SENSOR_RANGE_MISSING_MESSAGES, put(PATH + SENSOR_RANGE_PATH));
	}

	@Test
	void addSensorEmails_missingAllFields_throwsException() throws Exception {
		missingAllFieldsValidation(SENSOR_EMAILS_MISSING_ALL_FIELDS, EMAILS_MISSING_MESSAGES, post(PATH + EMAILS_PATH));
	}

	@Test
	void updateSensorEmails_missingAllFields_throwsException() throws Exception {
		missingAllFieldsValidation(SENSOR_EMAILS_MISSING_ALL_FIELDS, EMAILS_MISSING_MESSAGES, put(PATH + EMAILS_PATH));
	}
	
	private void testValidation(Object request, String expectedResponse, MockHttpServletRequestBuilder method, ResultMatcher expectedStatus)
			throws Exception {
		String json = mapper.writeValueAsString(request);
		String response = mockMvc.perform(method.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(expectedStatus).andReturn().getResponse().getContentAsString();
		assertEquals(expectedResponse, response);
	}
	
	private void missingAllFieldsValidation(Object request, String[] expectedResponse, MockHttpServletRequestBuilder method) throws Exception {
		String json = mapper.writeValueAsString(request);
		String response = mockMvc.perform(method.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		String[] errMessages = response.split(";");
		Arrays.sort(errMessages);
		Arrays.sort(expectedResponse);
		assertArrayEquals(expectedResponse, errMessages);
	}
}
