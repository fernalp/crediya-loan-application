package com.crediya.solicitudes.consumer.mappers;

import com.crediya.solicitudes.consumer.CustomerResponse;
import com.crediya.solicitudes.model.customer.Customer;

public class CustomerRestMapper {

    public static Customer toCustomer(CustomerResponse customerResponse) {
        return Customer.builder()
                .id(customerResponse.id())
                .idNumber(customerResponse.numeroIdentificacion())
                .firstName(customerResponse.nombres())
                .lastName(customerResponse.apellidos())
                .birthDate(customerResponse.fechaNacimiento())
                .address(customerResponse.direccion())
                .phone(customerResponse.telefono())
                .email(customerResponse.correoElectronico())
                .salary(customerResponse.salario())
                .idRole(customerResponse.idRol())
                .build();
    }

}
