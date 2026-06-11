package com.galapagos.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_servicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "detalle_texto", columnDefinition = "TEXT")
    private String detalleTexto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id")
    @JsonBackReference
    private Servicio servicio;
}