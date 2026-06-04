package tiendita.modelo;

/**
 * Empleado hereda de Persona.
 */
public class Empleado extends Persona {
    private String cargo;
    private double salario;

    public Empleado(String nombre, String apellido, String identificacion,
                    String telefono, String email, String cargo, double salario) {
        super(nombre, apellido, identificacion, telefono, email);
        this.cargo = cargo;
        this.salario = salario;
    }

    @Override
    public String mostrarInformacion() {
        return "EMPLEADO | " + getNombreCompleto() + " | Cargo: " + cargo;
    }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }
}
