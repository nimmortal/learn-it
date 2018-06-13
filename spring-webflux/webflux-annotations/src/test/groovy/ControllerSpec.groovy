import it.learn.webfluxannotations.WebfluxAnnotationsApplication
import it.learn.webfluxannotations.product.Product
import it.learn.webfluxannotations.product.ProductController
import it.learn.webfluxannotations.product.ProductEvent
import it.learn.webfluxannotations.product.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.FluxExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

@SpringBootTest(classes = WebfluxAnnotationsApplication)
class ControllerSpec extends Specification {

    private WebTestClient client

    private List<Product> expectedList

    @Autowired
    private ProductRepository repository

    def setup() {
        this.client =
                WebTestClient
                        .bindToController(new ProductController(repository))
                        .configureClient()
                        .baseUrl("/products")
                        .build()

        this.expectedList =
                repository.findAll().collectList().block()
    }

    def "should get all products"() {
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
