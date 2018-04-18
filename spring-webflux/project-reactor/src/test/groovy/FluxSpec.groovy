import org.reactivestreams.Subscription
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Flux
import spock.lang.Specification

import java.time.Duration

class FluxSpec extends Specification {

    def "first flux"() {
        given:
        Flux.just("A", "B", "C")
                .log()
                .subscribe()
    }

    def "flux from iterable"() {
        given:
        Flux.fromIterable(["A", "B", "C"])
                .log()
                .subscribe()
    }

    def "flux from range"() {
        given:
        Flux.range(10, 5)
                .log()
                .subscribe()
    }

    def "flux from interval"() {
        given:
        Flux.interval(Duration.ofSeconds(1))
                .log()
                .take(2)
                .subscribe()
//        Thread.sleep(5000);
    }

    def "flux request"() {
        given:
        Flux.range(1, 5)
                .log()
                .subscribe(
                null,
                null,
                null,
                { s -> s.request(3) }
        )
    }

    def "flux custom subscriber"() {
        given:
        Flux.range(1, 10)
                .log()
                .subscribe(new BaseSubscriber<Integer>() {

            int elementsToProcess = 3
            int counter = 0

            void hookOnSubscribe(Subscription subscription) {
                println "Subscribed!"
                request(elementsToProcess)
            }

            void hookOnNext(Integer value) {
                counter++
                if (counter == elementsToProcess) {
                    counter = 0

                    Random r = new Random()
                    elementsToProcess = r.ints(1, 4)
                            .findFirst().getAsInt()
                    request(elementsToProcess)
                }
            }
        })
    }

    def "flux limit rate"() {
        given:
        Flux.range(1, 5)
                .log()
                .limitRate(3)
                .subscribe()
    }
}
