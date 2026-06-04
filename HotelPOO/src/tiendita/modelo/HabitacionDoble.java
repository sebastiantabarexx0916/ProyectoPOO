package tiendita.modelo;

public class HabitacionDoble extends Habitacion {
    private static final double TARIFA = 200000;

    public HabitacionDoble(int numero, int piso, String descripcion) {
        super(numero, piso, descripcion);
    }

    @Override public double calcularCosto(int noches) { return TARIFA * noches; }
    @Override public double getTarifaNoche() { return TARIFA; }
    @Override public String getTipo() { return "Doble"; }

    @Override
    public String mostrarInformacion() {
        return "Doble | Hab " + numero + " | Piso " + piso
               + " | $" + TARIFA + "/noche | " + (disponible ? "Disponible" : "Ocupada");
    }
}
