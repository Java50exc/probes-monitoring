package telran.probes.auth;

import java.util.Arrays;
import org.springframework.http.*;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.model.Account;
import telran.probes.service.ServiceConfiguration;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
	final RestTemplate restTemplate;
	final ServiceConfiguration serviceConfig;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ResponseEntity<Account> responseEntity = restTemplate.exchange(getUrl(username), HttpMethod.GET, null,
				Account.class);

		if (responseEntity.getStatusCode().is4xxClientError()) {
			throw new UsernameNotFoundException("");
		}
		Account account = responseEntity.getBody();
		String[] roles = Arrays.stream(account.getRoles()).map(r -> "ROLE_" + r).toArray(String[]::new);
		User user = new User(username, account.getHashPassword(), AuthorityUtils.createAuthorityList(roles));

		return user;
	}

	private String getUrl(String username) {
		String url = String.format("http://%s:%d/%s/%s", serviceConfig.getHost(), serviceConfig.getPort(),
				serviceConfig.getPath(), username);
		log.debug("url created is {}", url);
		return url;
	}


}
