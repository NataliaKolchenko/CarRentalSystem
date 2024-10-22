package com.example.CarRentalSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @NotBlank(message = "BranchName may not be blank")
    private String branchName;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @NotNull(message = "Address may not be null")
    private Address address;

    @Column
    @NotBlank(message = "Phone may not be blank")
    private String phone;

    @Column
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
