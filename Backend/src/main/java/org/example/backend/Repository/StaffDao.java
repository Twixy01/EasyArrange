package org.example.backend.Repository;

import org.example.backend.Model.entity.Staff;

public interface StaffDao extends Dao<Staff>{
    Staff findByUserId(long userId);
}
