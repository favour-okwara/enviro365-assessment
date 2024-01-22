package com.enviro.assessment.grad001.favourokwara.investment.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EntityListeners(AuditingEntityListener.class) // automatically update fields
@MappedSuperclass
public abstract class Auditable {
    
    @JsonInclude(Include.NON_NULL)
    @CreatedBy
    protected String createdBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdDate;

    @JsonInclude(Include.NON_NULL)
    @LastModifiedBy
    protected String lastModifiedBy;

    @JsonInclude(Include.NON_NULL)
    @LastModifiedDate
    protected LocalDateTime lastModified;
}
