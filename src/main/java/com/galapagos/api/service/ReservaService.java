package com.galapagos.api.service;

import com.galapagos.api.model.Reserva;
import com.galapagos.api.model.Servicio;
import com.galapagos.api.model.Usuario;
import com.galapagos.api.model.Habitacion;
import com.galapagos.api.repository.ReservaRepository;
import com.galapagos.api.repository.ServicioRepository;
import com.galapagos.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public Map<String, Object> obtenerEstadisticasDashboard() {
        Map<String, Object> stats = new HashMap<>();
        long totalReservas = reservaRepository.count();
        Double totalIngresos = reservaRepository.sumTotalIngresos();

        stats.put("totalReservas", totalReservas);
        stats.put("totalIngresos", totalIngresos != null ? totalIngresos : 0.0);
        return stats;
    }

    @Transactional
    public Reserva crearReserva(Reserva reserva) {

        Servicio servicioDB = servicioRepository.findById(reserva.getServicio().getId())
                .orElseThrow(() -> new RuntimeException("Error: El servicio no existe en la base de datos."));

        Usuario usuarioDB = usuarioRepository.findById(reserva.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Error: El usuario no existe."));

        reserva.setServicio(servicioDB);
        reserva.setUsuario(usuarioDB);

        if ("HOTEL".equalsIgnoreCase(servicioDB.getCategoria())) {
            if (reserva.getFechaInicio() == null || reserva.getFechaFin() == null) {
                throw new RuntimeException("Las fechas de check-in y check-out son obligatorias para hoteles.");
            }

            long noches = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
            if (noches <= 0) {
                throw new RuntimeException("La fecha de fin debe ser posterior a la fecha de inicio.");
            }

            Habitacion habitacionSeleccionada = null;
            if (reserva.getHabitacion() != null && reserva.getHabitacion().getId() != null) {
                habitacionSeleccionada = servicioDB.getHabitaciones().stream()
                        .filter(h -> h.getId().equals(reserva.getHabitacion().getId()))
                        .findFirst()
                        .orElse(null);
            }

            if (habitacionSeleccionada == null && reserva.getHabitacion() != null) {
                String tipoBuscado = reserva.getHabitacion().getTipo();
                habitacionSeleccionada = servicioDB.getHabitaciones().stream()
                        .filter(h -> h.getTipo().equalsIgnoreCase(tipoBuscado))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("El tipo de habitación especificado no existe en este hotel."));
            }

            if (habitacionSeleccionada == null) {
                throw new RuntimeException("Debe seleccionar una habitación válida.");
            }

            reserva.setHabitacion(habitacionSeleccionada);

            int habsSolicitadas = reserva.getCantidadHabitaciones() != null ? reserva.getCantidadHabitaciones() : 1;
            int stockMaximoHabitacion = habitacionSeleccionada.getCantidadTotal();

            Integer habsOcupadas = reservaRepository.contarHabitacionesReservadasEnFechas(
                    habitacionSeleccionada.getId(),
                    reserva.getFechaInicio(),
                    reserva.getFechaFin()
            );
            if (habsOcupadas == null) habsOcupadas = 0;

            int habsDisponiblesReales = stockMaximoHabitacion - habsOcupadas;

            if (habsSolicitadas > habsDisponiblesReales) {
                throw new RuntimeException("Lo sentimos, no hay disponibilidad para las fechas seleccionadas. " +
                        "Quedan " + habsDisponiblesReales + " habitaciones libres de tipo " + habitacionSeleccionada.getTipo());
            }

            reserva.setPrecioTotal(habitacionSeleccionada.getPrecioPorNoche() * habsSolicitadas * noches);

        } else {
            reserva.setPrecioTotal(servicioDB.getPrecioBase() * reserva.getCantidadPersonas());
            reserva.setFechaFin(reserva.getFechaInicio());

            if (servicioDB.getCupoDisponible() != null) {
                int cupoActual = servicioDB.getCupoDisponible();
                int plazasSolicitadas = reserva.getCantidadPersonas();

                if (plazasSolicitadas > cupoActual) {
                    throw new RuntimeException("Lo sentimos, no hay suficientes plazas disponibles. Solo quedan " + cupoActual + " cupos.");
                }
                servicioDB.setCupoDisponible(cupoActual - plazasSolicitadas);
                servicioRepository.save(servicioDB);
            }
        }

        reserva.setEstado("CONFIRMADA");
        return reservaRepository.save(reserva);
    }

    public List<Reserva> listarPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public void eliminarReserva(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: La reserva que intentas eliminar no existe."));


        Servicio servicio = reserva.getServicio();

        if ("HOTEL".equalsIgnoreCase(servicio.getCategoria())) {

        } else {

            if (servicio.getCupoDisponible() != null) {
                int plazasCanceladas = reserva.getCantidadPersonas();
                servicio.setCupoDisponible(servicio.getCupoDisponible() + plazasCanceladas);
                servicioRepository.save(servicio);
            }
        }

        reservaRepository.delete(reserva);
    }


    @Transactional
    public Reserva confirmarReserva(Long id) {

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));

        reserva.setEstado("CONFIRMADA");

        return reservaRepository.save(reserva);
    }
}