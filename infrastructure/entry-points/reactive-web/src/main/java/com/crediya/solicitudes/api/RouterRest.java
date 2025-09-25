package com.crediya.solicitudes.api;

import com.crediya.solicitudes.api.constants.ApiConstants;
import com.crediya.solicitudes.api.openapi.LoanApplicationOpenApi;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;


@Configuration
public class RouterRest {

    @Bean
    public WebProperties.Resources resources(){
        return new WebProperties.Resources();
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction(HandlerV1 handlerV1) {
        return route()
                .POST(ApiConstants.PATH_API_LOAN_APPLICATIONS, handlerV1::createLoanApplication, LoanApplicationOpenApi::createLoanApplication)
                .GET(ApiConstants.PATH_API_LOAN_APPLICATIONS, handlerV1::getAllLoanApplicationPending, LoanApplicationOpenApi::getAllLoanApplicationPending)
                .build();
    }
}
