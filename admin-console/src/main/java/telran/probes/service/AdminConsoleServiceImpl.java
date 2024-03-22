package telran.probes.service;

import static telran.probes.messages.ErrorMessages.*;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${app.admin.console.producer.binding.name}")
	String producerBindingName;
	@Value("${app.sensor.ranges.collection.name}")
	String RANGE_COLLECTION;
	@Value("${app.sensor.emails.collection.name}")
	String EMAIL_COLLECTION;

	@Override
	public SensorRange addSensorRange(SensorRange sensorRange) {
		rangeValidation(sensorRange.range());
		long id = sensorRange.id();

		try {
			mongoTemplate.insert(new RangeDoc(sensorRange), RANGE_COLLECTION);
		} catch (DuplicateKeyException e) {
			log.error("Range for sensor with id {} already exists", id);
			throw new SensorRangeIllegalStateException(SENSOR_RANGE_ALREADY_EXISTS);
		}
		log.debug("Range {} for sensor with id {} has been added", sensorRange.range(), id);
		return sensorRange;
	}

	@Override
	public SensorEmails addSensorEmails(SensorEmails sensorEmails) {
		emailsValidation(sensorEmails.mails());
		long id = sensorEmails.id();

		try {
			mongoTemplate.insert(new SensorEmailsDoc(sensorEmails), EMAIL_COLLECTION);
		} catch (DuplicateKeyException e) {
			log.error("Emails for sensor with id {} already exists", id);
			throw new SensorEmailsIllegalStateException(SENSOR_EMAILS_ALREADY_EXISTS);
		}
		log.debug("Emails {} for sensor with id {} has been added", sensorEmails.mails(), id);
		return sensorEmails;
	}

	@Override
	public SensorRange updateSensorRange(SensorRange sensorRange) {
		rangeValidation(sensorRange.range());
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
		emailsValidation(sensorEmails.mails());
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
	
	private void rangeValidation(Range range) {
		if (range.maxValue() < range.minValue()) {
			throw new SensorRangeIllegalStateException(RANGE_MIN_GREATER_THEN_MAX);
		}
	}
	
	private void emailsValidation(String[] emails) { 
       Arrays.stream(emails).forEach(e -> {
    	   if (!e.matches(EMAIL_REGEX)) {
    		   throw new SensorEmailsIllegalStateException(INVALID_EMAIL_FORMAT + e);
    	   }
       });
	}

        

}
