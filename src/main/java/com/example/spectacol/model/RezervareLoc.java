package com.example.spectacol.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rezervare_locuri")
@Setter
@Getter
@NoArgsConstructor
public class RezervareLoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "rezervare_id", nullable = false)
    private Rezervare rezervare;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "loc_id", nullable = false)
    private Loc loc;

    public RezervareLoc(Rezervare rezervare, Loc loc) {
        this.rezervare = rezervare;
        this.loc = loc;
    }
}
