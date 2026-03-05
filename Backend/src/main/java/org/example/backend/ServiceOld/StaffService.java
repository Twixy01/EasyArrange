package org.example.backend.Service;

import org.example.backend.Model.entity.Staff;

public interface StaffService {
    Staff getByUserId(long userId);
}
