package org.example.backend.Service.impl;

import org.example.backend.Dao.StaffServiceDao;
import org.example.backend.Model.entity.Service;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Service.StaffService;
import org.example.backend.Service.StaffServiceService;

import java.util.List;

public class StaffServiceServiceImpl implements StaffServiceService {
    private final StaffServiceDao staffServiceDao;

    public StaffServiceServiceImpl(StaffServiceDao staffServiceDao) {
        this.staffServiceDao = staffServiceDao;
    }

    @Override
    public List<Service> getAllServicesByStaffId(long staffId) {
        return staffServiceDao.findAllServicesByStaffId(staffId);
    }

    @Override
    public List<Staff> getAllStaffByServiceId(long serviceId) {
        return staffServiceDao.findAllStaffByServiceId(serviceId);
    }
}
