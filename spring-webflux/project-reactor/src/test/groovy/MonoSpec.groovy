import reactor.core.publisher.Mono
import spock.lang.Specification

class MonoSpec extends Specification {

    def "first mono test"() {
        given:
        Mono.just("A")
                .log()
                .subscribe()
    }

    def "mono with consumer"() {
        given:
        Mono.just("A")
                .log()
                .subscribe({ s -> println(s) })
    }

    def "mono with do on"() {
        given:
        Mono.just("A")
                .log()
                .doOnSubscribe({ subs -> println("Subscribed: ${subs}") })
                .doOnRequest({ request -> println("Request: ${request}") })
                .doOnSuccess({ complete -> println("Complete: ${complete}") })
                .subscribe({ s -> println(s) })
    }

    def "empty mono"() {
        given:
        Mono.empty()
                .log()
                .subscribe({ s -> println(s) })
    }

    def "empty complete consumer mono"() {
        given:
        Mono.empty()
                .log()
                .subscribe(
                { s -> System.out.println(s) },
                null,
                { -> println "Done" }
        )
    }

    def "error runtime exception mono"() {
        given:
        Mono.error(new RuntimeException())
                .log()
                .subscribe()
    }

    def "error exception mono"() {
        given:
        Mono.error(new Exception())
                .log()
                .subscribe()
    }

    def "error consumer mono"() {
        given:
        Mono.error(new Exception())
                .log()
                .subscribe(
                { -> println() },
                { e -> println "Error: ${e}" }
        )
    }

    def "error do on error mono"() {
        given:
        Mono.error(new Exception())
                .doOnError({ e -> println "Error: ${e}" })
                .log()
                .subscribe()
    }

    def "error do on error resume mono"() {
        given:
        Mono.error(new Exception())
                .onErrorResume({ e ->
                    println "Caught: ${e}"
                    return Mono.just("B")
                })
                .log()
                .subscribe()
    }

    def "error do on error return mono"() {
        given:
        Mono.error(new Exception())
                .onErrorReturn("B")
                .log()
                .subscribe()
    }

}
