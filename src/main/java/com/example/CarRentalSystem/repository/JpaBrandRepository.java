package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBrandRepository extends JpaRepository<Brand, Long> {
    Brand findByBrandName(String brandName);
}
