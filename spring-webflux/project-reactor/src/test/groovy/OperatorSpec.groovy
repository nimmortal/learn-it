import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.Duration
import java.util.function.BiFunction

class OperatorSpec extends Specification {

    def "operator `map` showcase"() {
        given:
        Flux.range(1, 5)
                .map({ it * 10 })
                .subscribe({ println it })
    }

    def "operator `flatMap` showcase"() {
        given:
        Flux.range(1, 5)
                .flatMap({ Flux.range(it * 10, 2) })
                .subscribe({ println it })
    }

    def "operator `flatMapMany` showcase"() {
        given:
        Mono.just(3)
                .flatMapMany({ Flux.range(1, it) })
                .subscribe({ println it })
    }

    def "operator `concat` showcase"() {
        given:
        Flux<Integer> oneToFive = Flux.range(1, 5)
                .delayElements(Duration.ofMillis(200))

        Flux<Integer> sixToTen = Flux.range(6, 5)
                .delayElements(Duration.ofMillis(400))

        Flux.concat(oneToFive, sixToTen)
                .subscribe({ println it })

//        oneToFive.concatWith(sixToTen)
//                .subscribe({ println it })

        Thread.sleep(4000)
    }

    def "operator `merge` showcase"() {
        given:
        Flux<Integer> oneToFive = Flux.range(1, 5)
                .delayElements(Duration.ofMillis(200))

        Flux<Integer> sixToTen = Flux.range(6, 5)
                .delayElements(Duration.ofMillis(400))

        Flux.merge(oneToFive, sixToTen)
                .subscribe({ println it })

//        oneToFive.mergeWith(sixToTen)
//                .subscribe({ println it })

        Thread.sleep(4000)
    }

    def "operator `zip` showcase"() {
        given:
        Flux<Integer> oneToFive = Flux.range(1, 5)
        Flux<Integer> sixToTen = Flux.range(6, 5)

        Flux.zip(oneToFive, sixToTen,
                ({ item1, item2 -> item1 + ", " + item2 }) as BiFunction)
                .subscribe({ println it })
    }

}
