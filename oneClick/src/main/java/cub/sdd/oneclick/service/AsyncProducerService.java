package cub.sdd.oneclick.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import cub.sdd.oneclick.dto.DataDto.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AsyncProducerService {

	@Value("${cub.spring.kafka.batch.topic.request}")
	private String batchRequestTopic;

	@Value("${cub.spring.kafka.batch.consumer.group-id}")
	private String batchConsumerGroupId;

	@Autowired
	@Qualifier("BatchKafkaTemplate")
	private KafkaTemplate<String, User> batchKafkaTemplate;

	@Async("asyncTaskExecutor")
	public void produce(User user) {

		batchKafkaTemplate.executeInTransaction(kafkaTemplate -> {

			ProducerRecord<String, User> producerRecord = new ProducerRecord<>(batchRequestTopic, user);
			ListenableFuture<SendResult<String, User>> listenableFuture = kafkaTemplate.send(producerRecord);

			listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, User>>() {

				@Override
				public void onSuccess(SendResult<String, User> result) {

					// 提交commit
					Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();
					RecordMetadata meta = result.getRecordMetadata();
					offsetsToCommit = Collections.singletonMap(new TopicPartition(meta.topic(), meta.partition()),
							new OffsetAndMetadata(meta.offset() + 1));
					log.info("onSuccess>>>>>>" + result.getProducerRecord().value());
					kafkaTemplate.sendOffsetsToTransaction(offsetsToCommit, batchConsumerGroupId);

				}

				@Override
				public void onFailure(Throwable throwable) {
					log.error("failed to send, message={}", user, throwable);
				}
			});

			return null;
		});

	}

}
