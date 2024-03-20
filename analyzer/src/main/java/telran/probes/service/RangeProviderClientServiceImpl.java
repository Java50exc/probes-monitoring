package telran.probes.service;

import java.util.HashMap;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RangeProviderClientServiceImpl implements RangeProviderClientService {
	private HashMap<Long, Range> cache = new HashMap<>();
	final RestTemplate restTemplate;
	final ServiceConfiguration serviceConfiguration;

	@Override
	public Range getRange(long sensorId) {
		Range range;
		try {
			range = cache.computeIfAbsent(sensorId, (id) -> {
				Range response = serviceRequest(id);
				log.debug("received from remote service range value: {}", response);
				return response;
			});
			log.debug("received range value: {}", range);
		} catch (Exception e) {
			log.error("error at service request: {}", e.getMessage());
			range = new Range(MIN_DEFAULT_VALUE, MAX_DEFAULT_VALUE);
			log.warn("default range value: {}", range);
		}
		return range;
	}

	@Override
	public Range updateCache(long id, Range range) {
		return cache.put(id, range);
	}

	private Range serviceRequest(long sensorId) {
		Range range = null;
		ResponseEntity<?> responseEntity = restTemplate.exchange(getUrl(sensorId), HttpMethod.GET, null, Range.class);

		if (responseEntity.getStatusCode().is4xxClientError()) {
			throw new RuntimeException(responseEntity.getBody().toString());
		}

		range = (Range) responseEntity.getBody();
		return range;
	}

	private String getUrl(long sensorId) {
		String url = String.format("http://%s:%d%s%d", serviceConfiguration.getHost(), serviceConfiguration.getPort(),
				serviceConfiguration.getPath(), sensorId);
		log.debug("url created is {}", url);
		return url;
	}

}
