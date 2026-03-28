package org.example.backend.DTO.StaffService;

import org.example.backend.DTO.Service.ServiceResponse;
import org.example.backend.DTO.Staff.StaffResponse;

public record StaffServiceResponse(
        StaffResponse staff,
        ServiceResponse service
) {
}
