package telran.probes.service;

public interface EmailsProviderClientService {
	String[] getMails(long sensorId);
	String[] updateCache(long sensorId, String[] emails);

}
