package com.crediya.solicitudes.consumer.mappers;

import com.crediya.solicitudes.consumer.CustomerResponse;
import com.crediya.solicitudes.model.customer.Customer;

public class CustomerRestMapper {

    public static Customer toCustomer(CustomerResponse customerResponse) {
        return Customer.builder()
                .id(customerResponse.getId())
                .idNumber(customerResponse.getNumeroIdentificacion())
                .firstName(customerResponse.getNombres())
                .lastName(customerResponse.getApellidos())
                .birthDate(customerResponse.getFechaNacimiento())
                .address(customerResponse.getDireccion())
                .phone(customerResponse.getTelefono())
                .email(customerResponse.getCorreoElectronico())
                .salary(customerResponse.getSalario())
                .idRole(customerResponse.getIdRol())
                .build();
    }

}
