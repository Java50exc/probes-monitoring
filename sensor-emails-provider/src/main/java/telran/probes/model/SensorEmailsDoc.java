package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Document(collection = "emails")
@Getter
@AllArgsConstructor
public class SensorEmailsDoc {
	@Id
	long id;
	String[] emails;

}
