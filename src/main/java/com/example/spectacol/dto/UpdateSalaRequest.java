package com.example.spectacol.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateSalaRequest {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;
}

