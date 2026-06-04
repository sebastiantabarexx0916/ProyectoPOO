package tiendita.controlador;

import tiendita.modelo.Huesped;
import java.util.ArrayList;

public class ControladorHuesped {
    private ArrayList<Huesped> huespedes = new ArrayList<>();

    public ControladorHuesped() {
        huespedes.add(new Huesped("Carlos", "Gomez", "1234567890", "3001234567", "cgomez@mail.com", "Colombiana"));
        huespedes.add(new Huesped("Ana", "Martinez", "0987654321", "3109876543", "ana@mail.com", "Mexicana"));
    }

    public void registrar(Huesped h) throws Exception {
        if (h.getNombre().trim().isEmpty() || h.getApellido().trim().isEmpty())
            throw new Exception("Nombre y apellido son obligatorios.");
        if (h.getIdentificacion().trim().isEmpty())
            throw new Exception("La identificacion es obligatoria.");
        if (buscarPorId(h.getIdentificacion()) != null)
            throw new Exception("Ya existe un huesped con ID: " + h.getIdentificacion());
        if (!h.getTelefono().matches("\\d{7,15}"))
            throw new Exception("El telefono debe tener solo digitos (7 a 15).");
        huespedes.add(h);
    }

    public void eliminar(String id) throws Exception {
        Huesped h = buscarPorId(id);
        if (h == null) throw new Exception("Huesped con ID " + id + " no encontrado.");
        huespedes.remove(h);
    }

    public void actualizar(String id, String nombre, String apellido,
                            String telefono, String email, String nacionalidad) throws Exception {
        Huesped h = buscarPorId(id);
        if (h == null) throw new Exception("Huesped no encontrado.");
        if (!telefono.matches("\\d{7,15}"))
            throw new Exception("El telefono debe tener solo digitos (7 a 15).");
        h.setNombre(nombre);
        h.setApellido(apellido);
        h.setTelefono(telefono);
        h.setEmail(email);
        h.setNacionalidad(nacionalidad);
    }

    public Huesped buscarPorId(String id) {
        for (Huesped h : huespedes)
            if (h.getIdentificacion().equalsIgnoreCase(id.trim())) return h;
        return null;
    }

    public ArrayList<Huesped> buscarPorNombre(String texto) {
        ArrayList<Huesped> r = new ArrayList<>();
        for (Huesped h : huespedes)
            if (h.getNombreCompleto().toLowerCase().contains(texto.toLowerCase())) r.add(h);
        return r;
    }

    public ArrayList<Huesped> getHuespedes() { return huespedes; }

    public Object[][] getMatriz() {
        Object[][] d = new Object[huespedes.size()][6];
        for (int i = 0; i < huespedes.size(); i++) {
            Huesped h = huespedes.get(i);
            d[i][0] = h.getIdentificacion();
            d[i][1] = h.getNombre();
            d[i][2] = h.getApellido();
            d[i][3] = h.getTelefono();
            d[i][4] = h.getEmail();
            d[i][5] = h.getNacionalidad();
        }
        return d;
    }

    public String[] getColumnas() {
        return new String[]{"Identificacion", "Nombre", "Apellido", "Telefono", "Email", "Nacionalidad"};
    }
}
