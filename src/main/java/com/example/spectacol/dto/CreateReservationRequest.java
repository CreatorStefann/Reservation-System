package com.example.spectacol.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CreateReservationRequest {

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "Spectacol ID is required")
    private Long spectacolId;

    @NotEmpty(message = "At least one seat must be selected")
    private List<Long> locIds;
}
