package cub.sdd.oneclick.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.AggregatingReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cub.sdd.oneclick.DataCache;
import cub.sdd.oneclick.service.AsyncProducerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
public class TestController {

	@Value("${cub.spring.kafka.query.topic.request}")
	private String queryRequestTopic;

	@Autowired
	private AsyncProducerService asyncProducerService;

	@Autowired
	private DataCache dataCache;

	@Autowired
//	@Qualifier("OneClickAggregatingReplyingKafkaTemplate")
	private AggregatingReplyingKafkaTemplate<Integer, String, String> template;

	@PostMapping(path = "/sendBatchQuery")
	public void sendBatchQuery() {

		List<String> list = new ArrayList<>();
		list.add("moontea");
		list.add("wen");
		list.add("frank");
		for (String userId : list) {
			asyncProducerService.produce(userId);
		}

	}

	@PostMapping(path = "/sendQuery")
	public List<String> sendQuery(String userId) {

		try {

			template.setDefaultReplyTimeout(Duration.ofSeconds(30));

			ProducerRecord<Integer, String> record = new ProducerRecord<>(queryRequestTopic, null, null, null, userId);
			RequestReplyFuture<Integer, String, Collection<ConsumerRecord<Integer, String>>> future = template
					.sendAndReceive(record);
			future.getSendFuture().get(30, TimeUnit.SECONDS); // send ok

			ConsumerRecord<Integer, Collection<ConsumerRecord<Integer, String>>> consumerRecord = future.get(30,
					TimeUnit.SECONDS);
			List<String> list = new ArrayList<>();
			consumerRecord.value().forEach(x -> {
				log.info("Return value: {}", x.value());
				list.add(x.value());
			});
			return list;
		} catch (Exception ex) {
			log.error("Something went wrong! Kafka reply timed out!", ex);
		}
		return null;
	}

	@PostMapping(path = "/clearCache")
	public void clearCache() {
		dataCache.clearCache();
	}
}
