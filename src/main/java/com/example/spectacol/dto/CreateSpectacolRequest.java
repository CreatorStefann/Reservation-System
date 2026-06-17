package com.example.spectacol.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class CreateSpectacolRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    @Future
    private LocalDateTime dateTime;

    @NotNull
    @Min(1)
    private Double price;

    @NotNull
    private Long salaId;
}
