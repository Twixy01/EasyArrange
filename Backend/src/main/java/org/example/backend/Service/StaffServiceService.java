package org.example.backend.Service;

import org.example.backend.Model.entity.Service;

import java.util.List;

public interface StaffServiceService {
    List<Service> getAllServiceByStaffId(long staffId);
    List<Service> getAllServiceByServiceId(long serviceId);

}
