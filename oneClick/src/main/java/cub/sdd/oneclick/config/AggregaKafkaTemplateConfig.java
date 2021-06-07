package cub.sdd.oneclick.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
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
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.AggregatingReplyingKafkaTemplate;

@Configuration
@EnableKafka
public class AggregaKafkaTemplateConfig {

	@Value("${cub.spring.kafka.bootstrap-servers}")
	private String servers;

	@Value("${cub.spring.kafka.query.topic.request}")
	private String queryRequestTopic;

	@Value("${cub.spring.kafka.query.topic.reply}")
	private String queryReplyTopic;

	@Value("${cub.spring.kafka.query.consumer.group-id}")
	private String cosumerGroupId;

	@Value("${cub.spring.kafka.query.consumer.reply.group-id}")
	private String replyGroupId;

	@Bean
	public AggregatingReplyingKafkaTemplate<Integer, String, String> replyingTemplate(
			ProducerFactory<Integer, String> pf,
			ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer) {

		AggregatingReplyingKafkaTemplate<Integer, String, String> template = new AggregatingReplyingKafkaTemplate<>(pf,
				repliesContainer, coll -> coll.size() == 3);

		template.setSharedReplyTopic(true);
		template.setReturnPartialOnTimeout(true);
//		template.start();
		return template;
	}

	@Bean("QueryConcurrentMessageListenerContainer")
	public ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer(
			ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> containerFactory) {

		ConcurrentMessageListenerContainer<Integer, Collection<ConsumerRecord<Integer, String>>> repliesContainer = containerFactory
				.createContainer(queryReplyTopic);
		repliesContainer.getContainerProperties().setGroupId(replyGroupId); // Overrides any `group.id` property
																			// provided
		// by the consumer factory configuration
		repliesContainer.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

		repliesContainer.setAutoStartup(false);
		return repliesContainer;
	}

	@Bean
	public KafkaAdmin admin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic queryRequestTopic() {
		return TopicBuilder.name(queryRequestTopic).partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic queryReplyTopic() {
		return TopicBuilder.name(queryReplyTopic).partitions(3).replicas(1).build();
	}

	@Bean("QueryConcurrentKafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Integer, Collection<ConsumerRecord<Integer, String>>> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	@Bean("QueryConsumerFactory")
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

	@Bean("QueryProducerFactory")
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