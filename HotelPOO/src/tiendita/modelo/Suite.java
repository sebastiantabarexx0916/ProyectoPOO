package tiendita.modelo;

public class Suite extends Habitacion {
    private static final double TARIFA = 450000;
    private boolean incluyeDesayuno;

    public Suite(int numero, int piso, String descripcion, boolean incluyeDesayuno) {
        super(numero, piso, descripcion);
        this.incluyeDesayuno = incluyeDesayuno;
    }

    @Override
    public double calcularCosto(int noches) {
        double costo = TARIFA * noches;
        return incluyeDesayuno ? costo * 1.05 : costo;
    }

    @Override public double getTarifaNoche() { return TARIFA; }
    @Override public String getTipo() { return incluyeDesayuno ? "Suite+Desayuno" : "Suite"; }

    @Override
    public String mostrarInformacion() {
        return getTipo() + " | Hab " + numero + " | Piso " + piso
               + " | $" + TARIFA + "/noche | " + (disponible ? "Disponible" : "Ocupada");
    }

    public boolean isIncluyeDesayuno() { return incluyeDesayuno; }
    public void setIncluyeDesayuno(boolean v) { this.incluyeDesayuno = v; }
}
