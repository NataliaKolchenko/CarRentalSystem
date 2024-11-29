package com.example.CarRentalSystem.controller.intergationTests;

import com.example.CarRentalSystem.model.enums.BookingStatus;
import com.example.CarRentalSystem.model.enums.City;
import com.example.CarRentalSystem.exception.error.ErrorCarRentalSystem;
import com.example.CarRentalSystem.exception.error.ErrorMessage;
import com.example.CarRentalSystem.model.dto.BookingRequestDto;
import com.example.CarRentalSystem.model.dto.BookingResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/data/drop_table.sql")
@Sql("/data/schema_test.sql")
@WithMockUser(username = "user@user.user", password = "USER", roles = "USER")
public class BookingControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @Sql("/data/insert_data.sql")
    @Test
    public void testCreateBooking_Success() throws Exception {

        BookingRequestDto requestDto = new BookingRequestDto(
                "user@user.user",
                100L,
                LocalDate.of(2024, 12, 01),
                LocalDate.of(2024, 12, 03),
                City.BERLIN,
                City.BERLIN
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/booking/createBooking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        BookingResponseDto responseDto = objectMapper.readValue(jsonResponse, BookingResponseDto.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertNotNull(responseDto),
                () -> assertNotNull(responseDto.getId())
        );

    }

    @Test
    public void testCreateBooking_EmptyBody_InvalidInput() throws Exception {
        BookingRequestDto requestDto = null;
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/booking/createBooking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testCreateBooking_NullFields_InvalidInput() throws Exception {
        BookingRequestDto requestDto = new BookingRequestDto(
                " ",
                100L,
                null,
                null,
                null,
                null
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);


        MvcResult result = mockMvc.perform(post("/booking/createBooking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("DateFrom may not be null")),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("DateTo may not be null")),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("cityStart may not be null")),
                () -> assertTrue(errorResponse.getErrorDescriptionList().contains("cityEnd may not be null"))
        );

    }
    @Sql("/data/insert_data.sql")
    @Test
    public void testCreateNewBooking_VehicleIsAlreadyExist() throws Exception {
        BookingRequestDto requestDto = new BookingRequestDto(
                "user@user.user",
                100L,
                LocalDate.of(2024, 12, 05),
                LocalDate.of(2024, 12, 05),
                City.BERLIN,
                City.BERLIN
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/booking/createBooking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDescriptionList").isArray())
                .andExpect(jsonPath("$.errorDescriptionList[0]")
                        .value(ErrorMessage.BOOKING_IS_ALREADY_EXIST))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BOOKING_IS_ALREADY_EXIST))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetVehicleById_Success() throws Exception {
        Long bookingId = 100L;

        MvcResult result =
                mockMvc.perform(get("/booking/getBookingById/{id}", bookingId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookingResponseDto responseDto = objectMapper.readValue(jsonResponse, BookingResponseDto.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(bookingId, responseDto.getId()),
                () -> assertNotNull(responseDto)
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetBookingById_BookingIdWasNotFound() throws Exception {
        Long bookingId = 1000L;

        MvcResult result =
                mockMvc.perform(get("/booking/getBookingById/{id}", bookingId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BOOKING_ID_WAS_NOT_FOUND))
        );
    }
    @Sql("/data/insert_data.sql")
    @Test
    public void testGetBookingListByUserId_Success() throws Exception {
        mockMvc.perform(get("/booking/getBookingListByUserId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id":100,"userId":"user@user.user","vehicleId":100,"bookedFromDate":"2024-12-05","bookedToDate":"2024-12-05","status":"CREATED","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":101,"userId":"user@user.user","vehicleId":100,"bookedFromDate":"2024-12-06","bookedToDate":"2024-12-06","status":"CREATED","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":102,"userId":"user@user.user","vehicleId":101,"bookedFromDate":"2024-11-20","bookedToDate":%s,"status":"ACTIVE","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":103,"userId":"user@user.user","vehicleId":101,"bookedFromDate":%s,"bookedToDate":%s,"status":"CREATED","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":105,"userId":"user@user.user","vehicleId":101,"bookedFromDate":"2024-11-11","bookedToDate":"2024-11-20","status":"FINISHED","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":106,"userId":"user@user.user","vehicleId":100,"bookedFromDate":"2024-11-11","bookedToDate":"2024-11-20","status":"CANCELLED","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":107,"userId":"user@user.user","vehicleId":101,"bookedFromDate":"2024-11-20","bookedToDate":"2024-11-30","status":"ACTIVE","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null}]
                                """.replaceAll("%s", String.valueOf(LocalDate.now()))))
                .andReturn();
    }
    @Sql("/data/delete_data.sql")
    @Test
    public void testGetBookingListByUserId_EmptyList() throws Exception {
        mockMvc.perform(get("/booking/getBookingListByUserId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                ]
                                """))
                .andReturn();
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetBookingListByStatus_Created_Success() throws Exception {
        BookingStatus bookingStatus = BookingStatus.CREATED;

        mockMvc.perform(get("/booking/getBookingListByStatus/{status}", bookingStatus)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id":100,"userId":"user@user.user","vehicleId":100,"bookedFromDate":"2024-12-05","bookedToDate":"2024-12-05","status":"CREATED","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":101,"userId":"user@user.user","vehicleId":100,"bookedFromDate":"2024-12-06","bookedToDate":"2024-12-06","status":"CREATED","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":103,"userId":"user@user.user","vehicleId":101,"bookedFromDate":%s,"bookedToDate":%s,"status":"CREATED","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null}]
                                """.replaceAll("%s", String.valueOf(LocalDate.now()))))
                .andReturn();
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetBookingListByStatus_Active_Success() throws Exception {
        BookingStatus bookingStatus = BookingStatus.ACTIVE;

        mockMvc.perform(get("/booking/getBookingListByStatus/{status}", bookingStatus)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id":102,"userId":"user@user.user","vehicleId":101,"bookedFromDate":"2024-11-20","bookedToDate":%s,"status":"ACTIVE","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null},
                                {"id":107,"userId":"user@user.user","vehicleId":101,"bookedFromDate":"2024-11-20","bookedToDate":"2024-11-30","status":"ACTIVE","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null}]
                                """.replaceAll("%s", String.valueOf(LocalDate.now()))))
                .andReturn();
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetBookingListByStatus_Cancelled_Success() throws Exception {
        BookingStatus bookingStatus = BookingStatus.CANCELLED;

        mockMvc.perform(get("/booking/getBookingListByStatus/{status}", bookingStatus)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id":106,"userId":"user@user.user","vehicleId":100,"bookedFromDate":"2024-11-11","bookedToDate":"2024-11-20","status":"CANCELLED","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null}]
                                """))
                .andReturn();
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testGetBookingListByStatus_Finished_Success() throws Exception {
        BookingStatus bookingStatus = BookingStatus.FINISHED;

        mockMvc.perform(get("/booking/getBookingListByStatus/{status}", bookingStatus)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [{"id":105,"userId":"user@user.user","vehicleId":101,"bookedFromDate":"2024-11-11","bookedToDate":"2024-11-20","status":"FINISHED","cityStart":"BERLIN","cityEnd":"BERLIN","createDate":"2024-11-12T12:00:00","updateDate":null}]
                                """))
                .andReturn();
    }

    @Sql("/data/delete_data.sql")
    @Test
    public void testGetBookingListByStatus_EmptyList() throws Exception {
        BookingStatus bookingStatus = BookingStatus.FINISHED;
        mockMvc.perform(get("/booking/getBookingListByStatus/{status}", bookingStatus)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                ]
                                """))
                .andReturn();
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testCancelBooking_Success() throws Exception {
        Long vehicleId = 101L;

        String jsonRequest = objectMapper.writeValueAsString(vehicleId);

        MvcResult result =
                mockMvc.perform(put("/booking/cancelBooking")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Boolean responseResult = objectMapper.readValue(jsonResponse, Boolean.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(Boolean.TRUE, responseResult)
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testCancelBooking_CanNotBeCancelled() throws Exception {
        Long vehicleId = 102L;

        String jsonRequest = objectMapper.writeValueAsString(vehicleId);

        MvcResult result =
                mockMvc.perform(put("/booking/cancelBooking")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorDescriptionList").isArray())
                        .andExpect(jsonPath("$.errorDescriptionList[0]")
                                .value(ErrorMessage.BOOKING_CANNOT_BE_CANCELLED))
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BOOKING_CANNOT_BE_CANCELLED))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testFinishBooking_Success() throws Exception {
        Long vehicleId = 102L;

        String jsonRequest = objectMapper.writeValueAsString(vehicleId);

        MvcResult result =
                mockMvc.perform(put("/booking/finishBooking")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Boolean responseResult = objectMapper.readValue(jsonResponse, Boolean.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(Boolean.TRUE, responseResult)
        );

    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testFinishBooking_CanNotBeFinishedDueStatus() throws Exception {
        Long vehicleId = 105L;

        String jsonRequest = objectMapper.writeValueAsString(vehicleId);

        MvcResult result =
                mockMvc.perform(put("/booking/finishBooking")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorDescriptionList").isArray())
                        .andExpect(jsonPath("$.errorDescriptionList[0]")
                                .value(ErrorMessage.BOOKING_CANNOT_BE_FINISHED_DUE_STATUS))
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BOOKING_CANNOT_BE_FINISHED_DUE_STATUS))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testFinishBooking_CanNotBeFinishedDueDate() throws Exception {
        Long vehicleId = 107L;

        String jsonRequest = objectMapper.writeValueAsString(vehicleId);

        MvcResult result =
                mockMvc.perform(put("/booking/finishBooking")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorDescriptionList").isArray())
                        .andExpect(jsonPath("$.errorDescriptionList[0]")
                                .value(ErrorMessage.BOOKING_CANNOT_BE_FINISHED_DUE_DATE))
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BOOKING_CANNOT_BE_FINISHED_DUE_DATE))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testActivateBooking_Success() throws Exception {
        Long vehicleId = 103L;

        String jsonRequest = objectMapper.writeValueAsString(vehicleId);

        MvcResult result =
                mockMvc.perform(put("/booking/activateBooking")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Boolean responseResult = objectMapper.readValue(jsonResponse, Boolean.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(Boolean.TRUE, responseResult)
        );

    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testActiveBooking_CanNotBeActivatedDueStatus() throws Exception {
        Long vehicleId = 102L;

        String jsonRequest = objectMapper.writeValueAsString(vehicleId);

        MvcResult result =
                mockMvc.perform(put("/booking/activateBooking")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorDescriptionList").isArray())
                        .andExpect(jsonPath("$.errorDescriptionList[0]")
                                .value(ErrorMessage.BOOKING_CANNOT_BE_ACTIVATED_DUE_STATUS))
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BOOKING_CANNOT_BE_ACTIVATED_DUE_STATUS))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testActivateBooking_CanNotBeActivatedDueDate() throws Exception {
        Long vehicleId = 100L;

        String jsonRequest = objectMapper.writeValueAsString(vehicleId);

        MvcResult result =
                mockMvc.perform(put("/booking/activateBooking")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorDescriptionList").isArray())
                        .andExpect(jsonPath("$.errorDescriptionList[0]")
                                .value(ErrorMessage.BOOKING_CANNOT_BE_ACTIVATED_DUE_DATE))
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BOOKING_CANNOT_BE_ACTIVATED_DUE_DATE))
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testUpdateBooking_Success() throws Exception {
        Long bookingExistId = 100L;
        BookingRequestDto requestDto = new BookingRequestDto(
                " ",
                101L,
                LocalDate.of(2024, 12, 01),
                LocalDate.of(2024, 12, 03),
                City.BERLIN,
                City.BONN
        );


        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/booking/updateBooking/{id}", bookingExistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookingResponseDto responseDto = objectMapper.readValue(jsonResponse, BookingResponseDto.class);

        assertAll(
                () -> assertEquals(200, result.getResponse().getStatus()),
                () -> assertEquals(requestDto.getVehicleId(), responseDto.getVehicleId()),
                () -> assertEquals(bookingExistId, responseDto.getId()),
                () -> assertEquals(requestDto.getCityEnd(), responseDto.getCityEnd())
        );
    }

    @Sql("/data/insert_data.sql")
    @Test
    public void testUpdateBooking_CanNotBeUpdate() throws Exception {
        Long bookingId = 105L;
        BookingRequestDto requestDto = new BookingRequestDto(
                " ",
                101L,
                LocalDate.of(2024, 12, 01),
                LocalDate.of(2024, 12, 03),
                City.BERLIN,
                City.BONN
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result =
                mockMvc.perform(put("/booking/updateBooking/{id}", bookingId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.errorDescriptionList").isArray())
                        .andExpect(jsonPath("$.errorDescriptionList[0]")
                                .value(ErrorMessage.BOOKING_CANNOT_BE_UPDATED))
                        .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ErrorCarRentalSystem errorResponse = objectMapper.readValue(jsonResponse, ErrorCarRentalSystem.class);

        assertAll(
                () -> assertEquals(400, result.getResponse().getStatus()),
                () -> assertNotNull(errorResponse.getErrorDescriptionList()),
                () -> assertTrue(errorResponse.getErrorDescriptionList()
                        .contains(ErrorMessage.BOOKING_CANNOT_BE_UPDATED))
        );
    }

}
