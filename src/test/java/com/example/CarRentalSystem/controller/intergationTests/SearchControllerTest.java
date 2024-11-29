package com.example.CarRentalSystem.controller.intergationTests;

import com.example.CarRentalSystem.model.enums.City;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/data/drop_table.sql")
@Sql("/data/schema_test.sql")
public class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetAvailableVehicle() throws Exception {
        City cityStart = City.BERLIN;
        City cityEnd = City.BERLIN;
        LocalDate dateStart = LocalDate.of(2024,12,12);
        LocalDate dateEnd = LocalDate.of(2024, 12,12);

        mockMvc.perform(get("/searchService/searchVehicleByQuery?cityStart="+cityStart+"&cityEnd="+cityEnd+"&dateStart="+dateStart+"&dateEnd="+dateEnd)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id":100,"type":{"id":100,"vehicleTypeName":"Auto"},"subType":{"id":100,"subTypeName":"Lux","type":{"id":100,"vehicleTypeName":"Auto"}},"active":true,"brand":{"id":100,"brandName":"Ford"},"model":{"id":100,"modelName":"Mustang","brand":{"id":100,"brandName":"Ford"}},"engineType":"PETROL","year":1999,"branch":{"id":100,"branchName":"Berlin Brunch","address":{"id":100,"zipCode":"14000","country":"Germany","region":"BERLIN","city":"BERLIN","district":"Main district","street":"Mainstraße","house":2,"apartment":"GMbH CarRentalSystem","additionalInfo":"2 OG","createDate":"2024-11-12T12:00:00"},"phone":"+496872211","workingTime":"MO - FR: 9:00 - 18:00"},"transmissionType":"MANUAL","mileage":230445,"city":"BERLIN","favorite":true,"vinCode":"AAA123","vehicleNumber":"SSS555","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":101,"type":{"id":100,"vehicleTypeName":"Auto"},"subType":{"id":100,"subTypeName":"Lux","type":{"id":100,"vehicleTypeName":"Auto"}},"active":true,"brand":{"id":100,"brandName":"Ford"},"model":{"id":100,"modelName":"Mustang","brand":{"id":100,"brandName":"Ford"}},"engineType":"PETROL","year":1998,"branch":{"id":100,"branchName":"Berlin Brunch","address":{"id":100,"zipCode":"14000","country":"Germany","region":"BERLIN","city":"BERLIN","district":"Main district","street":"Mainstraße","house":2,"apartment":"GMbH CarRentalSystem","additionalInfo":"2 OG","createDate":"2024-11-12T12:00:00"},"phone":"+496872211","workingTime":"MO - FR: 9:00 - 18:00"},"transmissionType":"MANUAL","mileage":230440,"city":"BERLIN","favorite":false,"vinCode":"BBB111","vehicleNumber":"UUU444","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":102,"type":{"id":100,"vehicleTypeName":"Auto"},"subType":{"id":100,"subTypeName":"Lux","type":{"id":100,"vehicleTypeName":"Auto"}},"active":true,"brand":{"id":100,"brandName":"Ford"},"model":{"id":100,"modelName":"Mustang","brand":{"id":100,"brandName":"Ford"}},"engineType":"PETROL","year":1998,"branch":{"id":100,"branchName":"Berlin Brunch","address":{"id":100,"zipCode":"14000","country":"Germany","region":"BERLIN","city":"BERLIN","district":"Main district","street":"Mainstraße","house":2,"apartment":"GMbH CarRentalSystem","additionalInfo":"2 OG","createDate":"2024-11-12T12:00:00"},"phone":"+496872211","workingTime":"MO - FR: 9:00 - 18:00"},"transmissionType":"MANUAL","mileage":230440,"city":"BERLIN","favorite":false,"vinCode":"YYY555","vehicleNumber":"HHH664","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":103,"type":{"id":100,"vehicleTypeName":"Auto"},"subType":{"id":100,"subTypeName":"Lux","type":{"id":100,"vehicleTypeName":"Auto"}},"active":true,"brand":{"id":100,"brandName":"Ford"},"model":{"id":100,"modelName":"Mustang","brand":{"id":100,"brandName":"Ford"}},"engineType":"PETROL","year":1998,"branch":{"id":100,"branchName":"Berlin Brunch","address":{"id":100,"zipCode":"14000","country":"Germany","region":"BERLIN","city":"BERLIN","district":"Main district","street":"Mainstraße","house":2,"apartment":"GMbH CarRentalSystem","additionalInfo":"2 OG","createDate":"2024-11-12T12:00:00"},"phone":"+496872211","workingTime":"MO - FR: 9:00 - 18:00"},"transmissionType":"MANUAL","mileage":230440,"city":"BERLIN","favorite":false,"vinCode":"TTT434","vehicleNumber":"EEE333","createDate":"2024-11-12T12:00:00","updateDate":null}]
                                """))
                .andReturn();
    }

    @Sql("/data/delete_data.sql")
    @Test
    public void testGetAvailableVehicle_EmptyList() throws Exception {
        City cityStart = City.BERLIN;
        City cityEnd = City.BERLIN;
        LocalDate dateStart = LocalDate.of(2024,12,12);
        LocalDate dateEnd = LocalDate.of(2024, 12,12);

        mockMvc.perform(get("/searchService/searchVehicleByQuery?cityStart="+cityStart+"&cityEnd="+cityEnd+"&dateStart="+dateStart+"&dateEnd="+dateEnd)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                ]
                                """))
                .andReturn();
    }
}
