package it.learn.configurationprops

import it.learn.configurationprops.ApplicationProperties
import it.learn.configurationprops.ConfigurationPropsApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = ConfigurationPropsApplication)
class ApplicationPropertiesSpec extends Specification {

    @Autowired
    private ApplicationProperties properties

    def "spring should populate simple properties"() {
        expect:
            properties.from == "mailer@mail.com"
            properties.host == "mailer@mail.com"
            properties.port == 9000
    }

    def "spring should populate list"() {
        expect:
            properties.defaultRecipients.first() == "admin@mail.com"
            properties.defaultRecipients.last() == "owner@mail.com"
    }

    def "spring should populate map"() {
        expect:
            properties.additionalHeaders["redelivery"] == "true"
            properties.additionalHeaders["secure"] == "true"
    }

}
