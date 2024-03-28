package telran.probes;

import static telran.probes.messages.ErrorMessages.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import telran.probes.dto.*;
import telran.probes.model.*;

@Component
@RequiredArgsConstructor
public class TestDb {
	final MongoTemplate mongoTemplate;

	public record RangeWrongMinValue(String minValue, double maxValue) {
	}

	public record SensorRangeWrongId(String id, Range range) {
	}

	public record SensorEmailsWrongId(String id, String[] mails) {
	}

	public record SensorRangeWrongRange(long id, RangeWrongMinValue range) {
	}

	static final String PATH = "http://localhost:8080/";

	@Value("${app.sensor.ranges.collection.name}")
	String RANGE_COLLECTION;
	@Value("${app.sensor.emails.collection.name}")
	String EMAIL_COLLECTION;
	@Value("${app.admin.console.range.url}")
	String SENSOR_RANGE_PATH;
	@Value("${app.admin.console.email.url}")
	String EMAILS_PATH;

	static final long ID = 123;
	static final long ID_NOT_EXISTS = 124;

	static final Double MIN_VALUE = 100d;
	static final Double MAX_VALUE = 200d;

	static final String EMAIL1 = "email1@gmail.com";
	static final String EMAIL2 = "email2@gmail.com";
	static final String EMAIL3 = "email3@gmail.com";
	static final String[] EMAILS = { EMAIL1, EMAIL2 };
	static final String[] EMAILS_UPDATED = { EMAIL1, EMAIL3 };

	static final Range RANGE = new Range(MIN_VALUE, MAX_VALUE);
	static final Range RANGE_UPDATED = new Range(MIN_VALUE + 100, MAX_VALUE + 100);
	static final Range RANGE_MISSING_ALL_FIELDS = new Range(null, null);

	static final SensorRange SENSOR_RANGE = new SensorRange(ID, RANGE);
	static final SensorRange SENSOR_RANGE_UPDATED = new SensorRange(ID, RANGE_UPDATED);
	static final SensorRange SENSOR_RANGE_NOT_EXISTS = new SensorRange(ID_NOT_EXISTS, RANGE);
	static final SensorRange SENSOR_RANGE_MISSING_ALL_FIELDS = new SensorRange(null, null);
	static final SensorRange SENSOR_RANGE_MISSING_RANGE = new SensorRange(ID, RANGE_MISSING_ALL_FIELDS);

	static final SensorEmails SENSOR_EMAILS = new SensorEmails(ID, EMAILS);
	static final SensorEmails SENSOR_EMAILS_UPDATED = new SensorEmails(ID, EMAILS_UPDATED);
	static final SensorEmails SENSOR_EMAILS_NOT_EXISTS = new SensorEmails(ID_NOT_EXISTS, EMAILS);
	static final SensorEmails SENSOR_EMAILS_EMPTY = new SensorEmails(ID, new String[] {});
	static final SensorEmails SENSOR_EMAILS_MISSING_ALL_FIELDS = new SensorEmails(null, null);

	static final SensorUpdateData SENSOR_UPDATE_RANGE_DATA = new SensorUpdateData(SENSOR_RANGE_UPDATED.id(),
			SENSOR_RANGE_UPDATED.range(), null);
	static final SensorUpdateData SENSOR_UPDATE_EMAILS_DATA = new SensorUpdateData(SENSOR_EMAILS_UPDATED.id(), null,
			SENSOR_EMAILS_UPDATED.mails());

	static final RangeWrongMinValue RANGE_WRONG_MIN = new RangeWrongMinValue("kkk", MAX_VALUE);
	static final SensorRangeWrongId SENSOR_RANGE_WRONG_ID = new SensorRangeWrongId("kkk", RANGE);
	static final SensorRangeWrongRange SENSOR_RANGE_WRONG_RANGE = new SensorRangeWrongRange(ID, RANGE_WRONG_MIN);
	static final SensorEmailsWrongId SENSOR_EMAILS_WRONG_ID = new SensorEmailsWrongId("kkk", EMAILS);

	static final String[] RANGE_MISSING_MESSAGES = { MISSING_MIN_VALUE, MISSING_MAX_VALUE };
	static final String[] SENSOR_RANGE_MISSING_MESSAGES = { MISSING_SENSOR_ID, MISSING_RANGE };
	static final String[] EMAILS_MISSING_MESSAGES = { MISSING_SENSOR_ID, MISSING_EMAILS };

	static final RangeDoc RANGE_DOC = new RangeDoc(ID, RANGE);
	static final SensorEmailsDoc EMAILS_DOC = new SensorEmailsDoc(ID, EMAILS);

	public void createDb() {
		mongoTemplate.findAllAndRemove(new Query(), RANGE_COLLECTION);
		mongoTemplate.findAllAndRemove(new Query(), EMAIL_COLLECTION);
		mongoTemplate.insert(RANGE_DOC, RANGE_COLLECTION);
		mongoTemplate.insert(EMAILS_DOC, EMAIL_COLLECTION);
	}

	public <T> T findById(Object id, Class<T> entity, String collection) {
		return mongoTemplate.findById(id, entity, collection);

	}

}
