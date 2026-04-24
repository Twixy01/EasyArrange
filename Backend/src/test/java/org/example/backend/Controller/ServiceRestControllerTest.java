package org.example.backend.Controller;

import org.example.backend.DTO.Service.ServiceRequest;
import org.example.backend.DTO.Service.ServiceResponse;
import org.example.backend.Service.ServiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceRestControllerTest {

    @Mock
    private ServiceService serviceService;

    @InjectMocks
    private ServiceRestController controller;

    private ServiceRequest createRequest() {
        return new ServiceRequest("Haircut", 5000, 60, "Classic haircut", "img.png");
    }

    private ServiceResponse createResponse() {
        return new ServiceResponse(5L, "Haircut", 5000, 60, "Classic haircut", "img.png");
    }

    @Test
    void testGetServices() {
        List<ServiceResponse> expected = List.of(createResponse());
        when(serviceService.findAll()).thenReturn(expected);

        List<ServiceResponse> actual = controller.getServices();

        assertThat(actual).isSameAs(expected);
        verify(serviceService).findAll();
    }

    @Test
    void testGetServiceById() {
        ServiceResponse expected = createResponse();
        when(serviceService.findById(1L)).thenReturn(expected);

        ServiceResponse actual = controller.getServiceById(1L);

        assertThat(actual).isSameAs(expected);
        verify(serviceService).findById(1L);
    }

    @Test
    void testCreateService_sendsCorrectDto() {
        ServiceRequest request = createRequest();
        ServiceResponse response = createResponse();
        when(serviceService.create(any())).thenReturn(response);

        ServiceResponse actual = controller.createService(request);

        ArgumentCaptor<ServiceRequest> captor = ArgumentCaptor.forClass(ServiceRequest.class);
        verify(serviceService).create(captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testUpdateService_sendsCorrectDto() {
        ServiceRequest request = createRequest();
        ServiceResponse response = createResponse();
        when(serviceService.update(eq(1L), any())).thenReturn(response);

        ServiceResponse actual = controller.updateService(1L, request);

        ArgumentCaptor<ServiceRequest> captor = ArgumentCaptor.forClass(ServiceRequest.class);
        verify(serviceService).update(eq(1L), captor.capture());

        assertThat(captor.getValue()).isSameAs(request);
        assertThat(actual).isSameAs(response);
    }

    @Test
    void testDeleteService() {
        controller.deleteService(1L);
        verify(serviceService).remove(1L);
    }
}
