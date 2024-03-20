package telran.probes.service;

import telran.probes.dto.Range;

public interface RangeProviderClientService {
	Double MIN_DEFAULT_VALUE = -100d;
	Double MAX_DEFAULT_VALUE = 100d;

	Range getRange(long sensorId);

}
