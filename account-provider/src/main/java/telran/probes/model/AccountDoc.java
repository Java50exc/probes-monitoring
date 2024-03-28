package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import telran.probes.dto.AccountDto;

@Document(collection = "accounts")
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AccountDoc {
	@Id
	String username;
	String hashPassword;
	String[] roles;
	
	public AccountDto toDto() {
		return new AccountDto(username, hashPassword, roles);
	}
	
	public AccountDoc(AccountDto accountDto) {
		username = accountDto.username();
		hashPassword = accountDto.hashPassword();
		roles = accountDto.roles();
	}
}
