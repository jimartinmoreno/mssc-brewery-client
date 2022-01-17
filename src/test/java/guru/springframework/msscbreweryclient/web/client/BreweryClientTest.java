package guru.springframework.msscbreweryclient.web.client;

import guru.springframework.msscbreweryclient.web.model.BeerDto;
import guru.springframework.msscbreweryclient.web.model.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j //Lombok annotation
@SpringBootTest
class BreweryClientTest {

    @Autowired
    BreweryClient client;

    @Test
    void getBeerById() {
        BeerDto dto = client.getBeerById(UUID.randomUUID());
        log.info("dto = " + dto);
        assertNotNull(dto);

    }

    @Test
    void saveNewBeer() {
        //given
        BeerDto beerDto = BeerDto.builder().beerName("New Beer").build();
        URI uri = client.saveNewBeer(beerDto);
        assertNotNull(uri);
        log.info("uri: " + uri.toString());
    }

    @Test
    void testUpdateBeer() {
        //given
        UUID uuid = UUID.randomUUID();
        BeerDto beerDto = BeerDto.builder().beerName("New Beer").beerStyle("Pale").id(uuid).build();
        client.updateBeer(uuid, beerDto);
    }

    @Test
    void testDeleteBeer() {
        client.deleteBeer(UUID.randomUUID());
    }

    @Test
    void getCustomerById() {
        CustomerDto dto = client.getCustomerById(UUID.randomUUID());
        assertNotNull(dto);
    }

    @Test
    void testSaveNewCustomer() {
        //given
        CustomerDto customerDto = CustomerDto.builder().name("Joe").build();
        URI uri = client.saveNewCustomer(customerDto);
        assertNotNull(uri);
        log.info(uri.toString());
    }

    @Test
    void testUpdateCustomer() {
        //given
        CustomerDto customerDto = CustomerDto.builder().name("Jim").build();
        client.updateCustomer(UUID.randomUUID(), customerDto);
    }

    @Test
    void testDeleteCustomer() {
        client.deleteCustomer(UUID.randomUUID());
    }

    @Test
    void getBeerEntityById() {
        ResponseEntity<BeerDto> dto = client.getBeerEntityById(UUID.randomUUID());
        log.info("dto = " + dto);
        assertNotNull(dto);
    }

    @Test
    void getAllBeers() {
        List<BeerDto> dtoList = client.getAllBeers();
        log.info("dtoList = " + dtoList);
        assertNotNull(dtoList);
        assertNotEquals(dtoList.size(), 0);
        assertThat(dtoList, hasSize(2));
    }
}