package com.example.CarRentalSystem.repository.brand;

import com.example.CarRentalSystem.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBrandRepository extends JpaRepository<Brand, Long> {
    Brand findByBrandName(String brandName);
}
