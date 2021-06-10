package cub.sdd.oneclick.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CubKafkaListenerSingle {

	@KafkaListener(id = "CubKafkaListenerSingle1", topics = "request-jcic")
	@SendTo("reply-jcic")
	public String listenerA(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("JCICAAA - Server received: {}", message);
		return "JCICAAA" + message;
	}

	@KafkaListener(id = "CubKafkaListenerSingle2", topics = "request-etch")
	@SendTo("reply-etch")
	public String listenerB(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("ECTHAAA - Server received: {}", message);
		return "ECTHAAA" + message;
	}

	@KafkaListener(id = "CubKafkaListenerSingle3", topics = "request-intr")
	@SendTo("reply-intr")
	public String listenerC(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("INTRAAA - Server received: {}", message);
		return "INTRAAA" + message;
	}

}
