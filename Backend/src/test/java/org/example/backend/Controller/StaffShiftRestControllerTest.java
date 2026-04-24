package org.example.backend.Controller;

import org.example.backend.DTO.Shift.ShiftResponse;
import org.example.backend.DTO.Staff.StaffResponse;
import org.example.backend.DTO.StaffShift.ShiftUpdateRequest;
import org.example.backend.DTO.StaffShift.StaffShiftRequest;
import org.example.backend.DTO.StaffShift.StaffShiftResponse;
import org.example.backend.Service.StaffShiftService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StaffShiftRestControllerTest {

    @Mock
    private StaffShiftService staffShiftService;

    @InjectMocks
    private StaffShiftRestController controller;

    private StaffShiftRequest createStaffShiftRequest() {
        return new StaffShiftRequest(2L, 3L);
    }

    private ShiftUpdateRequest createUpdateRequest() {
        return new ShiftUpdateRequest(2L, "MONDAY", LocalTime.of(9, 0), LocalTime.of(17, 0));
    }

    private StaffShiftResponse createResponse() {
        return new StaffShiftResponse(null, null);
    }

    @Test
    void testGetAllStaffShifts() {
        List<StaffShiftResponse> expected = List.of(createResponse());
        when(staffShiftService.findAll()).thenReturn(expected);

        List<StaffShiftResponse> actual = controller.getAllStaffShifts();

        assertThat(actual).isSameAs(expected);
        verify(staffShiftService).findAll();
    }

    @Test
    void testGetStaffShiftById() {
        StaffShiftResponse expected = createResponse();
        when(staffShiftService.findById(2L, 3L)).thenReturn(expected);

        StaffShiftResponse actual = controller.getStaffShiftById(2L, 3L);

        assertThat(actual).isSameAs(expected);
        verify(staffShiftService).findById(2L, 3L);
    }

    @Test
    void testUpdateStaffShift_sendsCorrectDto() {
        ShiftUpdateRequest request = createUpdateRequest();
        StaffShiftResponse response = createResponse();
        when(staffShiftService.updateShiftForStaffDay(any())).thenReturn(response);

        StaffShiftResponse actual = controller.updateStaffShift(request);

        ArgumentCaptor<ShiftUpdateRequest> captor = ArgumentCaptor.forClass(ShiftUpdateRequest.class);
        verify(staffShiftService).updateShiftForStaffDay(captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testCreateStaffShift_sendsCorrectDto() {
        StaffShiftRequest request = createStaffShiftRequest();
        StaffShiftResponse response = createResponse();
        when(staffShiftService.create(any())).thenReturn(response);

        StaffShiftResponse actual = controller.createStaffShift(request);

        ArgumentCaptor<StaffShiftRequest> captor = ArgumentCaptor.forClass(StaffShiftRequest.class);
        verify(staffShiftService).create(captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testDeleteStaffShift() {
        controller.deleteStaffShift(2L, 3L);
        verify(staffShiftService).remove(2L, 3L);
    }
}

