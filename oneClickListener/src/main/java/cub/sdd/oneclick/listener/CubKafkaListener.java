package cub.sdd.oneclick.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CubKafkaListener {

	@KafkaListener(id = "server-JCIC", topics = "${cub.spring.kafka.topic.query.request}")
	@SendTo("${cub.spring.kafka.topic.query.reply}")
	public String listenerA(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("JCICAAA - Server received: {}", message);
		return "JCICAAA" + message;
	}

	@KafkaListener(id = "server-ECTH", topics = "${cub.spring.kafka.topic.query.request}")
	@SendTo("${cub.spring.kafka.topic.query.reply}")
	public String listenerB(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("ECTHAAA - Server received: {}", message);
		return "ECTHAAA" + message;
	}

	@KafkaListener(id = "server-INTR", topics = "${cub.spring.kafka.topic.query.request}")
	@SendTo("${cub.spring.kafka.topic.query.reply}")
	public String listenerC(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("INTRAAA - Server received: {}", message);
		return "INTRAAA" + message;
	}

}
