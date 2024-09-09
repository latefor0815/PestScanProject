package com.busanit501.pesttestproject0909.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsectDto {
    private Long id;
    private String name;
    private String species;
    private String description;
}