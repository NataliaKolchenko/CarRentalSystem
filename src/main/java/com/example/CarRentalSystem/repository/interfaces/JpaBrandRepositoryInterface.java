package com.example.CarRentalSystem.repository.interfaces;

import com.example.CarRentalSystem.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBrandRepositoryInterface extends JpaRepository<Brand, Long> {
    Brand findByBrandName(String brandName);
}
