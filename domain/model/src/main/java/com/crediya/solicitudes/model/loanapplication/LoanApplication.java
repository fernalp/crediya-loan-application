package com.crediya.solicitudes.model.loanapplication;
import com.crediya.solicitudes.model.loanstatus.LoanStatus;
import com.crediya.solicitudes.model.loantype.LoanType;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanApplication {

    private BigInteger id;
    private String idNumber;
    private BigDecimal amount;
    private Integer term;
    private String email;
    private LoanStatus loanStatus;
    private LoanType loanType;

}
