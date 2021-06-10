package cub.sdd.oneclick.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import cub.sdd.oneclick.DataCache;
import cub.sdd.oneclick.controller.TestController;
import cub.sdd.oneclick.dto.DataDto.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BatchListener {

	@Autowired
	private TestController testController;

	@Autowired
	private DataCache dataCache;

	@KafkaListener(topics = "${cub.spring.kafka.batch.topic.request}", groupId = "${cub.spring.kafka.batch.consumer.group-id}", containerFactory = "BatchContainerFactory")
	public void listen(User user, Acknowledgment ack) {
		log.info("BatchListener recieve message, userId : " + user.getUserId());
		List<List<String>> list = dataCache.getList();
		List<String> returnList = testController.sendQuery(user);
		if(null != returnList) {
			list.add(returnList);
		}

		// TODO use redis to record
		if (list.size() >= 3) {
			print(list);
		}
		ack.acknowledge();
	}


	public void print(List<List<String>> list) {
		log.info(">>>>>>>>>>>>Start");
		for (List<String> list2 : list) {
			list2.forEach(str -> log.info(str));
		}
		log.info(">>>>>>>>>>>>Finish");
	}

}
