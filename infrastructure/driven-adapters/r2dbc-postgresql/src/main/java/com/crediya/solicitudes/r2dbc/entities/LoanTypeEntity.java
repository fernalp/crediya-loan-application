package com.crediya.solicitudes.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("loan_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LoanTypeEntity {
    @Id
    private Integer id;
    private String name;
    @Column("minimum_amount")
    private BigDecimal minimumAmount;
    @Column("maximum_amount")
    private BigDecimal maximumAmount;
    @Column("interest_rate")
    private BigDecimal interestRate;
    @Column("automatic_approved")
    private Boolean automaticApproved;
}
