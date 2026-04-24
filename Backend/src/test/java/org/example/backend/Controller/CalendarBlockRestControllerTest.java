package org.example.backend.Controller;

import org.example.backend.DTO.CalendarBlock.CalendarBlockRequest;
import org.example.backend.DTO.CalendarBlock.CalendarBlockResponse;
import org.example.backend.Service.CalendarBlockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarBlockRestControllerTest {

    @Mock
    private CalendarBlockService calendarBlockService;

    @InjectMocks
    private CalendarBlockRestController controller;

    private CalendarBlockRequest createRequest() {
        return new CalendarBlockRequest(
                "Vacation",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                2L
        );
    }

    private CalendarBlockResponse createResponse() {
        return new CalendarBlockResponse(
                10L,
                "Vacation",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                null
        );
    }

    @Test
    void testGetCalendarBlocks() {
        List<CalendarBlockResponse> expected = List.of(createResponse());
        when(calendarBlockService.findAllByOrderByStartDateTimeAsc()).thenReturn(expected);

        List<CalendarBlockResponse> actual = controller.getCalendarBlocks();

        assertThat(actual).isSameAs(expected);
        verify(calendarBlockService).findAllByOrderByStartDateTimeAsc();
    }

    @Test
    void testGetCalendarBlockById() {
        CalendarBlockResponse expected = createResponse();
        when(calendarBlockService.findCalendarBlockById(1L)).thenReturn(expected);

        CalendarBlockResponse actual = controller.getCalendarBlockById(1L);

        assertThat(actual).isSameAs(expected);
        verify(calendarBlockService).findCalendarBlockById(1L);
    }

    @Test
    void testCreateCalendarBlock_sendsCorrectDto() {
        CalendarBlockRequest request = createRequest();
        CalendarBlockResponse response = createResponse();
        when(calendarBlockService.create(any())).thenReturn(response);

        CalendarBlockResponse actual = controller.createCalendarBlock(request);

        ArgumentCaptor<CalendarBlockRequest> captor = ArgumentCaptor.forClass(CalendarBlockRequest.class);
        verify(calendarBlockService).create(captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testUpdateCalendarBlock_sendsCorrectDto() {
        CalendarBlockRequest request = createRequest();
        CalendarBlockResponse response = createResponse();
        when(calendarBlockService.update(eq(1L), any())).thenReturn(response);

        CalendarBlockResponse actual = controller.updateCalendarBlock(1L, request);

        ArgumentCaptor<CalendarBlockRequest> captor = ArgumentCaptor.forClass(CalendarBlockRequest.class);
        verify(calendarBlockService).update(eq(1L), captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testDeleteCalendarBlock() {
        controller.deleteCalendarBlock(1L);
        verify(calendarBlockService).remove(1L);
    }
}

