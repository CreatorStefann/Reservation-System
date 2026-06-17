package com.example.spectacol.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "spectacole")
@Setter
@Getter
@NoArgsConstructor
public class Spectacol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @NotNull(message = "Date and time are mandatory")
    @Future(message = "Spectacol must be scheduled in the future")
    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Min(value = 1, message = "Price must be at least 1")
    @Column(nullable = false)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    public Spectacol(String title, String description,
                     LocalDateTime dateTime, Double price, Sala sala) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.price = price;
        this.sala = sala;
    }
}
