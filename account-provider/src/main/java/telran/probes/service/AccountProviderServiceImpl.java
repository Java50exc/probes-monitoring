package telran.probes.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.AccountDto;
import telran.probes.exceptions.AccountNotFoundException;
import telran.probes.repo.AccountProviderRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountProviderServiceImpl implements AccountProviderService {
	final AccountProviderRepo providerRepo;

	@Override
	public AccountDto getAccount(String email) {
		log.debug("AccountProviderServiceImpl: getAccount: searching for {}", email);
		AccountDto account = providerRepo.findById(email).orElseThrow(AccountNotFoundException::new).toDto();
		log.debug("AccountProviderServiceImpl: getAccount: found {}", account);
		return account;
	}

}
