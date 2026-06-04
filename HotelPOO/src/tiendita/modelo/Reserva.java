package tiendita.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Reserva: asocia un Huesped con una Habitacion y calcula el costo total.
 */
public class Reserva {
    private static int contadorId = 1;
    private int id;
    private Huesped huesped;
    private Habitacion habitacion;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
    private String estado;
    private double costoTotal;

    public Reserva(Huesped huesped, Habitacion habitacion,
                   LocalDate fechaIngreso, LocalDate fechaSalida) {
        this.id = contadorId++;
        this.huesped = huesped;
        this.habitacion = habitacion;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.estado = "ACTIVA";
        this.costoTotal = habitacion.calcularCosto(getNoches());
    }

    public int getNoches() {
        return (int) ChronoUnit.DAYS.between(fechaIngreso, fechaSalida);
    }

    public String generarComprobante() {
        return "==============================\n"
             + "    COMPROBANTE DE RESERVA    \n"
             + "==============================\n"
             + "Reserva N:    " + id + "\n"
             + "Huesped:      " + huesped.getNombreCompleto() + "\n"
             + "ID Huesped:   " + huesped.getIdentificacion() + "\n"
             + "Habitacion:   " + habitacion.getNumero() + " (" + habitacion.getTipo() + ")\n"
             + "Check-in:     " + fechaIngreso + "\n"
             + "Check-out:    " + fechaSalida + "\n"
             + "Noches:       " + getNoches() + "\n"
             + "Tarifa/noche: $" + String.format("%,.0f", habitacion.getTarifaNoche()) + "\n"
             + "TOTAL:        $" + String.format("%,.0f", costoTotal) + "\n"
             + "Estado:       " + estado + "\n"
             + "==============================";
    }

    public int getId() { return id; }
    public Huesped getHuesped() { return huesped; }
    public Habitacion getHabitacion() { return habitacion; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public double getCostoTotal() { return costoTotal; }
}
