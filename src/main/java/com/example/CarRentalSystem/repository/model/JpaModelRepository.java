package com.example.CarRentalSystem.repository.model;

import com.example.CarRentalSystem.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaModelRepository  extends JpaRepository<Model, Long> {
    Model findByModelName(String modelName);
}
