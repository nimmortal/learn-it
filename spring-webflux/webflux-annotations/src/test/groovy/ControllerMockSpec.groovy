import it.learn.webfluxannotations.product.Product
import it.learn.webfluxannotations.product.ProductController
import it.learn.webfluxannotations.product.ProductEvent
import it.learn.webfluxannotations.product.ProductRepository
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.FluxExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class ControllerMockSpec extends Specification {

    private WebTestClient client

    private List<Product> expectedList

    private ProductRepository repository = Mock()

    def setup() {
        this.client =
                WebTestClient
                        .bindToController(new ProductController(repository))
                        .configureClient()
                        .baseUrl("/products")
                        .build()

        this.expectedList = [  new Product("1", "Big Latte", 2.99) ]
    }

    def "should get all products"() {
        given:
        repository.findAll() >> Flux.fromIterable(this.expectedList)

        expect:
        client.get()
                .uri("/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Product.class)
                .isEqualTo(expectedList)
    }

    def "should not found invalid product id"() {
        given:
        def id = "aaa"
        repository.findById(id) >> Mono.empty()

        expect:
        client.get()
                .uri("/aaa")
                .exchange()
                .expectStatus()
                .isNotFound()
    }

    def "should found a product"() {
        given:
        Product expectedProduct = expectedList.get(0)
        repository.findById(expectedProduct.getId()) >> Mono.just(expectedProduct)

        when:
        def response = client.get()
                .uri("/{id}", expectedProduct.getId())
                .exchange()

        then:
        response.expectStatus()
                .isOk()
                .expectBody(Product.class)
                .isEqualTo(expectedProduct)
    }

    def "test product events"() {
        given:
        ProductEvent expectedEvent =
                new ProductEvent(0L, "Product Event")

        when:
        FluxExchangeResult<ProductEvent> result =
                client.get().uri("/events")
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .exchange()
                        .expectStatus().isOk()
                        .returnResult(ProductEvent.class)

        then:
        StepVerifier.create(result.getResponseBody())
                .expectNext(expectedEvent)
                .expectNextCount(2)
                .consumeNextWith({ assertThat(it.getEventId()).isEqualTo(3)})
                .thenCancel()
                .verify()
    }
}
