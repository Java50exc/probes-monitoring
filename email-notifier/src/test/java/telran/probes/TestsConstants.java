package telran.probes;

public interface TestsConstants {
	String URL = "http://localhost:8282/emails/sensor/";
	String SENSOR_NOT_FOUND_MESSAGE = "Sensor not found";
	
	long SENSOR_ID = 123;
	long SENSOR_ID_NOT_FOUND = 124;
	long SENSOR_ID_UNAVAILABLE = 170;

	String EMAIL1 = "email1@gmail.com";
	String EMAIL2 = "email2@gmail.com";
	String EMAIL3 = "email3@gmail.com";
	String EMAIL4 = "email4@gmail.com";
	String EMAIL5 = "email5@gmail.com";
	String EMAIL_DEFAULT = "allanteone@icloud.com";
	
	String[] EMAILS = {EMAIL1, EMAIL2, EMAIL3};
	String[] EMAILS_UPDATED = {EMAIL4, EMAIL5};
	String[] EMAILS_DEFAULT = {EMAIL_DEFAULT};
}
