package com.enviro.assessment.grad001.favourokwara.investment.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper=false) // exclude fields from BaseEntiy 
@Data
@NoArgsConstructor
@Entity
@Table(name = "investors")
public class Investor extends BaseEntity {

    @NotNull(message = "FIRST NAME is mandatory.")
    @NotBlank(message = "FIRST NAME can not be blank.")
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "")
    @NotBlank(message = "LAST NAME can not be blank.")
    @Column(name = "last_name")
    private String lastName;

    @Email
    @NotNull(message = "EMAIL is mandatory.")
    @NotBlank(message = "EMAIL can not be blank.")
    @Column(name = "email")
    private String email;

    @NotNull(message = "PHONE NUMBER is mandatory.")
    @NotBlank(message = "PHONE NUMBER can not be blank.")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Past
    @NotNull(message = "DATE OF BIRTH is mandatory.")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToMany(
        mappedBy = "investor",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Product> products;

    public int calculateAge() {
        return Period
            .between(dateOfBirth, LocalDate.now())
            .getYears();
    }
}