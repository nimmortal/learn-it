package it.learn.configurationprops;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "mail")
public class ApplicationProperties {

    public static class Credentials {
        private String authMethod;
        private String username;
        private String password;

        @Override
        public String toString() {
            return "Credentials{" +
                    "authMethod='" + authMethod + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }

        public String getAuthMethod() {
            return authMethod;
        }

        public Credentials setAuthMethod(String authMethod) {
            this.authMethod = authMethod;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public Credentials setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Credentials setPassword(String password) {
            this.password = password;
            return this;
        }
    }

    private String host;
    private int port;
    private String from;
    private Credentials credentials;
    private List<String> defaultRecipients;
    private Map<String, String> additionalHeaders;

    @Override
    public String toString() {
        return "ApplicationProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", from='" + from + '\'' +
                ", credentials=" + credentials +
                ", defaultRecipients=" + defaultRecipients +
                ", additionalHeaders=" + additionalHeaders +
                '}';
    }

    public String getHost() {
        return host;
    }

    public ApplicationProperties setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ApplicationProperties setPort(int port) {
        this.port = port;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public ApplicationProperties setFrom(String from) {
        this.from = from;
        return this;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public ApplicationProperties setCredentials(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public List<String> getDefaultRecipients() {
        return defaultRecipients;
    }

    public ApplicationProperties setDefaultRecipients(List<String> defaultRecipients) {
        this.defaultRecipients = defaultRecipients;
        return this;
    }

    public Map<String, String> getAdditionalHeaders() {
        return additionalHeaders;
    }

    public ApplicationProperties setAdditionalHeaders(Map<String, String> additionalHeaders) {
        this.additionalHeaders = additionalHeaders;
        return this;
    }
}
