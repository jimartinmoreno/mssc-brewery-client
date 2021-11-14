package guru.springframework.msscbreweryclient.web.client;

import guru.springframework.msscbreweryclient.web.model.BeerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j //Lombok annotation
public class AsyncBreweryClient {
    private final RestTemplate restTemplate;
    @Value("${sfg.brewery.beerApiPath}")
    private String beerApiPath;
    @Value("${sfg.brewery.apihost}")
    private String apiHost;

    public AsyncBreweryClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<BeerDto> findBeer(UUID uuid) throws InterruptedException {
        log.info("Looking up " + uuid);
        log.info("beerApiPath:  " + beerApiPath);
        log.info("apiHost: " + apiHost);

        // String url = String.format("https://api.github.com/users/%s", user);
        BeerDto result = restTemplate.getForObject(apiHost + beerApiPath + "/{id}", BeerDto.class, uuid);

        log.info("result: " + result);
        // Artificial delay of 1s for demonstration purposes
        // Thread.sleep(1000L);
        return CompletableFuture.completedFuture(result);
    }

//    public void setApiHost(String apiHost) {
//        this.apiHost = apiHost;
//    }
//
//    public void setBeerApiPath(String beerApiPath) {
//        this.beerApiPath = beerApiPath;
//    }

}
