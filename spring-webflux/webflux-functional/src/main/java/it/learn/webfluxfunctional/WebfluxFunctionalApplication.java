package it.learn.webfluxfunctional;

import it.learn.webfluxfunctional.product.Product;
import it.learn.webfluxfunctional.product.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class WebfluxFunctionalApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxFunctionalApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ReactiveMongoOperations operations, ProductRepository repository) {
        return args -> {
            Flux<Product> productFlux = Flux.just(
                    new Product(null, "Big Latte", 2.99),
                    new Product(null, "Big Decaf", 2.49),
                    new Product(null, "Green Tea", 1.99))
                    .flatMap(repository::save);

            productFlux
                    .thenMany(repository.findAll())
                    .subscribe(System.out::println);

            /*operations.collectionExists(Product.class)
                    .flatMap(exists -> exists ? operations.dropCollection(Product.class) : Mono.just(exists))
					.thenMany(v -> operations.createCollection(Product.class))
					.thenMany(productFlux)
					.thenMany(repository.findAll())
					.subscribe(System.out::println);*/
        };
    }

}