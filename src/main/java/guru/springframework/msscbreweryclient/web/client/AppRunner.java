package guru.springframework.msscbreweryclient.web.client;

import guru.springframework.msscbreweryclient.web.model.BeerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Interface used to indicate that a bean should run when it is contained within a SpringApplication.
 * Multiple CommandLineRunner beans can be defined within the same application context and can be
 * ordered using the Ordered interface or @Order annotation.
 */
@Component
@Slf4j
public class AppRunner implements CommandLineRunner {

    private final AsyncBreweryClient asyncBreweryClient;

    public AppRunner(AsyncBreweryClient asyncBreweryClient) {
        this.asyncBreweryClient = asyncBreweryClient;
    }

    @Override
    public void run(String... args) throws Exception {
        // Start the clock
        long start = System.currentTimeMillis();

        // Kick of multiple, asynchronous lookups
        CompletableFuture<BeerDto> beer1 = asyncBreweryClient.findBeer(UUID.randomUUID());
        CompletableFuture<BeerDto> beer2 = asyncBreweryClient.findBeer(UUID.randomUUID());
        CompletableFuture<BeerDto> beer3 = asyncBreweryClient.findBeer(UUID.randomUUID());

        // Wait until they are all done
        //CompletableFuture.allOf(beer1, beer2, beer3).join();

        // Print results, including elapsed time
        log.info("Elapsed time: " + (System.currentTimeMillis() - start));
        log.info("--> " + beer1.get());
        log.info("--> " + beer2.get());
        log.info("--> " + beer3.get());

    }

}