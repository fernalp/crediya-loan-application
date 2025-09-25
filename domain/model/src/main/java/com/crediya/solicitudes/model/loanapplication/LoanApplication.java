package com.crediya.solicitudes.model.loanapplication;
import com.crediya.solicitudes.model.customer.Customer;
import com.crediya.solicitudes.model.loanstatus.LoanStatus;
import com.crediya.solicitudes.model.loantype.LoanType;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class LoanApplication {

    private BigInteger id;
    private String idNumber;
    private BigDecimal amount;
    private Integer term;
    private String email;
    private Integer idLoanType;
    private Integer idLoanStatus;
    private LoanStatus loanStatus;
    private LoanType loanType;
    private String token;
    private Customer customer;
    private BigDecimal monthlyPayment;

}
