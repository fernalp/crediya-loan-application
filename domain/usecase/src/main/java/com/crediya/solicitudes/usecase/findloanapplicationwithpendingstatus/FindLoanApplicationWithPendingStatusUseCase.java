package com.crediya.solicitudes.usecase.findloanapplicationwithpendingstatus;

import com.crediya.solicitudes.model.PageFilter;
import com.crediya.solicitudes.model.PageResponse;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loanstatus.gateways.LoanStatusRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class FindLoanApplicationWithPendingStatusUseCase {

    private static final String DEFAULT_LOAN_STATUS = "PENDIENTE";
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final String DEFAULT_SORT = "id";
    private static final String DEFAULT_DIRECTION = "ASC";

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanStatusRepository loanStatusRepository;

    public Mono<PageResponse<LoanApplication>> execute(PageFilter pageFilter ) {
        if (pageFilter == null) {
            pageFilter = PageFilter.builder()
                    .page(DEFAULT_PAGE)
                    .size(DEFAULT_SIZE)
                    .sort(DEFAULT_SORT)
                    .direction(DEFAULT_DIRECTION)
                    .build();
        }

        Mono<Long> total = loanStatusRepository.findByName(DEFAULT_LOAN_STATUS)
                .flatMap(loanApplicationRepository::countByLoanStatus);
        Mono<List<LoanApplication>> loanApplications = loanApplicationRepository.findAllByFilter(pageFilter).collectList();
        PageFilter finalPageFilter = pageFilter;
        return Mono.zip(total, loanApplications)
                .map(tuple -> new PageResponse<LoanApplication>(
                        finalPageFilter.getPage(),
                        finalPageFilter.getSize(),
                        tuple.getT1(),
                        Math.ceilDiv(Math.toIntExact(tuple.getT1()), finalPageFilter.getSize()),
                        tuple.getT2()));
    }
}
