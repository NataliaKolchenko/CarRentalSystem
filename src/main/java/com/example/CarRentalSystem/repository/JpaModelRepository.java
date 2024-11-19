package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaModelRepository  extends JpaRepository<Model, Long> {
    Model findByModelName(String modelName);

    List<Model> findByBrandId(Long brandId);
}
