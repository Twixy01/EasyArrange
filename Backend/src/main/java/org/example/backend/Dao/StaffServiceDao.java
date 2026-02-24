package org.example.backend.Dao;

import org.example.backend.Model.entity.Service;
import org.example.backend.Model.entity.StaffService;

import java.util.List;

public interface StaffServiceDao extends Dao<StaffService>{
    List<Service> findAllServiceByStaffId(long staffId);
    List<Service> findAllServiceByServiceId(long serviceId);
}
