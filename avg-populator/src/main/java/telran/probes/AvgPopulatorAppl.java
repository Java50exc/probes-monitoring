package telran.probes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;

@SpringBootApplication
@Slf4j
public class AvgPopulatorAppl {
	private Table table;
	
	@Value("${app.avg.populator.tablename}")
	private String tableName;
	@Value("${app.avg.populator.sensor.id}")
	private String idField;
	@Value("${app.avg.populator.timestamp}")
	private String timestamp;
	@Value("${app.avg.populator.value}")
	private String value;
	
	public static void main(String[] args) {
		SpringApplication.run(AvgPopulatorAppl.class, args);
	}
	
	@Bean
	Consumer<ProbeData> avgPopulatorConsumer() {
		return probeData -> probeDataPopulation(probeData);
	}
	

	private void probeDataPopulation(ProbeData probeData) {
		Map<String, Object> dbRecord = new HashMap<>();
		dbRecord.put(idField, probeData.id());
		dbRecord.put(timestamp, getDateTime(probeData.timestamp()));
		dbRecord.put(value, probeData.value());
		table.putItem(new PutItemSpec().withItem(Item.fromMap(dbRecord)));
		log.debug("avg probe data for sensor {} has been saved", probeData.id());
	}
	
	private String getDateTime(long timestamp) {
		return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
	
	
	@PostConstruct
	private void initTable() {
		DynamoDB dynamo = new DynamoDB(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build());
		table = dynamo.getTable(tableName);
	}
	

}
