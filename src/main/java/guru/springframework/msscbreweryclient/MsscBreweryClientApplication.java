package guru.springframework.msscbreweryclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * nables Spring's asynchronous method execution capability, similar to functionality found in
 * Spring's <task:*> XML namespace.
 */
@EnableAsync
@SpringBootApplication
public class MsscBreweryClientApplication {

    public static void main(String[] args) {
        // close the application context to shut down the custom ExecutorService
        SpringApplication.run(MsscBreweryClientApplication.class, args).close();
    }

    @Bean
    public Executor taskExecutor() {
        /**
         * JavaBean that allows for configuring a ThreadPoolExecutor in bean style
         * (through its "corePoolSize", "maxPoolSize", "keepAliveSeconds", "queueCapacity" properties)
         * and exposing it as a Spring org.springframework.core.task.TaskExecutor.
         */
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("BreweryLookup-");
        executor.initialize();
        return executor;
    }

}
