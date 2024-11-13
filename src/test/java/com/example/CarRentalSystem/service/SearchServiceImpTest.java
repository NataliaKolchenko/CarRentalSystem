package com.example.CarRentalSystem.service;

import com.example.CarRentalSystem.enums.City;
import com.example.CarRentalSystem.model.Vehicle;
import com.example.CarRentalSystem.repository.SearchRepository;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.*;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Testcontainers
class SearchServiceImpTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() throws LiquibaseException {
        postgres.start();

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(postgres.getJdbcUrl());
        dataSource.setUser(postgres.getUsername());
        dataSource.setPassword(postgres.getPassword());
//
//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setDataSource(dataSource);
//        liquibase.setChangeLog("changelog/changelog-master.yaml");
//        liquibase.afterPropertiesSet();

//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.
//        liquibase.dataSource = dataSource
//        liquibase.changeLog = "classpath:db/changelog/master.xml"
//        liquibase.afterPropertiesSet()
//        liquibase.changeLog = "classpath:db/testchange/master.xml"
//        liquibase.afterPropertiesSet()
    }

//    @BeforeEach
//    void beforeEach() throws LiquibaseException {
//        postgres.start();
//
//        PGSimpleDataSource dataSource = new PGSimpleDataSource();
//        dataSource.setURL(postgres.getJdbcUrl());
//        dataSource.setUser(postgres.getUsername());
//        dataSource.setPassword(postgres.getPassword());
//    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

//    @AfterEach
//    void afterEach() {
//        postgres.stop(); // Останавливаем контейнер после каждого теста
//    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    SearchRepository searchRepository;


//    @BeforeEach
//    void setUp() {
//        postgres.start();
//        dataSource.setUrl(postgres.jdbcUrl)
//        dataSource.user = postgres.username
//        dataSource.password = postgres.password
//
//        jdbcTemplate = JdbcTemplate(dataSource)
//
//
//    }

//    @Test
//    void shouldGetCustomers() {
//        customerService.createCustomer(new Customer(1L, "George"));
//        customerService.createCustomer(new Customer(2L, "John"));
//
//        List<Customer> customers = customerService.getAllCustomers();
//        assertEquals(2, customers.size());
//    }

    @Test
    @Sql("/sql/insert_data.sql")
    void getAvailableVehicle_test1() {
        List<Vehicle> vehicles = searchRepository.queryByAvailableVehicle(
                City.BERLIN,
                LocalDate.of(2024, 11, 12),
                LocalDate.of(2024, 11, 20));
        List<Long> expectedVehicleIdList = List.of(4L, 6L, 8L, 9L);

        List<Long> vehicleIds = vehicles.stream()
                .map(Vehicle::getId)
                .collect(Collectors.toList());

        assertAll(
                () -> assertNotNull(vehicles),
                () -> assertTrue(vehicleIds.containsAll(expectedVehicleIdList))
        );
    }

    @Test
    @Sql("/sql/insert_data.sql")
    void getAvailableVehicle_test2() {
        List<Vehicle> vehicles = searchRepository.queryByAvailableVehicle(
                City.BERLIN,
                LocalDate.of(2024, 11, 12),
                LocalDate.of(2024, 11, 20));
        List<Long> expectedVehicleIdList = List.of(2L, 3L,4L, 5L, 6L, 7L,8L, 9L);

        List<Long> vehicleIds = vehicles.stream()
                .map(Vehicle::getId)
                .collect(Collectors.toList());

        assertAll(
                () -> assertNotNull(vehicles),
                () -> assertTrue(vehicleIds.containsAll(expectedVehicleIdList))
        );
    }
//
//    @Test
//    @Sql("/sql/insert_test3_data.sql")
//    void getAvailableVehicle_test3() {
//        List<Vehicle> vehicles = searchRepository.queryByAvailableVehicle(
//                City.BERLIN,
//                LocalDate.of(2024, 11, 11),
//                LocalDate.of(2024, 11, 11));
//        List<Long> expectedVehicleIdList = List.of(1L, 2L, 3L,4L, 5L, 6L, 7L,8L, 9L);
//
//        List<Long> vehicleIds = vehicles.stream()
//                .map(Vehicle::getId)
//                .collect(Collectors.toList());
//
//        assertAll(
//                () -> assertNotNull(vehicles),
//                () -> assertTrue(vehicleIds.containsAll(expectedVehicleIdList))
//        );
//    }
//
//    @Test
//    @Sql("/sql/insert_test4_data.sql")
//    void getAvailableVehicle_test4() {
//        List<Vehicle> vehicles = searchRepository.queryByAvailableVehicle(
//                City.BERLIN,
//                LocalDate.of(2024, 11, 21),
//                LocalDate.of(2024, 11, 21));
//        List<Long> expectedVehicleIdList = List.of(1L, 2L, 3L,4L, 5L, 6L, 7L,8L, 9L);
//
//        List<Long> vehicleIds = vehicles.stream()
//                .map(Vehicle::getId)
//                .collect(Collectors.toList());
//
//        assertAll(
//                () -> assertNotNull(vehicles),
//                () -> assertTrue(vehicleIds.containsAll(expectedVehicleIdList))
//        );
//    }
//
//    @Test
//    @Sql("/sql/insert_test5_data.sql")
//    void getAvailableVehicle_test5() {
//        List<Vehicle> vehicles = searchRepository.queryByAvailableVehicle(
//                City.BERLIN,
//                LocalDate.of(2024, 11, 15),
//                LocalDate.of(2024, 11, 16));
//        List<Long> expectedVehicleIdList = List.of(2L, 3L,4L, 5L, 6L, 7L,8L, 9L);
//
//        List<Long> vehicleIds = vehicles.stream()
//                .map(Vehicle::getId)
//                .collect(Collectors.toList());
//
//        assertAll(
//                () -> assertNotNull(vehicles),
//                () -> assertTrue(vehicleIds.containsAll(expectedVehicleIdList))
//        );
//    }
//
//    @Test
//    @Sql("/sql/insert_test6_data.sql")
//    void getAvailableVehicle_test6() {
//        List<Vehicle> vehicles = searchRepository.queryByAvailableVehicle(
//                City.BERLIN,
//                LocalDate.of(2024, 11, 11),
//                LocalDate.of(2024, 11, 12));
//        List<Long> expectedVehicleIdList = List.of(2L, 3L,4L, 5L, 6L, 7L,8L, 9L);
//
//        List<Long> vehicleIds = vehicles.stream()
//                .map(Vehicle::getId)
//                .collect(Collectors.toList());
//
//        assertAll(
//                () -> assertNotNull(vehicles),
//                () -> assertTrue(vehicleIds.containsAll(expectedVehicleIdList))
//        );
//    }
//
//

}