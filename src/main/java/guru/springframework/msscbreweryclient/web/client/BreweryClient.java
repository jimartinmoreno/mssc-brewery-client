package guru.springframework.msscbreweryclient.web.client;

import guru.springframework.msscbreweryclient.web.model.BeerDto;
import guru.springframework.msscbreweryclient.web.model.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

/**
 * @ConfigurationProperties Annotation for externalized configuration. Add this to a class definition
 * or a @Bean method in a @Configuration class if you want to bind and validate some external
 * Properties (e.g. from a .properties file).
 */
@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
@Component
@Slf4j //Lombok annotation
public class BreweryClient {

    /**
     * Se va a inyectar tanto el beerApiPath como el apiHost que está en el fichero
     * application.properties
     */
    //public final String BEER_PATH_V1 = "/api/v1/beer/";
    private String beerApiPath;
    private String customerApiPath;
    private String apiHost;
    private final RestTemplate restTemplate;

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    public void setBeerApiPath(String beerApiPath) {
        this.beerApiPath = beerApiPath;
    }

    public void setCustomerApiPath(String customerApiPath) {
        this.customerApiPath = customerApiPath;
    }

    public BreweryClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        log.info("this.restTemplate.getClientHttpRequestInitializers(): " + this.restTemplate.getClientHttpRequestInitializers());
        log.info("this.restTemplate.getRequestFactory(): " + this.restTemplate.getRequestFactory());
    }

    public BeerDto getBeerById(UUID uuid) {
        return restTemplate.getForObject(apiHost + beerApiPath + uuid, BeerDto.class);
    }

    public ResponseEntity<BeerDto> getBeerEntityById(UUID uuid) {
        ResponseEntity<BeerDto> entity = restTemplate
                .getForEntity(apiHost + beerApiPath + "/{id}",
                        BeerDto.class,
                        uuid);
        log.info("Status code value: " + entity.getStatusCodeValue());
        log.info("HTTP Header 'ContentType': " + entity.getHeaders().getContentType());
        log.info("entity = " + entity);
        log.info("body = " + entity.getBody());
        log.info("Location => " + entity.getHeaders().getLocation());
        entity.getHeaders().forEach((key, value) -> log.info(key + ": " + value));
        return entity;
    }

    public List<BeerDto> getAllBeers() {
        // public List<BeerDto> getAll(int page, int pageSize) {
        // String requestUri = REQUEST_URI + "?page={page}&pageSize={pageSize}";
        // Map<String, String> urlParameters = new HashMap<>();
        // urlParameters.put("page", Integer.toString(page));
        // urlParameters.put("pageSize", Long.toString(pageSize));
        ResponseEntity<BeerDto[]> entity = restTemplate.getForEntity(apiHost + beerApiPath,
                BeerDto[].class);
        return entity.getBody() != null ? Arrays.asList(entity.getBody()) : Collections.emptyList();
    }

    public void updateBeer(UUID uuid, BeerDto beerDto) {
        restTemplate.put(apiHost + beerApiPath + uuid, beerDto);
    }

    public void deleteBeer(UUID uuid) {
        restTemplate.delete(apiHost + beerApiPath + uuid);
    }

    public URI saveNewBeer(BeerDto beerDto) {
        //postOtherWays(beerDto);
        return restTemplate.postForLocation(apiHost + beerApiPath, beerDto);
    }

    private void postOtherWays(BeerDto beerDto) {
        log.info("-----------------");
        BeerDto dto = restTemplate.postForObject(apiHost + beerApiPath, beerDto, BeerDto.class);
        log.info("dto = " + dto);
        log.info("-----------------");
        ResponseEntity<BeerDto> entity = restTemplate.postForEntity(apiHost + beerApiPath, beerDto, BeerDto.class);
        log.info("entity = " + entity);
        log.info("body = " + entity.getBody());
        log.info("Location => " + entity.getHeaders().getLocation());
        entity.getHeaders().forEach((key, value) -> log.info(key + ": " + value));
        log.info("-----------------");

    }

    public CustomerDto getCustomerById(UUID customerId) {
        return restTemplate.getForObject(apiHost + customerApiPath + customerId, CustomerDto.class);
    }

    public URI saveNewCustomer(CustomerDto customerDto) {
        return restTemplate.postForLocation(apiHost + customerApiPath, customerDto);
    }

    public void updateCustomer(UUID customerId, CustomerDto customerDto) {
        restTemplate.put(apiHost + customerApiPath + customerId, customerDto);
    }

    public void deleteCustomer(UUID customerId) {
        restTemplate.delete(apiHost + customerApiPath + customerId);
    }

}
