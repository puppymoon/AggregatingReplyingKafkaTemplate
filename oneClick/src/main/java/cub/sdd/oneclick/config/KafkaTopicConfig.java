package cub.sdd.oneclick.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@EnableKafka
public class KafkaTopicConfig {

	@Value("${cub.spring.kafka.bootstrap-servers}")
	private String servers;

//	@Value("${cub.spring.kafka.query.topic.request}")
//	private String queryRequestTopic;

//	@Value("${cub.spring.kafka.query.topic.reply}")
//	private String queryReplyTopic;

	@Bean
	public KafkaAdmin admin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic Topic1() {
		return TopicBuilder.name("request-jcic-etch-intr").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic2() {
		return TopicBuilder.name("request-jcic-etch").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic3() {
		return TopicBuilder.name("request-jcic-intr").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic4() {
		return TopicBuilder.name("request-etch-intr").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic5() {
		return TopicBuilder.name("request-jcic").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic6() {
		return TopicBuilder.name("request-etch").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic7() {
		return TopicBuilder.name("request-intr").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic8() {
		return TopicBuilder.name("reply-jcic-etch-intr").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic9() {
		return TopicBuilder.name("reply-jcic-etch").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic11() {
		return TopicBuilder.name("reply-jcic-intr").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic12() {
		return TopicBuilder.name("reply-etch-intr").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic13() {
		return TopicBuilder.name("reply-jcic").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic14() {
		return TopicBuilder.name("reply-etch").partitions(3).replicas(1).build();
	}

	@Bean
	public NewTopic Topic15() {
		return TopicBuilder.name("reply-intr").partitions(3).replicas(1).build();
	}

}
