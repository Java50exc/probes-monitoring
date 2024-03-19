package telran.probes.service;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.*;
import telran.probes.exceptions.*;
import telran.probes.model.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminConsoleServiceImpl implements AdminConsoleService {
	final MongoTemplate mongoTemplate;
	final StreamBridge streamBridge;

	String producerBindingName = "adminConsole-out-0";
	static final String RANGE_COLLECTION = "ranges";
	static final String EMAIL_COLLECTION = "emails";

	@Override
	public SensorRange addSensorRange(SensorRange sensorRange) {
		log.debug("Range {} for sensor with id {} has been added", sensorRange.range(), 1);
		long id = sensorRange.id();

		try {
			mongoTemplate.insert(new RangeDoc(sensorRange), RANGE_COLLECTION);
		} catch (DuplicateKeyException e) {
			log.error("Range for sensor with id {} already exists", id);
			throw new SensorRangeIllegalStateException();
		}
		log.debug("Range {} for sensor with id {} has been added", sensorRange.range(), id);
		return sensorRange;
	}

	@Override
	public SensorEmails addSensorEmails(SensorEmails sensorEmails) {
		long id = sensorEmails.id();

		try {
			mongoTemplate.insert(new SensorEmailsDoc(sensorEmails), EMAIL_COLLECTION);
		} catch (DuplicateKeyException e) {
			log.error("Emails for sensor with id {} already exists", id);
			throw new SensorEmailsIllegalStateException();
		}
		log.debug("Emails {} for sensor with id {} has been added", sensorEmails.mails(), id);
		return sensorEmails;
	}

	@Override
	public SensorRange updateSensorRange(SensorRange sensorRange) {
		long id = sensorRange.id();
		RangeDoc doc = mongoTemplate.findAndModify(new Query(Criteria.where("id").is(id)),
				new Update().set("range", sensorRange.range()), RangeDoc.class, RANGE_COLLECTION);

		if (doc == null) {
			log.error("Range for sensor with id {} not found", id);
			throw new SensorRangeNotFoundException();
		}
		streamBridge.send(producerBindingName, new SensorUpdateData(id, sensorRange.range(), null));
		log.debug("Range {} for sensor with id {} has been updated", sensorRange.range(), id);
		return sensorRange;
	}

	@Override
	public SensorEmails updateSensorEmails(SensorEmails sensorEmails) {
		long id = sensorEmails.id();
		SensorEmailsDoc doc = mongoTemplate.findAndModify(new Query(Criteria.where("id").is(id)),
				new Update().set("emails", sensorEmails.mails()), SensorEmailsDoc.class, EMAIL_COLLECTION);
		
		if (doc == null) {
			log.error("Emails for sensor with id {} not found", id);
			throw new SensorEmailsNotFoundException();
		}
		streamBridge.send(producerBindingName, new SensorUpdateData(id, null, sensorEmails.mails()));
		log.debug("Emails {} for sensor with id {} has been updated", sensorEmails.mails(), id);
		return sensorEmails;
	}

}
