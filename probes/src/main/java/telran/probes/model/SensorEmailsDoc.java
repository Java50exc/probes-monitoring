package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.probes.dto.SensorEmails;

@Document(collection = "emails")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SensorEmailsDoc {
	@Id
	long id;
	String[] emails;
	
	public SensorEmails build() {
		return new SensorEmails(id, emails);
	}
	
	public SensorEmailsDoc(SensorEmails sensorEmails) {
		id = sensorEmails.id();
		emails = sensorEmails.mails();
	}

}
