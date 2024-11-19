package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.SubType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSubTypeRepository extends JpaRepository<SubType, Long> {
    SubType findBySubTypeName(String vehicleSubTypeName);

    List<SubType> findByTypeId(Long vehicleType);
}
