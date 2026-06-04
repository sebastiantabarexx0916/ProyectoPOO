package tiendita.modelo;

/**
 * Huesped hereda de Persona.
 * Representa al cliente que se hospeda en el hotel.
 */
public class Huesped extends Persona {
    private String nacionalidad;

    public Huesped(String nombre, String apellido, String identificacion,
                   String telefono, String email, String nacionalidad) {
        super(nombre, apellido, identificacion, telefono, email);
        this.nacionalidad = nacionalidad;
    }

    @Override
    public String mostrarInformacion() {
        return "HUESPED | " + getNombreCompleto() + " | ID: " + identificacion
               + " | Tel: " + telefono + " | " + nacionalidad;
    }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
}
