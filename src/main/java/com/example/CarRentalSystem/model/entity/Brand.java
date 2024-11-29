package com.example.CarRentalSystem.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "brandName may not be blank or null or has spaces")
    private String brandName;

    public Brand() {
    }
    public Brand(String brandName) {
        this.brandName = brandName;
    }
}
