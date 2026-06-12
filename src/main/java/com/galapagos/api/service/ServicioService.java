package com.galapagos.api.service;

import com.galapagos.api.model.Servicio;
import com.galapagos.api.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;


    public List<Servicio> listarTodos() {
        return servicioRepository.findAllByOrderByIdDesc();
    }


    public List<Servicio> buscarConFiltros(String ubicacion, Integer adultos) {
        if (ubicacion != null && !ubicacion.trim().isEmpty()) {
            return servicioRepository.findByUbicacionContainingIgnoreCaseAndCapacidadMaxPersonaGreaterThanEqual(
                    ubicacion, adultos);
        } else {
            return servicioRepository.findByCapacidadMaxPersonaGreaterThanEqual(adultos);
        }
    }

    public Servicio obtenerPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El servicio con ID " + id + " no existe."));
    }

    public Servicio guardar(Servicio servicio) {

        if (servicio.getDetallesDinamicos() != null) {
            servicio.getDetallesDinamicos().forEach(d -> d.setServicio(servicio));
        }

        if ("HOTEL".equalsIgnoreCase(servicio.getCategoria())) {
            servicio.setFechaInicio(null);
            if (servicio.getHabitaciones() != null) {
                servicio.getHabitaciones().forEach(h -> h.setServicio(servicio));
            }
        } else {
            servicio.setHabitaciones(null);
            servicio.setHabitacionesDisponibles(0);
        }

        return servicioRepository.save(servicio);
    }

    public void eliminar(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: el servicio no existe.");
        }
        servicioRepository.deleteById(id);
    }

    public Servicio actualizar(Long id, Servicio nuevo) {
        return servicioRepository.findById(id).map(s -> {
            s.setNombre(nuevo.getNombre());
            s.setPrecioBase(nuevo.getPrecioBase());
            s.setCupoDisponible(nuevo.getCupoDisponible());
            s.setDescripcion(nuevo.getDescripcion());
            s.setUbicacion(nuevo.getUbicacion());
            s.setImagenUrl(nuevo.getImagenUrl());
            s.setCategoria(nuevo.getCategoria());
            s.setDestacado(nuevo.getDestacado());
            s.setCapacidadMaxPersona(nuevo.getCapacidadMaxPersona());
            s.setHabitacionesDisponibles(nuevo.getHabitacionesDisponibles());

            if ("HOTEL".equalsIgnoreCase(nuevo.getCategoria())) {
                s.setFechaInicio(null);
            } else if ("EXCURSION".equalsIgnoreCase(nuevo.getCategoria())) {
                s.setFechaInicio(nuevo.getFechaInicio());
                s.setHabitacionesDisponibles(0);
                if (s.getHabitaciones() != null) {
                    s.getHabitaciones().clear();
                }
            }

            if (nuevo.getDetallesDinamicos() != null) {
                s.getDetallesDinamicos().clear();
                nuevo.getDetallesDinamicos().forEach(d -> {
                    d.setServicio(s);
                    s.getDetallesDinamicos().add(d);
                });
            } else {
                s.getDetallesDinamicos().clear();
            }

            if ("HOTEL".equalsIgnoreCase(nuevo.getCategoria()) && nuevo.getHabitaciones() != null) {
                s.getHabitaciones().clear();
                nuevo.getHabitaciones().forEach(h -> {
                    h.setServicio(s);
                    s.getHabitaciones().add(h);
                });
            }

            return servicioRepository.save(s);
        }).orElseThrow(() -> new RuntimeException("No se pudo actualizar: el ID " + id + " no existe."));
    }

    public List<Servicio> listarTodoSinPaginacion() {
        return servicioRepository.findAllByOrderByIdDesc();
    }
}