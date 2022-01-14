package guru.springframework.msscbreweryclient.web.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Permite definir la configuración para usar Apache HTTP en lugar de la implementación estandar
 * Tiene mejor rendimiento
 */
@Component
public class ApacheRestBlockingTemplateCustomizer implements RestTemplateCustomizer {

    private final Integer maxTotalConnections;
    private final Integer defaultMaxTotalConnetions;
    private final Integer connectionRequestTimeout;
    private final Integer socketTimeout;

    public ApacheRestBlockingTemplateCustomizer(@Value("${apache.maxtotalconnections}") Integer maxTotalConnections,
                                                @Value("${apache.defaultmaxtotalconnections}") Integer defaultMaxTotalConnetions,
                                                @Value("${apache.connectionrequesttimeout}") Integer connectionRequestTimeout,
                                                @Value("${apache.sockettimeout}") Integer socketTimeout) {
        this.maxTotalConnections = maxTotalConnections;
        this.defaultMaxTotalConnetions = defaultMaxTotalConnetions;
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.socketTimeout = socketTimeout;
    }

    public ClientHttpRequestFactory apacheClientHttpRequestFactory() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // set a total amount of connections across all HTTP routes
        connectionManager.setMaxTotal(maxTotalConnections);
        // set a maximum amount of connections for each HTTP route in pool
        connectionManager.setDefaultMaxPerRoute(defaultMaxTotalConnetions);

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        // Apache
        restTemplate.setRequestFactory(this.apacheClientHttpRequestFactory());

        // To set interceptors (https://stackoverflow.com/questions/7952154/spring-resttemplate-how-to-enable-full-debugging-logging-of-requests-responses)
        // List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        // interceptors.add(new LoggingRequestInterceptor());
        // restTemplate.setInterceptors(interceptors);
    }
}
