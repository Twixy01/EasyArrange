package org.example.backend.Controller;

import org.example.backend.DTO.StaffService.StaffServiceRequest;
import org.example.backend.DTO.StaffService.StaffServiceResponse;
import org.example.backend.Service.StaffServiceJunctionService;
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
class StaffServiceRestControllerTest {

    @Mock
    private StaffServiceJunctionService staffServiceJunctionService;

    @InjectMocks
    private StaffServiceRestController controller;

    private StaffServiceRequest createRequest() {
        return new StaffServiceRequest(2L, 5L);
    }

    private StaffServiceResponse createResponse() {
        return new StaffServiceResponse(null, null);
    }

    @Test
    void testGetAllStaffServices() {
        List<StaffServiceResponse> expected = List.of(createResponse());
        when(staffServiceJunctionService.findAll()).thenReturn(expected);

        List<StaffServiceResponse> actual = controller.getAllStaffServices();

        assertThat(actual).isSameAs(expected);
        verify(staffServiceJunctionService).findAll();
    }

    @Test
    void testGetStaffServiceById() {
        StaffServiceResponse expected = createResponse();
        when(staffServiceJunctionService.findById(2L, 5L)).thenReturn(expected);

        StaffServiceResponse actual = controller.getStaffServiceById(2L, 5L);

        assertThat(actual).isSameAs(expected);
        verify(staffServiceJunctionService).findById(2L, 5L);
    }

    @Test
    void testCreateStaffService_sendsCorrectDto() {
        StaffServiceRequest request = createRequest();
        StaffServiceResponse response = createResponse();
        when(staffServiceJunctionService.create(any())).thenReturn(response);

        StaffServiceResponse actual = controller.createStaffService(request);

        ArgumentCaptor<StaffServiceRequest> captor = ArgumentCaptor.forClass(StaffServiceRequest.class);
        verify(staffServiceJunctionService).create(captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testDeleteStaffService() {
        controller.deleteStaffService(2L, 5L);
        verify(staffServiceJunctionService).remove(2L, 5L);
    }
}
