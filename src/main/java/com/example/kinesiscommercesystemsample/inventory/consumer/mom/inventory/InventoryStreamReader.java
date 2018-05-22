package com.example.kinesiscommercesystemsample.inventory.consumer.mom.inventory;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.example.kinesiscommercesystemsample.common.util.ConfigurationUtils;
import com.example.kinesiscommercesystemsample.common.util.CredentialUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class InventoryStreamReader {

	@Value("${application.kinesis.inventory.stream-name}")
	private String streamName;

	@Value("${application.kinesis.inventory.application-name}")
	private String applicationName;

	@Value("${application.kinesis.inventory.region-name}")
	private String regionName;

	@Value("${application.kinesis.inventory.idle-time-between-reads-in-millis}")
	private long idleTimeBetweenReadsInMillis;

	@Autowired
	private InventoryRecordProcessorFactory inventoryRecordProcessorFactory;

	public void run() throws Exception {

		Region region = RegionUtils.getRegion(regionName);

		AWSCredentialsProvider credentialsProvider = CredentialUtils.getCredentialsProvider();

		String workerId = String.valueOf(UUID.randomUUID());

		ZonedDateTime startPosition = ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(60 * 60 *24);

		Date timestampAtInitialPositionInStream = Date.from(startPosition.toInstant());

		KinesisClientLibConfiguration kclConfig =
				new KinesisClientLibConfiguration(applicationName, streamName, credentialsProvider, workerId)
						//.withInitialPositionInStream(InitialPositionInStream.AT_TIMESTAMP) // InitialPositionInStream.AT_TIMESTAMP (指定時間より後の読み取り)の場合は必要なし。withTimestampAtInitialPositionInStreamのみで良い。
						.withTimestampAtInitialPositionInStream(timestampAtInitialPositionInStream)
						.withRegionName(region.getName())
						.withCommonClientConfig(ConfigurationUtils.getClientConfigWithUserAgent(applicationName))
						.withIdleTimeBetweenReadsInMillis(idleTimeBetweenReadsInMillis);

		Worker worker = new Worker(inventoryRecordProcessorFactory, kclConfig);

		worker.run();
	}
}
