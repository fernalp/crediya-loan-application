package com.crediya.solicitudes.usecase.updatestatusloanapplication;

import com.crediya.solicitudes.model.constants.LoanConstants;
import com.crediya.solicitudes.model.exception.ValidationException;
import com.crediya.solicitudes.model.gateway.SendMessageGateway;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loanstatus.gateways.LoanStatusRepository;
import com.crediya.solicitudes.model.loantype.gateways.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class UpdateStatusLoanApplicationUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanStatusRepository loanStatusRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final SendMessageGateway sendMessageGateway;

    public Mono<LoanApplication> execute(BigInteger loanApplicationId, String status) {
        return loanApplicationRepository.findById(loanApplicationId)
                .flatMap(loanApplication -> validateLoanStatus(loanApplication, status)
                        .flatMap(this::setLoanType)
                        .flatMap(loanApplicationRepository::save))
                .flatMap(loanApplication -> sendNotification(loanApplication, status)
                        .thenReturn(loanApplication));
    }

    private Mono<LoanApplication> validateLoanStatus(LoanApplication loanApplication,String status) {
        return loanStatusRepository.findByName(status)
                .flatMap(loanStatus -> {
                    loanApplication.setLoanStatus(loanStatus);
                    return Mono.just(loanApplication);
                })
                .switchIfEmpty(Mono.error(new ValidationException(LoanConstants.ERROR_MESSAGE_LOAN_STATUS_NOT_FOUND)));
    }

    private Mono<LoanApplication> setLoanType(LoanApplication loanApplication) {
        return loanTypeRepository.findById(loanApplication.getIdLoanType())
                .flatMap(loanType -> {
                    loanApplication.setLoanType(loanType);
                    return Mono.just(loanApplication);
                }).switchIfEmpty(Mono.error(new ValidationException(LoanConstants.ERROR_MESSAGE_LOAN_TYPE_NOT_FOUND)));
    }

    private Mono<String> sendNotification(LoanApplication loanApplication, String status) {
        Map<String, Object> message = new HashMap<>();
        message.put("loanApplicationId", loanApplication.getId());
        message.put("decision", status);
        message.put("applicantEmail", loanApplication.getEmail());

        String json = toJson(message);
        return sendMessageGateway.send(json);
    }

    private String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        for (Map.Entry<String, Object> e : map.entrySet()) {
            if (!first) sb.append(',');
            first = false;
            sb.append('"').append(e.getKey()).append('"').append(':');
            Object v = e.getValue();
            if (v == null) {
                sb.append("null");
            } else if (v instanceof Number || v instanceof Boolean) {
                sb.append(v.toString());
            } else {
                sb.append('"').append(escape(v.toString())).append('"');
            }
        }
        sb.append('}');
        return sb.toString();
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

}
