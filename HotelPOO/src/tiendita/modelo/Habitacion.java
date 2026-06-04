package tiendita.modelo;

/**
 * Habitacion abstracta: base para Simple, Doble y Suite.
 * Define calcularCosto() y mostrarInformacion() de forma polimorfica.
 */
public abstract class Habitacion {
    protected int numero;
    protected int piso;
    protected boolean disponible;
    protected String descripcion;

    public Habitacion(int numero, int piso, String descripcion) {
        this.numero = numero;
        this.piso = piso;
        this.descripcion = descripcion;
        this.disponible = true;
    }

    // Polimorfismo: cada tipo calcula su costo de forma distinta
    public abstract double calcularCosto(int noches);
    public abstract String mostrarInformacion();
    public abstract String getTipo();
    public abstract double getTarifaNoche();

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public int getPiso() { return piso; }
    public void setPiso(int piso) { this.piso = piso; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
