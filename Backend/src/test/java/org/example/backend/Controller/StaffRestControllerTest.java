package org.example.backend.Controller;

import org.example.backend.DTO.Staff.StaffRequest;
import org.example.backend.DTO.Staff.StaffResponse;
import org.example.backend.Service.StaffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StaffRestControllerTest {

    @Mock
    private StaffService staffService;

    @InjectMocks
    private StaffRestController controller;

    private StaffRequest createRequest() {
        return new StaffRequest(7L, "Senior Stylist", "Bio text");
    }

    private StaffResponse createResponse() {
        return new StaffResponse(2L, null, "Senior Stylist", "Bio text", List.of());
    }

    @Test
    void testGetStaff() {
        List<StaffResponse> expected = List.of(createResponse());
        when(staffService.findAll()).thenReturn(expected);

        List<StaffResponse> actual = controller.getStaff();

        assertThat(actual).isSameAs(expected);
        verify(staffService).findAll();
    }

    @Test
    void testGetStaffById() {
        StaffResponse expected = createResponse();
        when(staffService.findById(1L)).thenReturn(expected);

        StaffResponse actual = controller.getStaffById(1L);

        assertThat(actual).isSameAs(expected);
        verify(staffService).findById(1L);
    }

    @Test
    void testAddStaff_sendsCorrectDto() {
        StaffRequest request = createRequest();
        StaffResponse response = createResponse();
        when(staffService.create(any())).thenReturn(response);

        StaffResponse actual = controller.addStaff(request);

        ArgumentCaptor<StaffRequest> captor = ArgumentCaptor.forClass(StaffRequest.class);
        verify(staffService).create(captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testDeleteStaff() {
        controller.deleteStaff(1L);
        verify(staffService).remove(1L);
    }
}

