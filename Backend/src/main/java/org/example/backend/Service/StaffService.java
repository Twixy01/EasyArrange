package org.example.backend.Service;

import org.example.backend.Model.entity.Staff;
import org.example.backend.Repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {
    private StaffRepository staffRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    public Staff findById(long id) {
        Optional<Staff> staff = staffRepository.findById(id);
        return staff.orElseThrow(() -> new IllegalArgumentException("Staff Not Found!"));
    }

    public Staff findStaffByUserId(long id){
        Optional<Staff> staff = staffRepository.findById(id);
        return staff.orElseThrow(() -> new IllegalArgumentException("Staff Not Found!"));
    }

    public void remove(long id) {
        staffRepository.deleteById(id);
    }

    public Staff update(Staff staff) {
        return staffRepository.save(staff);
    }

    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }
}
