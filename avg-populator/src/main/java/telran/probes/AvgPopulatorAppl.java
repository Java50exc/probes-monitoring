package telran.probes;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;
import telran.probes.model.ProbeDataDoc;
import telran.probes.repo.AvgPopulatorRepo;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class AvgPopulatorAppl {
	final AvgPopulatorRepo avgPopulatorRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(AvgPopulatorAppl.class, args);
	}
	
	@Bean
	Consumer<ProbeData> avgPopulatorConsumer() {
		return probeData -> probeDataPopulation(probeData);
	}

	private void probeDataPopulation(ProbeData probeData) {
		avgPopulatorRepo.save(new ProbeDataDoc(probeData));
		log.debug("avg probe data for sensor {} has been saved", probeData.id());
	}

}
