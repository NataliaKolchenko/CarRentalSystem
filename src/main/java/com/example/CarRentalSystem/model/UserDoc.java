package com.example.CarRentalSystem.model;

import com.example.CarRentalSystem.enums.UserDocType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class UserDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//сделать проброс userID
    @NotBlank(message = "userDocType may not be blank or null or has spaces")
    private UserDocType userDocType;

    @URL
    @NotBlank
    private String link;

    @NotBlank(message = "userId may not be blank or null or has spaces")
    private Long userId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public UserDoc() {
    }

    public UserDoc(UserDocType userDocType, String link, Long userId, LocalDateTime updateDate) {
        this.userDocType = userDocType;
        this.link = link;
        this.userId = userId;
        this.createDate = LocalDateTime.now();
        this.updateDate = updateDate;
    }
}
