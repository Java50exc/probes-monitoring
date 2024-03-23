package telran.probes;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import telran.probes.dto.ProbeData;
import telran.probes.dto.SensorUpdateData;
import telran.probes.service.ProbesService;

@SpringBootApplication
@RequiredArgsConstructor
public class ProbesAppl {
	final ProbesService probesService;
	@Value("${app.probes.polling.count}")
	private int POLL_COUNT;
	private volatile static int curCount = 0;
	
	
	
	public static void main(String[] args) throws Exception {
		var ctx = SpringApplication.run(ProbesAppl.class, args);
		ProbesAppl probesAppl = ctx.getBean(ProbesAppl.class);
		while (probesAppl.POLL_COUNT == 0 || curCount < probesAppl.POLL_COUNT) {
			Thread.sleep(100);
		}
		ctx.close();
	}
	
	
	@Bean
	Supplier<ProbeData> probesSupplier() {
		return () -> probeGeneration();
	}

	private ProbeData probeGeneration() {
		curCount++;
		return probesService.getProbeData();
	}
	
	@Bean
	Consumer<SensorUpdateData> updateProbesConsumer() {
		return updateData -> updateProcessing(updateData);
	}

	private void updateProcessing(SensorUpdateData updateData) {
		if (updateData.range() != null) {
			probesService.updateCache(updateData.id(), updateData.range());
		}
	}
	

}
