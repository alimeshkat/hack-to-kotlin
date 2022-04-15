package nl.rabobank.kotlinmovement.pizzarecipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PizzaRecipeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaRecipeApplication.class, args);
	}

}
