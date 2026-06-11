package com.galapagos.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @JsonProperty("precioBase")
    @Column(name = "precio_base_noche_persona")
    private Double precioBase;

    @Column(name = "imagen_url", columnDefinition = "TEXT")
    private String imagenUrl;

    private String categoria;
    private String ubicacion;
    private Integer cupoDisponible;
    private Boolean destacado;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "capacidad_max_persona")
    private Integer capacidadMaxPersona;

    @Column(name = "habitaciones_disponibles")
    private Integer habitacionesDisponibles;

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DetalleServicio> detallesDinamicos = new ArrayList<>();

    private Boolean tieneWifi;
    private Boolean tieneDesayuno;
    private Boolean tieneTransporte;
    private Boolean tieneEquipo;

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Habitacion> habitaciones = new ArrayList<>();
}