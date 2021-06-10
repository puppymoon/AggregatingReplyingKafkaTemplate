package cub.sdd.oneclick.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.AggregatingReplyingKafkaTemplate;

@Configuration
@EnableKafka
public class AggregaKafkaTemplateConfig {

	@Value("${cub.spring.kafka.bootstrap-servers}")
	private String servers;

//	@Value("${cub.spring.kafka.query.topic.reply}")
//	private String queryReplyTopic;

	@Value("${cub.spring.kafka.query.consumer.group-id}")
	private String cosumerGroupId;

	@Value("${cub.spring.kafka.query.consumer.reply.group-id}")
	private String replyGroupId;

//	@Bean
//	public AggregatingReplyingKafkaTemplate<Integer, String, String> replyingTemplate(
//			ProducerFactory<Integer, String> pf,
//			ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer) {
//
//		AggregatingReplyingKafkaTemplate<Integer, String, String> template = new AggregatingReplyingKafkaTemplate<>(pf,
//				repliesContainer, coll -> coll.size() == 3);
//
//		template.setSharedReplyTopic(true);
//		template.setReturnPartialOnTimeout(true);
////		template.start();
//		return template;
//	}

//	@Bean("QueryConcurrentMessageListenerContainer")
//	public ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer(
//			ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> containerFactory) {
//
//		ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer = containerFactory
//				.createContainer(queryReplyTopic);
//		repliesContainer.getContainerProperties().setGroupId(replyGroupId); // Overrides any `group.id` property
//																			// provided
//		// by the consumer factory configuration
//		repliesContainer.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
//
//		repliesContainer.setAutoStartup(false);
//		return repliesContainer;
//	}
//
//	@Bean("QueryConcurrentKafkaListenerContainerFactory")
	@Bean
	public ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	@Bean("AggregatingReplyingKafkaTemplate1")
	public AggregatingReplyingKafkaTemplate<Integer, String, String> replyingTemplate1(ProducerFactory<Integer, String> pf) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "AggregatingReplyingKafkaTemplate1");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));

//		String queryReplyTopic,
//		String replyGroupId, int colSize
		ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer = containerFactory
				.createContainer("reply-jcic-etch-intr");
		repliesContainer.getContainerProperties().setGroupId("group-jcic-etch-intr"); // Overrides any `group.id`
																						// property
		// provided
		// by the consumer factory configuration
		repliesContainer.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

		repliesContainer.setAutoStartup(false);
//		return repliesContainer;

		AggregatingReplyingKafkaTemplate<Integer, String, String> template = new AggregatingReplyingKafkaTemplate<>(pf,
				repliesContainer, coll -> coll.size() == 3);

		template.setSharedReplyTopic(true);
		template.setReturnPartialOnTimeout(true);
//		template.start();
		return template;
	}
	
	@Bean("AggregatingReplyingKafkaTemplate2")
	public AggregatingReplyingKafkaTemplate<Integer, String, String> replyingTemplate2(ProducerFactory<Integer, String> pf) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "AggregatingReplyingKafkaTemplate2");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));

//		String queryReplyTopic,
//		String replyGroupId, int colSize
		ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer = containerFactory
				.createContainer("reply-jcic-etch");
		repliesContainer.getContainerProperties().setGroupId("group-jcic-etch"); // Overrides any `group.id`
																						// property
		// provided
		// by the consumer factory configuration
		repliesContainer.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

		repliesContainer.setAutoStartup(false);
//		return repliesContainer;

		AggregatingReplyingKafkaTemplate<Integer, String, String> template = new AggregatingReplyingKafkaTemplate<>(pf,
				repliesContainer, coll -> coll.size() == 2);

		template.setSharedReplyTopic(true);
		template.setReturnPartialOnTimeout(true);
//		template.start();
		return template;
	}

	@Bean("AggregatingReplyingKafkaTemplate3")
	public AggregatingReplyingKafkaTemplate<Integer, String, String> replyingTemplate3(ProducerFactory<Integer, String> pf) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "AggregatingReplyingKafkaTemplate3");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));

//		String queryReplyTopic,
//		String replyGroupId, int colSize
		ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer = containerFactory
				.createContainer("reply-jcic-intr");
		repliesContainer.getContainerProperties().setGroupId("group-jcic-intr"); // Overrides any `group.id`
																						// property
		// provided
		// by the consumer factory configuration
		repliesContainer.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

		repliesContainer.setAutoStartup(false);
//		return repliesContainer;

		AggregatingReplyingKafkaTemplate<Integer, String, String> template = new AggregatingReplyingKafkaTemplate<>(pf,
				repliesContainer, coll -> coll.size() == 2);

		template.setSharedReplyTopic(true);
		template.setReturnPartialOnTimeout(true);
//		template.start();
		return template;
	}

	@Bean("AggregatingReplyingKafkaTemplate4")
	public AggregatingReplyingKafkaTemplate<Integer, String, String> replyingTemplate4(ProducerFactory<Integer, String> pf) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "AggregatingReplyingKafkaTemplate4");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));

//		String queryReplyTopic,
//		String replyGroupId, int colSize
		ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer = containerFactory
				.createContainer("reply-etch-intr");
		repliesContainer.getContainerProperties().setGroupId("group-etch-intr"); // Overrides any `group.id`
																						// property
		// provided
		// by the consumer factory configuration
		repliesContainer.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

		repliesContainer.setAutoStartup(false);
//		return repliesContainer;

		AggregatingReplyingKafkaTemplate<Integer, String, String> template = new AggregatingReplyingKafkaTemplate<>(pf,
				repliesContainer, coll -> coll.size() == 2);

		template.setSharedReplyTopic(true);
		template.setReturnPartialOnTimeout(true);
//		template.start();
		return template;
	}

	@Bean("AggregatingReplyingKafkaTemplate5")
	public AggregatingReplyingKafkaTemplate<Integer, String, String> replyingTemplate5(ProducerFactory<Integer, String> pf) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "AggregatingReplyingKafkaTemplate5");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));

//		String queryReplyTopic,
//		String replyGroupId, int colSize
		ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer = containerFactory
				.createContainer("reply-jcic");
		repliesContainer.getContainerProperties().setGroupId("group-jcic"); // Overrides any `group.id`
																						// property
		// provided
		// by the consumer factory configuration
		repliesContainer.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

		repliesContainer.setAutoStartup(false);
//		return repliesContainer;

		AggregatingReplyingKafkaTemplate<Integer, String, String> template = new AggregatingReplyingKafkaTemplate<>(pf,
				repliesContainer, coll -> coll.size() == 1);

		template.setSharedReplyTopic(true);
		template.setReturnPartialOnTimeout(true);
//		template.start();
		return template;
	}
	
	@Bean("AggregatingReplyingKafkaTemplate6")
	public AggregatingReplyingKafkaTemplate<Integer, String, String> replyingTemplate6(ProducerFactory<Integer, String> pf) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "AggregatingReplyingKafkaTemplate6");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));

//		String queryReplyTopic,
//		String replyGroupId, int colSize
		ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer = containerFactory
				.createContainer("reply-etch");
		repliesContainer.getContainerProperties().setGroupId("group-etch"); // Overrides any `group.id`
																						// property
		// provided
		// by the consumer factory configuration
		repliesContainer.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

		repliesContainer.setAutoStartup(false);
//		return repliesContainer;

		AggregatingReplyingKafkaTemplate<Integer, String, String> template = new AggregatingReplyingKafkaTemplate<>(pf,
				repliesContainer, coll -> coll.size() == 1);

		template.setSharedReplyTopic(true);
		template.setReturnPartialOnTimeout(true);
//		template.start();
		return template;
	}
	
	@Bean("AggregatingReplyingKafkaTemplate7")
	public AggregatingReplyingKafkaTemplate<Integer, String, String> replyingTemplate7(ProducerFactory<Integer, String> pf) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "AggregatingReplyingKafkaTemplate7");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));

//		String queryReplyTopic,
//		String replyGroupId, int colSize
		ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer = containerFactory
				.createContainer("reply-intr");
		repliesContainer.getContainerProperties().setGroupId("group-intr"); // Overrides any `group.id`
																						// property
		// provided
		// by the consumer factory configuration
		repliesContainer.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

		repliesContainer.setAutoStartup(false);
//		return repliesContainer;

		AggregatingReplyingKafkaTemplate<Integer, String, String> template = new AggregatingReplyingKafkaTemplate<>(pf,
				repliesContainer, coll -> coll.size() == 1);

		template.setSharedReplyTopic(true);
		template.setReturnPartialOnTimeout(true);
//		template.start();
		return template;
	}

//	@Bean("QueryConsumerFactory")
	@Bean
	public ConsumerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}

	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, cosumerGroupId);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return props;
	}

//	@Bean
//	public KafkaProperties.Listener listener() {
//		return new KafkaProperties.Listener();
//	}

//	@Bean("QueryProducerFactory")
	@Bean
	public ProducerFactory<Integer, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return props;
	}

}