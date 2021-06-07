package cub.sdd.oneclick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class OneclickListenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneclickListenerApplication.class, args);
	}

}
