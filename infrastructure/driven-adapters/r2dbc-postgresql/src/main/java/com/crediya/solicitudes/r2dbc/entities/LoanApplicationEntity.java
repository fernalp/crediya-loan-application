package com.crediya.solicitudes.r2dbc.entities;

import com.crediya.solicitudes.model.loanstatus.LoanStatus;
import com.crediya.solicitudes.model.loantype.LoanType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.math.BigInteger;

@Table("loan_application")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationEntity {
    @Id
    private BigInteger id;
    private BigDecimal amount;
    private BigInteger term;
    private String email;
    @Column("id_loan_status")
    private Integer idLoanStatus;
    @Column("id_loan_type")
    private Integer idLoanType;
    @Transient
    private LoanStatus loanStatus;
    @Transient
    private LoanType loanType;
}
