package com.example.CarRentalSystem.repository;

import com.example.CarRentalSystem.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBranchRepository extends JpaRepository<Branch, Long> {
    Branch findByBranchName(String branchName);
}
