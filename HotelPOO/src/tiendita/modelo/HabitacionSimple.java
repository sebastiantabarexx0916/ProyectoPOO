package tiendita.modelo;

// ─── Habitacion Simple ────────────────────────────────────────────────────────
public class HabitacionSimple extends Habitacion {
    private static final double TARIFA = 120000;

    public HabitacionSimple(int numero, int piso, String descripcion) {
        super(numero, piso, descripcion);
    }

    @Override public double calcularCosto(int noches) { return TARIFA * noches; }
    @Override public double getTarifaNoche() { return TARIFA; }
    @Override public String getTipo() { return "Simple"; }

    @Override
    public String mostrarInformacion() {
        return "Simple | Hab " + numero + " | Piso " + piso
               + " | $" + TARIFA + "/noche | " + (disponible ? "Disponible" : "Ocupada");
    }
}
