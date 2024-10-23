package com.example.CarRentalSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@EqualsAndHashCode
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "BranchName may not be blank")
    private String branchName;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @NotNull(message = "Address may not be null")
    private Address address;

    @NotBlank(message = "Phone may not be blank")
    private String phone;

    @NotBlank(message = "WorkingTime may not be blank")
    private String workingTime;

    public Branch() {
    }

    public Branch(Long id, String branchName, Address address, String phone, String workingTime) {
        this.id = id;
        this.branchName = branchName;
        this.address = address;
        this.phone = phone;
        this.workingTime = workingTime;
    }
}
