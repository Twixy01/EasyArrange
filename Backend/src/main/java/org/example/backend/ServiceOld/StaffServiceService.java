package org.example.backend.Service;

import org.example.backend.Model.entity.Service;
import org.example.backend.Model.entity.Staff;

import java.util.List;

public interface StaffServiceService {
    List<Service> getAllServicesByStaffId(long staffId);
    List<Staff> getAllStaffByServiceId(long serviceId);
}
