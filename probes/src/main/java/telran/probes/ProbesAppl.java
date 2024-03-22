package telran.probes;

import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import telran.probes.dto.ProbeData;
import telran.probes.service.ProbesService;

@SpringBootApplication
@RequiredArgsConstructor
public class ProbesAppl {
	private static int TIMEOUT = 11000;
	
	final ProbesService probesService;
	
	public static void main(String[] args) throws Exception {
		var ctx = SpringApplication.run(ProbesAppl.class, args);
		Thread.sleep(TIMEOUT);
		ctx.close();
	}
	
	
	@Bean
	Supplier<ProbeData> probesSupplier() {
		return () -> probeGeneration();
	}


	private ProbeData probeGeneration() {
		return probesService.getProbeData();
	}
	

}
