package com.crediya.solicitudes.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PageFilter{

    private int page;
    private int size;
    private String sort;
    private String direction;

}
