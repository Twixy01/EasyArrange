package org.example.backend.Controller;

import org.example.backend.DTO.Booking.*;
import org.example.backend.DTO.TimeSlot.AvailableSlotResponse;
import org.example.backend.Model.entity.BookingStatus;
import org.example.backend.Service.BookingService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingRestControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingRestController controller;

    private BookingResponse createResponse() {
        return new BookingResponse(
                15L,
                null, //StaffResponse
                null, //UserResponse
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                null, //ServiceResponse
                "BOOKED"
        );
    }

    // helper that supplies the required constructor args for the record
    private BookingCreateRequest createCreateRequest() {
        return new BookingCreateRequest(
                2L, // staffId
                7L, // userId
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                5L  // serviceId
        );
    }

    // java
    private BookingUpdateRequest createUpdateRequest() {
        return new BookingUpdateRequest(
                LocalDateTime.now(),                 // start
                LocalDateTime.now().plusHours(1),    // end
                5L,                                   // serviceId (Long)
                "BOOKED"                              // status (String)
        );
    }


    @Test
    void testCreate_sendsCorrectDto() {
        BookingCreateRequest request = createCreateRequest();

        BookingResponse response = createResponse();
        when(bookingService.create(any())).thenReturn(response);

        BookingResponse actual = controller.create(request);

        ArgumentCaptor<BookingCreateRequest> captor =
                ArgumentCaptor.forClass(BookingCreateRequest.class);

        verify(bookingService).create(captor.capture());

        BookingCreateRequest captured = captor.getValue();

        assertThat(captured).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }


    @Test
    void testUpdate_sendsCorrectDto() {
        BookingUpdateRequest request = createUpdateRequest();

        BookingResponse response = createResponse();
        when(bookingService.update(eq(1L), any())).thenReturn(response);

        controller.update(1L, request);

        ArgumentCaptor<BookingUpdateRequest> captor =
                ArgumentCaptor.forClass(BookingUpdateRequest.class);

        verify(bookingService).update(eq(1L), captor.capture());

        BookingUpdateRequest captured = captor.getValue();

        assertThat(captured).isSameAs(request);
    }
}
