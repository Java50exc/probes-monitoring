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
	private int pollCount;
	@Value("${spring.integration.poller.fixedDelay}")
	private int delay;
	private volatile static int curCount = 0;

	public static void main(String[] args) throws Exception {
		var ctx = SpringApplication.run(ProbesAppl.class, args);
		ProbesAppl probesAppl = ctx.getBean(ProbesAppl.class);
		synchronized (probesAppl) {
			while (probesAppl.pollCount == 0 || curCount < probesAppl.pollCount) {
				probesAppl.wait(probesAppl.delay / 10);
			}
		}
		ctx.close();
	}

	@Bean
	Supplier<ProbeData> probesSupplier() {
		return () -> probeGeneration();
	}

	private synchronized ProbeData probeGeneration() {
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
