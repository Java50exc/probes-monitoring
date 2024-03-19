package telran.probes.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.model.SensorEmailsDoc;
import telran.probes.repo.SensorEmailsProviderRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorEmailsProviderServiceImpl implements SensorEmailsProviderService {
	final SensorEmailsProviderRepo sensorEmailsProviderRepo;

	@Override
	public String[] getSensorEmails(long sensorId) {
		//FIXME to add a proper exception and update tests
		SensorEmailsDoc emailsDoc = sensorEmailsProviderRepo.findById(sensorId).orElseThrow(IllegalStateException::new);
		log.debug("SensorEmailsProviderService: received mails {} for id {}", emailsDoc.getEmails(), sensorId);
		return emailsDoc.getEmails();
	}

}
