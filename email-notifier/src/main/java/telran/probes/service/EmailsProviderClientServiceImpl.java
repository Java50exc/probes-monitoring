package telran.probes.service;

import java.util.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailsProviderClientServiceImpl implements EmailsProviderClientService {
	private HashMap<Long, String[]> cache = new HashMap<>();
	final RestTemplate restTemplate;
	final ServiceConfiguration serviceConfiguration;

	@Override
	public String[] getMails(long sensorId) {
		String[] emails;
		try {
			emails = cache.computeIfAbsent(sensorId, (id) -> {
				String[] response = serviceRequest(id);
				log.debug("received from remote service range value: {}", Arrays.toString(response));
				return response;
			});
			log.debug("received range value: {}", Arrays.toString(emails));
		} catch (Exception e) {
			log.error("error at service request: {}", e.getMessage());
			emails = new String[] { serviceConfiguration.defaultEmail };
			log.warn("default range value: {}", Arrays.toString(emails));
		}
		return emails;
	}

	@Override
	public String[] updateCache(long sensorId, String[] emails) {
		return cache.put(sensorId, emails);
	}

	private String[] serviceRequest(long sensorId) {
		String[] emails = null;
		ResponseEntity<?> responseEntity = restTemplate.exchange(getUrl(sensorId), HttpMethod.GET, null,
				String[].class);

		if (responseEntity.getStatusCode().is4xxClientError()) {
			throw new RuntimeException(responseEntity.getBody().toString());
		}

		emails = (String[]) responseEntity.getBody();
		return emails;
	}

	private String getUrl(long sensorId) {
		String url = String.format("http://%s:%d/%s/%d", serviceConfiguration.getHost(), serviceConfiguration.getPort(),
				serviceConfiguration.getPath(), sensorId);
		log.debug("url created is {}", url);
		return url;
	}

}
