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
public class UpdateSpectacolRequest {

    @NotBlank(message = "Title is mandatory")
    private String title;

    private String description;

    @NotNull(message = "Date and time are mandatory")
    @Future(message = "Spectacol must be scheduled in the future")
    private LocalDateTime dateTime;

    @NotNull(message = "Price is mandatory")
    @Min(value = 1, message = "Price must be at least 1")
    private Double price;
}

