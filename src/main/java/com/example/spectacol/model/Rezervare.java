package com.example.spectacol.model;

import com.example.spectacol.model.enums.ReservationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rezervari")
@Setter
@Getter
@NoArgsConstructor
public class Rezervare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "spectacol_id", nullable = false)
    private Spectacol spectacol;

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private Double totalPrice;

    @OneToMany(mappedBy = "rezervare", cascade = CascadeType.ALL)
    private List<RezervareLoc> rezervareLocuri;

    public Rezervare(Client client, Spectacol spectacol,
                     LocalDateTime reservationDate,
                     ReservationStatus status,
                     Double totalPrice) {
        this.client = client;
        this.spectacol = spectacol;
        this.reservationDate = reservationDate;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
