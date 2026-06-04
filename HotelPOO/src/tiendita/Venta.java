package tiendita;

public class Venta {
    int id;
    String descripcion;
    int cantida;
    double precio;
    double importe;

    public Venta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {   // ✔ MÉTODO CORRECTO
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantida() {
        return cantida;
    }

    public void setCantida(int cantida) {
        this.cantida = cantida;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
}
