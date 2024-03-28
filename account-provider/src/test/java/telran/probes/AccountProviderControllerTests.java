package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.probes.messages.ErrorMessages.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import telran.probes.dto.AccountDto;
import telran.probes.exceptions.AccountNotFoundException;
import telran.probes.service.AccountProviderService;

@WebMvcTest
class AccountProviderControllerTests {
	private final static String PATH = "http://localhost:8080";
	private final static String EMAIL = "email@gmail.com";
	private final static String WRONG_EMAIL = "emailgmail.com";
	private final static String BLANK_EMAIL = "  ";
	private static final String PASSWORD = "12345678";
	private static String ROLE1 = "Role1";
	private static String ROLE2 = "Role2";
	private static final String[] ROLES = {ROLE1, ROLE2};
	private final static AccountDto ACCOUNT_DTO = new AccountDto(EMAIL, PASSWORD, ROLES);
	
	@Autowired
	MockMvc mockMvc;
	@MockBean
	AccountProviderService providerService;
	@Autowired
	ObjectMapper mapper;
	@Value("${app.account.provider.path}")
	String ACCOUNTS_PATH;
	

	@Test
	void getAccount_correctFlow_success() throws Exception {
		when(providerService.getAccount(EMAIL)).thenReturn(ACCOUNT_DTO);
		testValidation(null, mapper.writeValueAsString(ACCOUNT_DTO), get(PATH + ACCOUNTS_PATH + "/" + EMAIL), status().isOk());
	}
	
	@Test
	void getAccount_exceptionThrown_errorMessage() throws Exception {
		when(providerService.getAccount(EMAIL)).thenThrow(new AccountNotFoundException());
		testValidation(null, MISSING_ACCOUNT, get(PATH + ACCOUNTS_PATH + "/" + EMAIL), status().isNotFound());
	}
	
	@Test
	void getAccount_wrongEmail_errorMessage() throws Exception {
		testValidation(null, WRONG_EMAIL_FORMAT, get(PATH + ACCOUNTS_PATH + "/" + WRONG_EMAIL), status().isBadRequest());
	}
	
	@Test
	void getAccount_nullEmail_errorMessage() throws Exception {
		when(providerService.getAccount(EMAIL)).thenThrow(new AccountNotFoundException());
		testValidation(null, WRONG_EMAIL_FORMAT, get(PATH + ACCOUNTS_PATH + "/" + null), status().isBadRequest());
	}
	
	@Test
	void getAccount_blankEmail_errorMessage() throws Exception {
		when(providerService.getAccount(EMAIL)).thenThrow(new AccountNotFoundException());
		testValidation(null, WRONG_EMAIL_FORMAT, get(PATH + ACCOUNTS_PATH + "/" + BLANK_EMAIL), status().isBadRequest());
	}
	
	
	private void testValidation(Object request, String expectedResponse, MockHttpServletRequestBuilder method,
			ResultMatcher expectedStatus) throws Exception {
		if (request != null) {
			method = method.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request));
		}
		String response = mockMvc.perform(method).andExpect(expectedStatus).andReturn().getResponse()
				.getContentAsString();
		assertEquals(expectedResponse, response);
	}
}
