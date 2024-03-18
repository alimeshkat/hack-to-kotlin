package nl.alimeshkat.recipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RecipesApplication {
	public static void main(String[] args) {
		SpringApplication.run(RecipesApplication.class, args);
	}

}
