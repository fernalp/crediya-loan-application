package com.crediya.solicitudes.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("loan_status")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoanStatusEntity {
    @Id
    private Integer id;
    private String name;
    private String description;
}
