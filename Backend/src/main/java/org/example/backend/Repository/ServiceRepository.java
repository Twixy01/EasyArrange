package org.example.backend.Repository;

import org.example.backend.Model.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ServiceRepository extends JpaRepository<Service, Long> {

    Optional<Service> findByName(String name);
    List<Service> findAllByOrderByNameAsc();
    boolean serviceExists(String service);

}
