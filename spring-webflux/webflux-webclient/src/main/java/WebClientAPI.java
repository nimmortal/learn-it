import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WebClientAPI {
    private WebClient webClient;

    private WebClientAPI() {
        //this.webClient = WebClient.create("http://localhost:8080/products");
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/products")
                .build();
    }

    public static void main(String args[]) {
        WebClientAPI api = new WebClientAPI();

        // TODO: 24.04.2018. Why requests not working until blocking?

//        System.out.println(api.getAllProducts().blockFirst());

//        api.getAllProducts().subscribe(System.out::println);

        api.postNewProduct()
                .thenMany(api.getAllProducts())
                .take(1)
                .flatMap(p -> api.updateProduct(p.getId(), "White Tea", 0.99))
                .flatMap(p -> api.deleteProduct(p.getId()))
                .thenMany(api.getAllProducts())
                .thenMany(api.getAllEvents())
                .subscribe(System.out::println);


//        try {
//            Thread.sleep(5000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        /*
        Program output
        [DEBUG] (main) Using Console logging
        [DEBUG] (main) Default Epoll support : false
        [DEBUG] (main) Default KQueue support : false
        [DEBUG] (main) New http client pool for localhost/127.0.0.1:8080
        [DEBUG] (main) Acquiring existing channel from pool: DefaultPromise@4d154ccd(incomplete) SimpleChannelPool{activeConnections=0}
        */
    }

    private Mono<ResponseEntity<Product>> postNewProduct() {
        return webClient
                .post()
                .body(Mono.just(new Product(null, "Black Tea", 1.99)), Product.class)
                .exchange()
                .flatMap(response -> response.toEntity(Product.class))
                .doOnSuccess(o -> System.out.println("**********POST " + o));
    }

    private Flux<Product> getAllProducts() {
        return webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(o -> System.out.println("**********GET: " + o));
    }

    private Mono<Product> updateProduct(String id, String name, double price) {
        return webClient
                .put()
                .uri("/{id}", id)
                .body(Mono.just(new Product(null, name, price)), Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnSuccess(o -> System.out.println("**********UPDATE " + o));
    }

    private Mono<Void> deleteProduct(String id) {
        return webClient
                .delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(o -> System.out.println("**********DELETE " + o));
    }

    private Flux<ProductEvent> getAllEvents() {
        return webClient
                .get()
                .uri("/events")
                .retrieve()
                .bodyToFlux(ProductEvent.class);
    }
}
