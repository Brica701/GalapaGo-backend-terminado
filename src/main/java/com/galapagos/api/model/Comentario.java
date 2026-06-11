package com.galapagos.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne
    private Usuario usuario;

    @NotNull(message = "El servicio es obligatorio")
    @ManyToOne
    private Servicio servicio;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer puntuacion;

    @NotBlank(message = "El comentario no puede estar vacío")
    @Size(min = 5, max = 500, message = "El comentario debe tener entre 5 y 500 caracteres")
    private String texto;

    private LocalDateTime fechaPublicacion = LocalDateTime.now();

    private String respuestaAdmin;
    private LocalDateTime fechaRespuesta;
}