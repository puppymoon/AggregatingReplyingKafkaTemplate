package cub.sdd.oneclick.service;

import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.AggregatingReplyingKafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTemplateUtils {

	@Autowired
	private ProducerFactory<Integer, String> pf;

	@Autowired
	private ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> containerFactory;

	public AggregatingReplyingKafkaTemplate<Integer, String, String> replyingTemplate(String queryReplyTopic,
			String replyGroupId, int colSize) {

		ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer = containerFactory
				.createContainer(queryReplyTopic);
		repliesContainer.getContainerProperties().setGroupId(replyGroupId); // Overrides any `group.id` property
																			// provided
		// by the consumer factory configuration
		repliesContainer.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

		repliesContainer.setAutoStartup(false);
//		return repliesContainer;

		AggregatingReplyingKafkaTemplate<Integer, String, String> template = new AggregatingReplyingKafkaTemplate<>(pf,
				repliesContainer, coll -> coll.size() == colSize);

		template.setSharedReplyTopic(true);
		template.setReturnPartialOnTimeout(true);
		template.start();
		return template;
	}

}
