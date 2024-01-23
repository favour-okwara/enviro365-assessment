package com.enviro.assessment.grad001.favourokwara.investment.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper=false)
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @NonNull
    @NotBlank(message = "NAME can not be blank.")
    @NotNull(message = "NAME is mandtory.")
    @Column(name = "name")
    private String name;

    @NonNull
    @NotNull(message = "PRODUCT TYPE is mandtory.")
    @Column(name = "product_type")
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @NonNull
    @PositiveOrZero
    @NotNull(message = "BALANCE is mandtory.")
    @Column(name = "balance")
    private Double balance;

    @ManyToOne
    @JoinColumn(name = "investor_id")
    @JsonIgnore
    private Investor investor;

    @OneToMany(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<WithdrawalNotice> withdrawalNotices;
    
    public Product() {
    }
}
