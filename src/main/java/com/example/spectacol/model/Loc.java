package com.example.spectacol.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "locuri")
@Setter
@Getter
@NoArgsConstructor
public class Loc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "Row number must be at least 1")
    @Column(nullable = false)
    private Integer rowNumber;

    @Min(value = 1, message = "Seat number must be at least 1")
    @Column(nullable = false)
    private Integer seatNumber;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    public Loc(Integer rowNumber, Integer seatNumber, Sala sala) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.sala = sala;
    }
}
