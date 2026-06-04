package tiendita.controlador;

import tiendita.modelo.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ControladorReserva {
    private ArrayList<Reserva> reservas = new ArrayList<>();
    private ControladorHabitacion ctrlHab;
    private ControladorHuesped ctrlHues;

    public ControladorReserva(ControladorHabitacion ctrlHab, ControladorHuesped ctrlHues) {
        this.ctrlHab = ctrlHab;
        this.ctrlHues = ctrlHues;
    }

    public Reserva crear(String idHuesped, int numHab,
                         LocalDate ingreso, LocalDate salida) throws Exception {
        Huesped huesped = ctrlHues.buscarPorId(idHuesped);
        if (huesped == null) throw new Exception("Huesped '" + idHuesped + "' no encontrado.");

        Habitacion hab = ctrlHab.buscar(numHab);
        if (hab == null) throw new Exception("Habitacion " + numHab + " no encontrada.");
        if (!hab.isDisponible()) throw new Exception("La habitacion " + numHab + " no esta disponible.");

        if (ingreso == null || salida == null)
            throw new Exception("Las fechas son obligatorias.");
        if (!salida.isAfter(ingreso))
            throw new Exception("La fecha de salida debe ser posterior al ingreso.");
        if (ingreso.isBefore(LocalDate.now()))
            throw new Exception("La fecha de ingreso no puede ser en el pasado.");

        Reserva r = new Reserva(huesped, hab, ingreso, salida);
        hab.setDisponible(false);
        reservas.add(r);
        return r;
    }

    public void cancelar(int idReserva) throws Exception {
        Reserva r = buscar(idReserva);
        if (r == null) throw new Exception("Reserva N " + idReserva + " no encontrada.");
        if (r.getEstado().equals("CANCELADA"))
            throw new Exception("La reserva N " + idReserva + " ya fue cancelada.");
        r.setEstado("CANCELADA");
        r.getHabitacion().setDisponible(true);
    }

    public Reserva buscar(int id) {
        for (Reserva r : reservas) if (r.getId() == id) return r;
        return null;
    }

    public ArrayList<Reserva> getReservas() { return reservas; }

    public Object[][] getMatriz() {
        Object[][] d = new Object[reservas.size()][8];
        for (int i = 0; i < reservas.size(); i++) {
            Reserva r = reservas.get(i);
            d[i][0] = r.getId();
            d[i][1] = r.getHuesped().getNombreCompleto();
            d[i][2] = r.getHabitacion().getNumero();
            d[i][3] = r.getHabitacion().getTipo();
            d[i][4] = r.getFechaIngreso();
            d[i][5] = r.getFechaSalida();
            d[i][6] = "$" + String.format("%,.0f", r.getCostoTotal());
            d[i][7] = r.getEstado();
        }
        return d;
    }

    public String[] getColumnas() {
        return new String[]{"N Reserva", "Huesped", "N Hab", "Tipo", "Ingreso", "Salida", "Total", "Estado"};
    }
}
