package telran.probes.service;

import telran.probes.dto.*;

public interface ProbesService {
	ProbeData getProbeData();
	void updateCache(long id, Range range);

}
