package telran.probes.service;

public interface EmailsProviderClientService {
	String EMAIL_DEFAULT = "default@gmail.com";
	
	String[] getMails(long sensorId);
	String[] updateCache(long sensorId, String[] emails);

}
