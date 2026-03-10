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

    public Staff findById(Long id) {
        Optional<Staff> staff = staffRepository.findById(id);
        return staff.orElseThrow(() -> new IllegalArgumentException("Staff Not Found!"));
    }

    public Staff findStaffByUserId(Long id){
        Optional<Staff> staff = staffRepository.findById(id);
        return staff.orElseThrow(() -> new IllegalArgumentException("Staff Not Found!"));
    }

    public Staff create(Staff staff) {
        return staffRepository.save(staff);
    }

    public Staff update(Staff staff) {
        return staffRepository.save(staff);
    }

    public void remove(Long id) {
        staffRepository.deleteById(id);
    }
}
