package telran.probes;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.probes.model.SensorEmailsDoc;
import telran.probes.repo.SensorEmailsProviderRepo;

@Component
@RequiredArgsConstructor
public class TestDb {
	final SensorEmailsProviderRepo sensorEmailsProviderRepo;
	
	static final String URL_PATH = "http://localhost:8080/";
	static final long ID = 123;
	static final long ID_NOT_EXISTS = 124;
	static final String EMAIL1 = "email1@gmail.com";
	static final String EMAIL2 = "email2@gmail.com";
	static final String[] EMAILS = {EMAIL1, EMAIL2};
	static final SensorEmailsDoc EMAILS_DOC = new SensorEmailsDoc(ID, EMAILS);
	
	void createDb() {
		sensorEmailsProviderRepo.deleteAll();
		sensorEmailsProviderRepo.insert(EMAILS_DOC);
	}

}
