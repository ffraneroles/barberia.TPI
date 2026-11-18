public class Servicio {

    private String nombre;
    private String descripcion;
    private double precio;
    private int duracionMinutos;

    public Servicio(String nombre, String descripcion, double precio, int duracionMinutos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public int getDuracionMinutos() { return duracionMinutos; }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
