package com.example.spectacol.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateSalaRequest {

    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    private Integer rows;

    @NotNull
    @Min(1)
    private Integer seatsPerRow;
}
