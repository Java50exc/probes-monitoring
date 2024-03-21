package telran.probes;

import telran.probes.dto.ProbeData;

public interface TestsConstants {
	Double VALUE = 100.0;
	long SENSOR_ID = 123;
	ProbeData PROBE_DATA = new ProbeData(SENSOR_ID, VALUE, 0);
	
}
