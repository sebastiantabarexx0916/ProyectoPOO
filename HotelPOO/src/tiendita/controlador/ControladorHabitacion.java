package tiendita.controlador;

import tiendita.modelo.*;
import java.util.ArrayList;

/**
 * ControladorHabitacion: logica de negocio para habitaciones.
 */
public class ControladorHabitacion {
    private ArrayList<Habitacion> habitaciones = new ArrayList<>();

    public ControladorHabitacion() {
        // Datos de ejemplo
        habitaciones.add(new HabitacionSimple(101, 1, "Vista al jardin"));
        habitaciones.add(new HabitacionSimple(102, 1, "Vista interior"));
        habitaciones.add(new HabitacionDoble(201, 2, "Vista a la piscina"));
        habitaciones.add(new HabitacionDoble(202, 2, "Vista a la ciudad"));
        habitaciones.add(new Suite(301, 3, "Suite presidencial", true));
        habitaciones.add(new Suite(302, 3, "Suite estandar", false));
    }

    public void agregar(Habitacion h) throws Exception {
        for (Habitacion x : habitaciones)
            if (x.getNumero() == h.getNumero())
                throw new Exception("Ya existe la habitacion N " + h.getNumero());
        habitaciones.add(h);
    }

    public void eliminar(int numero) throws Exception {
        Habitacion h = buscar(numero);
        if (h == null) throw new Exception("Habitacion no encontrada.");
        if (!h.isDisponible()) throw new Exception("No se puede eliminar una habitacion ocupada.");
        habitaciones.remove(h);
    }

    public void actualizarDescripcion(int numero, String desc) throws Exception {
        Habitacion h = buscar(numero);
        if (h == null) throw new Exception("Habitacion no encontrada.");
        h.setDescripcion(desc);
    }

    public void actualizarDisponibilidad(int numero, boolean disponible) throws Exception {
        Habitacion h = buscar(numero);
        if (h == null) throw new Exception("Habitacion no encontrada.");
        h.setDisponible(disponible);
    }

    public Habitacion buscar(int numero) {
        for (Habitacion h : habitaciones)
            if (h.getNumero() == numero) return h;
        return null;
    }

    public ArrayList<Habitacion> getDisponibles() {
        ArrayList<Habitacion> lista = new ArrayList<>();
        for (Habitacion h : habitaciones) if (h.isDisponible()) lista.add(h);
        return lista;
    }

    public ArrayList<Habitacion> getHabitaciones() { return habitaciones; }

    public Object[][] getMatriz() {
        Object[][] d = new Object[habitaciones.size()][6];
        for (int i = 0; i < habitaciones.size(); i++) {
            Habitacion h = habitaciones.get(i);
            d[i][0] = h.getNumero();
            d[i][1] = h.getTipo();
            d[i][2] = h.getPiso();
            d[i][3] = "$" + String.format("%,.0f", h.getTarifaNoche());
            d[i][4] = h.getDescripcion();
            d[i][5] = h.isDisponible() ? "Disponible" : "Ocupada";
        }
        return d;
    }

    public String[] getColumnas() {
        return new String[]{"N Hab", "Tipo", "Piso", "Tarifa/Noche", "Descripcion", "Estado"};
    }
}
