package cub.sdd.oneclick.controller;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.requestreply.AggregatingReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cub.sdd.oneclick.DataCache;
import cub.sdd.oneclick.dto.DataDto.User;
import cub.sdd.oneclick.service.AsyncProducerService;
import cub.sdd.oneclick.service.KafkaTemplateUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
public class TestController {

	@Autowired
	private AsyncProducerService asyncProducerService;

	@Autowired
	private DataCache dataCache;

//	@Autowired
//	@Qualifier("OneClickAggregatingReplyingKafkaTemplate")
//	private AggregatingReplyingKafkaTemplate<Integer, String, String> template;

	@Autowired
	@Qualifier("AggregatingReplyingKafkaTemplate1")
	private AggregatingReplyingKafkaTemplate<Integer, String, String> template1;

	@Autowired
	@Qualifier("AggregatingReplyingKafkaTemplate2")
	private AggregatingReplyingKafkaTemplate<Integer, String, String> template2;

	@Autowired
	@Qualifier("AggregatingReplyingKafkaTemplate3")
	private AggregatingReplyingKafkaTemplate<Integer, String, String> template3;

	@Autowired
	@Qualifier("AggregatingReplyingKafkaTemplate4")
	private AggregatingReplyingKafkaTemplate<Integer, String, String> template4;

	@Autowired
	@Qualifier("AggregatingReplyingKafkaTemplate5")
	private AggregatingReplyingKafkaTemplate<Integer, String, String> template5;

	@Autowired
	@Qualifier("AggregatingReplyingKafkaTemplate6")
	private AggregatingReplyingKafkaTemplate<Integer, String, String> template6;

	@Autowired
	@Qualifier("AggregatingReplyingKafkaTemplate7")
	private AggregatingReplyingKafkaTemplate<Integer, String, String> template7;

	@Autowired
	private KafkaTemplateUtils kafkaTemplateUtils;

	@PostMapping(path = "/sendBatchQuery")
	public void sendBatchQuery(List<User> list) {

		for (User user : list) {
			asyncProducerService.produce(user);
		}

	}

	@PostMapping(path = "/sendQuery")
	public List<String> sendQuery(User user) {

		AggregatingReplyingKafkaTemplate<Integer, String, String> template = null;
		try {

			template = decideAggregaTemplate(user);

			template.setDefaultReplyTimeout(Duration.ofSeconds(30));

			ProducerRecord<Integer, String> record = new ProducerRecord<>(decideRequestTopic(user), null, null, null,
					user.getUserId());
			RequestReplyFuture<Integer, String, Collection<ConsumerRecord<Integer, String>>> future = template
					.sendAndReceive(record);
			future.getSendFuture().get(30, TimeUnit.SECONDS); // send ok

			ConsumerRecord<Integer, Collection<ConsumerRecord<Integer, String>>> consumerRecord = future.get(30,
					TimeUnit.SECONDS);
			List<String> list = new ArrayList<>();
			consumerRecord.value().forEach(x -> {
				log.info("SendQuery Return value: {}", x.value());
				log.info("SendQuery Return topic: {}", x.topic());
				list.add(x.value());
			});
			return list;
		} catch (Exception ex) {
			log.error("Something went wrong! Kafka reply timed out!", ex);
		}
		return null;
	}

	private String decideRequestTopic(User user) {

		String topic1 = user.getQueryTopic1();
		String topic2 = user.getQueryTopic2();
		String topic3 = user.getQueryTopic3();

		if (topic1 != null && topic2 != null && topic3 != null) {
			return "request-jcic-etch-intr";
		} else if (topic1 != null && topic2 != null) {
			return "request-jcic-etch";
		} else if (topic1 != null && topic3 != null) {
			return "request-jcic-intr";
		} else if (topic2 != null && topic3 != null) {
			return "request-etch-intr";
		} else if (topic1 != null) {
			return "request-jcic";
		} else if (topic2 != null) {
			return "request-etch";
		} else if (topic3 != null) {
			return "request-intr";
		}
		throw new RuntimeException("decideTopic failed!");
	}

	private AggregatingReplyingKafkaTemplate<Integer, String, String> decideAggregaTemplate(User user) {

		String topic1 = user.getQueryTopic1();
		String topic2 = user.getQueryTopic2();
		String topic3 = user.getQueryTopic3();

		if (topic1 != null && topic2 != null && topic3 != null) {
			return template1;
		} else if (topic1 != null && topic2 != null) {
			return template2;
		} else if (topic1 != null && topic3 != null) {
			return template3;
		} else if (topic2 != null && topic3 != null) {
			return template4;
		} else if (topic1 != null) {
			return template5;
		} else if (topic2 != null) {
			return template6;
		} else if (topic3 != null) {
			return template7;
		}

		throw new RuntimeException("decideTopic failed!");
	}

	@PostMapping(path = "/clearCache")
	public void clearCache() {
		dataCache.clearCache();
	}
}
