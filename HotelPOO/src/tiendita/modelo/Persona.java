package tiendita.modelo;

/**
 * Clase abstracta base Persona.
 * Aplica encapsulamiento y herencia hacia Huesped y Empleado.
 */
public abstract class Persona {
    protected String nombre;
    protected String apellido;
    protected String identificacion;
    protected String telefono;
    protected String email;

    public Persona(String nombre, String apellido, String identificacion,
                   String telefono, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.identificacion = identificacion;
        this.telefono = telefono;
        this.email = email;
    }

    // Metodo polimorfico que cada subclase implementa
    public abstract String mostrarInformacion();

    public String getNombreCompleto() { return nombre + " " + apellido; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String id) { this.identificacion = id; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
