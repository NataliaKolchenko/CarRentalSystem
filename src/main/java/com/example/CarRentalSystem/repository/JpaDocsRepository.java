package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.Doc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface JpaDocsRepository extends JpaRepository<Doc, Long> {
    List<Doc> findByVehicleId (Long id);
    List<Doc> findByUserId(Long id);
}
