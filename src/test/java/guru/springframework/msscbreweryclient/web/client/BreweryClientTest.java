package guru.springframework.msscbreweryclient.web.client;

import guru.springframework.msscbreweryclient.web.model.BeerDto;
import guru.springframework.msscbreweryclient.web.model.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j //Lombok annotation
@SpringBootTest
class BreweryClientTest {

    @Autowired
    BreweryClient client;

    @Test
    void getAllBeers() {
        List<BeerDto> dtoList = client.getAllBeers();
        log.info("dtoList = " + dtoList);
        assertNotNull(dtoList);
        assertNotEquals(dtoList.size(), 0);
        assertThat(dtoList.size()).isPositive();
    }

    @Test
    void getAllBeersExchange() {
        List<BeerDto> dtoList = client.getAllBeersExchange();
        log.info("dtoList = " + dtoList);
        assertNotNull(dtoList);
        assertNotEquals(dtoList.size(), 0);
        assertThat(dtoList.size()).isPositive();
    }

    @Test
    void getBeerById() {
        BeerDto dto = client.getBeerById(UUID.fromString("c05d52d0-b2fb-455d-96d7-e8a1f053720a"));
        log.info("dto = " + dto);
        assertNotNull(dto);
        assertThat(dto.getBeerName()).isEqualTo("Galaxy Cat");
    }

    //@Test
    void saveNewBeer() {
        //given
        BeerDto beerDto = getBeerDto();
        beerDto.setUpc("0083783370002");
        URI uri = client.saveNewBeer(beerDto);
        log.info("uri: " + uri);
        assertNotNull(uri);

    }

    @Test
    void saveNewBeer2() {
        //given
        BeerDto beerDto = getBeerDto();
        beerDto.setUpc("0083783370001");
        beerDto = client.saveNewBeer2(beerDto);
        log.info("uri: " + beerDto);
        assertNotNull(beerDto);
        assertThat(beerDto).isNotNull();

    }


    @Test
    void saveNewBeer3() {
        //given
        BeerDto beerDto = getBeerDto();
        beerDto.setUpc("0083783370003");
        beerDto = client.saveNewBeer3(beerDto);
        log.info("uri: " + beerDto);
        assertNotNull(beerDto);
        assertThat(beerDto).isNotNull();
    }

    @Test
    void testUpdateBeer() {
        //given
        BeerDto beerDto = getBeerDto();
        beerDto.setUpc("0083783370004");
        beerDto = client.saveNewBeer3(beerDto);

        beerDto.setBeerName("New Beer Updated");
        client.updateBeer(beerDto.getId(), beerDto);
        //assertNotNull(beerDto);

    }

    @Test
    void testUpdateBeer2() {
        //given
        BeerDto beerDto = getBeerDto();
        beerDto.setUpc("0083783370005");
        beerDto = client.saveNewBeer3(beerDto);
        System.out.println("beerDto = " + beerDto);

        beerDto.setBeerName("New Beer Updated");

        beerDto = client.updateBeer2(beerDto.getId(), beerDto);
        System.out.println("beerDtoUpdated = " + beerDto);
        assertNotNull(beerDto);

    }

    //@Test
    void testDeleteBeer() {
        client.deleteBeer(UUID.randomUUID());
    }

    //@Test
    void getCustomerById() {
        CustomerDto dto = client.getCustomerById(UUID.randomUUID());
        assertNotNull(dto);
    }

    //@Test
    void testSaveNewCustomer() {
        //given
        CustomerDto customerDto = CustomerDto.builder().name("Joe").build();
        URI uri = client.saveNewCustomer(customerDto);
        assertNotNull(uri);
        log.info(uri.toString());
    }

    //@Test
    void testUpdateCustomer() {
        //given
        CustomerDto customerDto = CustomerDto.builder().name("Jim").build();
        client.updateCustomer(UUID.randomUUID(), customerDto);
    }

    //@Test
    void testDeleteCustomer() {
        client.deleteCustomer(UUID.randomUUID());
    }

    //@Test
    void getBeerEntityById() {
        ResponseEntity<BeerDto> dto = client.getBeerEntityById(UUID.randomUUID());
        log.info("dto = " + dto);
        assertNotNull(dto);
    }

    private BeerDto getBeerDto() {

        return BeerDto.builder()
                .beerName("New Beer")
                .beerStyle("WHEAT")
                .upc("0083783370001")
                .quantityOnHand(10)
                .price(new BigDecimal("2.5"))
                .build();
    }


}