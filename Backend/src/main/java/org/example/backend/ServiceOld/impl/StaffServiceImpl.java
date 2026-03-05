package org.example.backend.Service.impl;

import org.example.backend.RepositoryOld.StaffDao;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Service.StaffService;

public class StaffServiceImpl implements StaffService {
    private final StaffDao staffDao;

    public StaffServiceImpl(StaffDao staffDao) {
        this.staffDao = staffDao;
    }


    @Override
    public Staff getByUserId(long userId) {
        return staffDao.findByUserId(userId);
    }
}
