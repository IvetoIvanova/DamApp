package bg.softuni.damapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DamAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DamAppApplication.class, args);
	}

}
