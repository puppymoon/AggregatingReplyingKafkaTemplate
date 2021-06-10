package cub.sdd.oneclick.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CubKafkaListenerDouble {

	@KafkaListener(id = "CubKafkaListenerDouble1", topics = "request-jcic-etch")
	@SendTo("reply-jcic-etch")
	public String listener1(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("JCICAAA - Server received: {}", message);
		return "JCICAAA" + message;
	}

	@KafkaListener(id = "CubKafkaListenerDouble2", topics = "request-jcic-etch")
	@SendTo("reply-jcic-etch")
	public String listener2(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("ECTHAAA - Server received: {}", message);
		return "ECTHAAA" + message;
	}

	@KafkaListener(id = "CubKafkaListenerDouble3", topics = "request-jcic-intr")
	@SendTo("reply-jcic-intr")
	public String listener3(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("JCICAAA - Server received: {}", message);
		return "JCICAAA" + message;
	}

	@KafkaListener(id = "CubKafkaListenerDouble4", topics = "request-jcic-intr")
	@SendTo("reply-jcic-intr")
	public String listener4(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("INTRAAA - Server received: {}", message);
		return "INTRAAA" + message;
	}

	@KafkaListener(id = "CubKafkaListenerDouble5", topics = "request-etch-intr")
	@SendTo("reply-etch-intr")
	public String listener5(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("INTRAAA - Server received: {}", message);
		return "INTRAAA" + message;
	}

	@KafkaListener(id = "CubKafkaListenerDouble6", topics = "request-etch-intr")
	@SendTo("reply-etch-intr")
	public String listener6(String message) throws InterruptedException {
		Thread.sleep(2000);
		log.info("ECTHAAA - Server received: {}", message);
		return "ECTHAAA" + message;
	}

}
