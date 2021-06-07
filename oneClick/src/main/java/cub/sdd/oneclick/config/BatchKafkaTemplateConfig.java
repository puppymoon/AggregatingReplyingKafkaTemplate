package cub.sdd.oneclick.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;

@Configuration
@EnableKafka
public class BatchKafkaTemplateConfig {

	@Value("${cub.spring.kafka.bootstrap-servers}")
	private String servers;

	@Value("${cub.spring.kafka.batch.consumer.group-id}")
	private String batchConsumerGroupId;

	@Value("${cub.spring.kafka.batch.topic.request}")
	private String batchRequestTopic;

	@Value("${cub.spring.kafka.batch.topic.reply}")
	private String batchReplyTopic;

	@Value("${cub.spring.kafka.batch.producer.prefix}")
	private String prefix;

	@Bean("BatchConsumerConfigs")
	public Map<String, Object> consumerConfigs() {
		UUID uuid = UUID.randomUUID();
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, batchConsumerGroupId);
		props.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, uuid.toString());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
		return props;
	}

	@Bean("BatchConsumerFactory")
	public ConsumerFactory<String, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
//		return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
//				new JsonDeserializer<>(Message.class));
	}

	@Bean("BatchContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.getContainerProperties().setPollTimeout(1500);

		// 配置手動提交
//		factory.setBatchListener(true);
		factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
//		factory.getContainerProperties().setAckMode(AckMode.MANUAL);
		return factory;
	}

	@Bean("BatchProducerConfigs")
	public Map<String, Object> producerConfigs() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
//		configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "prod-1");
		configProps.put(ProducerConfig.ACKS_CONFIG, "all");
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return configProps;
	}

	@Bean("BatchProducerFactory")
	public ProducerFactory<String, String> producerFactory() {
		DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(producerConfigs());
		factory.setTransactionIdPrefix(prefix);
		return factory;
	}

	@Bean("BatchKafkaTemplate")
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public NewTopic batchRequestTopic() {
		return TopicBuilder.name(batchRequestTopic).partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic batchReplyTopic() {
		return TopicBuilder.name(batchReplyTopic).partitions(3).replicas(1).build();
	}

}
