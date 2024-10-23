package com.example.CarRentalSystem.model;

import com.example.CarRentalSystem.enums.DocType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
public class Doc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "docType may not be blank or null or has spaces")
    private DocType docType;
    @URL
    @NotBlank
    private String link;
    private Long userId;
    private Long vehicleId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public Doc() {
    }

    public Doc(Long id, DocType docType, String link, Long userId, Long vehicleId, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.docType = docType;
        this.link = link;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.createDate = LocalDateTime.now();
        this.updateDate = updateDate;
    }
}
