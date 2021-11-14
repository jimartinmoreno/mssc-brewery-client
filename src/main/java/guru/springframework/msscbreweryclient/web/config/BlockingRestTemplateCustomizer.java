package guru.springframework.msscbreweryclient.web.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Permite definir la configuración para usar Apache HTTP en lugar de la implementación estandar
 * Tiene mejor rendimiento
 */
// @Component
public class BlockingRestTemplateCustomizer implements RestTemplateCustomizer {

    private static final int HTTP_MAX_IDLE = 20;
    private static final int HTTP_KEEP_ALIVE = 20;
    private static final int HTTP_CONNECTION_TIMEOUT = 30;

    public ClientHttpRequestFactory apacheClientHttpRequestFactory(){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // set a total amount of connections across all HTTP routes
        connectionManager.setMaxTotal(100);
        // set a maximum amount of connections for each HTTP route in pool
        connectionManager.setDefaultMaxPerRoute(20);

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(3000)
                .setSocketTimeout(3000)
                .build();

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    public ClientHttpRequestFactory okHttp3ClientHttpRequestFactory(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        ConnectionPool okHttpConnectionPool = new ConnectionPool(HTTP_MAX_IDLE, HTTP_KEEP_ALIVE,
                TimeUnit.SECONDS);
        builder.connectionPool(okHttpConnectionPool);
        builder.connectTimeout(HTTP_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);
        return new OkHttp3ClientHttpRequestFactory(builder.build());
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        // Apache
        // restTemplate.setRequestFactory(this.apacheClientHttpRequestFactory());
        // okHttp3
        // restTemplate.setRequestFactory(this.okHttp3ClientHttpRequestFactory());
    }
}
