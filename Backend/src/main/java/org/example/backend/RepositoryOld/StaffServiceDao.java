package org.example.backend.Repository;

import org.example.backend.Model.entity.Service;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.StaffService;

import java.util.List;

public interface StaffServiceDao extends Dao<StaffService>{
    List<Service> findAllServicesByStaffId(long staffId);
    List<Staff> findAllStaffByServiceId(long serviceId);
}
