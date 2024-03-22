package telran.probes;

import telran.probes.dto.*;

public interface TestsConstants {
	String URL = "http://localhost:8383/range/sensor/";
	String SENSOR_NOT_FOUND_MESSAGE = "Sensor not found";
	long SENSOR_ID = 123;
	long SENSOR_ID_NOT_FOUND = 124;
	long SENSOR_ID_UNAVAILABLE = 170;

	double MIN_VALUE = 100;
	double MAX_VALUE = 200;
	double MIN_DEFAULT_VALUE = -1000d;
	double MAX_DEFAULT_VALUE = 1000d;
	double VALUE_NORMAL = 150;
	double VALUE_LESS_MIN = 50;
	double VALUE_GREATER_MAX = 220;
	
	Double DEVIATION_LESS_MIN = VALUE_LESS_MIN - MIN_VALUE;
	Double DEVIATION_GREATER_MAX = VALUE_GREATER_MAX - MAX_VALUE;
	
	Range RANGE = new Range(MIN_VALUE, MAX_VALUE);
	Range RANGE_DEFAULT = new Range(MIN_DEFAULT_VALUE, MAX_DEFAULT_VALUE);
	Range RANGE_UPDATED = new Range(MIN_VALUE + 10, MAX_VALUE + 10);

	ProbeData probeNormalData = new ProbeData(SENSOR_ID, VALUE_NORMAL, System.currentTimeMillis());
	ProbeData probeGreaterMaxData = new ProbeData(SENSOR_ID, VALUE_GREATER_MAX, System.currentTimeMillis());
	ProbeData probeLessMinData = new ProbeData(SENSOR_ID, VALUE_LESS_MIN, System.currentTimeMillis());
}
