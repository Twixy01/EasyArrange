package org.example.backend.Service;

import org.example.backend.DTO.Service.ServiceResponseMapper;
import org.example.backend.DTO.Staff.StaffRequest;
import org.example.backend.DTO.Staff.StaffResponse;
import org.example.backend.DTO.Staff.StaffResponseMapper;
import org.example.backend.Model.entity.Staff;
import org.example.backend.Model.entity.User;
import org.example.backend.Repository.StaffRepository;
import org.example.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffService {
    private UserRepository userRepository;
    private StaffRepository staffRepository;
    private StaffResponseMapper staffResponseMapper;

    @Autowired
    public StaffService(UserRepository userRepository, StaffRepository staffRepository, StaffResponseMapper staffResponseMapper) {
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.staffResponseMapper = staffResponseMapper;
    }

    public List<StaffResponse> findAll() {
        return staffRepository.findAll().stream()
                .map(staffResponseMapper)
                .collect(Collectors.toList());
    }

    public StaffResponse findById(Long id) {
        Optional<Staff> staff = staffRepository.findById(id);
        return staff.map(staffResponseMapper).orElseThrow(() -> new IllegalArgumentException("Staff Not Found!"));
    }

    public StaffResponse findStaffByUserId(Long id){
        Optional<Staff> staff = staffRepository.findStaffByUserId(id);
        return staff.map(staffResponseMapper).orElseThrow(() -> new IllegalArgumentException("Staff Not Found By UserId!"));
    }

    public boolean existsByUserId(Long userId) {
        return staffRepository.existsByUserId(userId);
    }

    public StaffResponse create(StaffRequest staffDto) {
        if (existsByUserId(staffDto.userId())) {
            throw new IllegalArgumentException("User with id " + staffDto.userId() + " is already a staff member.");
        }
        User user = userRepository.findById(staffDto.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + staffDto.userId()));

        String roleName = user.getRole().getName();
        if (!roleName.equals("STAFF") && !roleName.equals("ADMIN")) {
            throw new IllegalArgumentException("User must have STAFF or ADMIN role to be added as staff.");
        }
        Staff staff = new Staff();
        staff.setUser(user);
        staffRepository.save(staff);
        return staffResponseMapper.apply(staff);
    }

    //doesn't make sense to update a staff
    /*public Staff update(Staff staff) {
        return staffRepository.save(staff);
    }*/

    public void remove(Long id) {
        staffRepository.deleteById(id);
    }
}
