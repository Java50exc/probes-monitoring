package telran.probes.model;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
	String email;
	String hashPassword;
	String[] roles;
	
}
