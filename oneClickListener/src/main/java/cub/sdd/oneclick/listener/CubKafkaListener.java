package cub.sdd.oneclick.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CubKafkaListener {

	@KafkaListener(id = "CubKafkaListener1", topics = "request-jcic-etch-intr")
	@SendTo("reply-jcic-etch-intr")
	public String listenerA(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("JCICAAA - Server received: {}", message);
		return "JCICAAA" + message;
	}

	@KafkaListener(id = "CubKafkaListener2", topics = "request-jcic-etch-intr")
	@SendTo("reply-jcic-etch-intr")
	public String listenerB(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("ECTHAAA - Server received: {}", message);
		return "ECTHAAA" + message;
	}

	@KafkaListener(id = "CubKafkaListener3", topics = "request-jcic-etch-intr")
	@SendTo("reply-jcic-etch-intr")
	public String listenerC(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("INTRAAA - Server received: {}", message);
		return "INTRAAA" + message;
	}

}
