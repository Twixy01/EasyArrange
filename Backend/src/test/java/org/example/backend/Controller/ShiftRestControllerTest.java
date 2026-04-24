package org.example.backend.Controller;

import org.example.backend.DTO.Shift.ShiftCreateRequest;
import org.example.backend.DTO.Shift.ShiftResponse;
import org.example.backend.Service.ShiftService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShiftRestControllerTest {

    @Mock
    private ShiftService shiftService;

    @InjectMocks
    private ShiftRestController controller;

    private ShiftCreateRequest createRequest() {
        return new ShiftCreateRequest("MONDAY", LocalTime.of(9, 0), LocalTime.of(17, 0));
    }

    private ShiftResponse createResponse() {
        return new ShiftResponse(3L, "MONDAY", LocalTime.of(9, 0), LocalTime.of(17, 0));
    }

    @Test
    void testGetAll() {
        List<ShiftResponse> expected = List.of(createResponse());
        when(shiftService.findAll()).thenReturn(expected);

        List<ShiftResponse> actual = controller.getAll();

        assertThat(actual).isSameAs(expected);
        verify(shiftService).findAll();
    }

    @Test
    void testGetById() {
        ShiftResponse expected = createResponse();
        when(shiftService.findShiftById(1L)).thenReturn(expected);

        ShiftResponse actual = controller.getById(1L);

        assertThat(actual).isSameAs(expected);
        verify(shiftService).findShiftById(1L);
    }

    @Test
    void testCreate_sendsCorrectDto() {
        ShiftCreateRequest request = createRequest();
        ShiftResponse response = createResponse();
        when(shiftService.create(any())).thenReturn(response);

        ShiftResponse actual = controller.create(request);

        ArgumentCaptor<ShiftCreateRequest> captor = ArgumentCaptor.forClass(ShiftCreateRequest.class);
        verify(shiftService).create(captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testUpdate_sendsCorrectDto() {
        ShiftCreateRequest request = createRequest();
        ShiftResponse response = createResponse();
        when(shiftService.updateResponse(eq(1L), any())).thenReturn(response);

        ShiftResponse actual = controller.update(1L, request);

        ArgumentCaptor<ShiftCreateRequest> captor = ArgumentCaptor.forClass(ShiftCreateRequest.class);
        verify(shiftService).updateResponse(eq(1L), captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testDelete() {
        controller.delete(1L);
        verify(shiftService).remove(1L);
    }
}

