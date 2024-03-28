package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telran.probes.dto.AccountDto;
import telran.probes.exceptions.AccountNotFoundException;
import telran.probes.model.AccountDoc;
import telran.probes.repo.AccountProviderRepo;
import telran.probes.service.AccountProviderService;

@SpringBootTest
class AccountProviderServiceTests {
	@Autowired
	AccountProviderService providerService;
	@Autowired
	AccountProviderRepo providerRepo;
	
	private final static String EMAIL1 = "email1@gmail.com";
	private final static String EMAIL2 = "email2@gmail.com";
	private final static String PASSWORD1 = "12345678";
	private final static String ROLE1 = "Role1";
	private final static String[] ROLES1 = {ROLE1};
	private final static AccountDto ACCOUNT_DTO1 = new AccountDto(EMAIL1, PASSWORD1, ROLES1);
	
	
	@BeforeEach
	void setUp() {
		providerRepo.save(new AccountDoc(ACCOUNT_DTO1));
	}
	
	@Test
	void getAccount_correctFlow_success() {
		assertEquals(ACCOUNT_DTO1, providerService.getAccount(EMAIL1));
	}
	
	@Test
	void getAccount_notExists_throwsException() {
		assertThrowsExactly(AccountNotFoundException.class, () -> providerService.getAccount(EMAIL2));
	}

}
