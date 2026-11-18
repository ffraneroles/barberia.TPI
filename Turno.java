import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Turno {

    private LocalDate fecha;
    private LocalTime hora;
    private EstadoTurno estado;
    private String observaciones;
    private Cliente cliente;
    private Barbero barbero;
    private ArrayList<Servicio> servicios;
    // Precio que se cobra, fijado al crear (o modificar) el turno.
    // Asi, si despues cambia el precio de un servicio, los turnos ya dados no cambian.
    private double precioCobrado;

    public Turno(LocalDate fecha, LocalTime hora, Cliente cliente, Barbero barbero,
                 ArrayList<Servicio> servicios) {
        this.fecha = fecha;
        this.hora = hora;
        this.cliente = cliente;
        this.barbero = barbero;
        this.servicios = new ArrayList<>(servicios);
        this.precioCobrado = calcularPrecioTotal(servicios);
        this.estado = EstadoTurno.PENDIENTE;
        this.observaciones = "";
    }

    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public EstadoTurno getEstado() { return estado; }
    public String getObservaciones() { return observaciones; }
    public Cliente getCliente() { return cliente; }
    public Barbero getBarbero() { return barbero; }
    public ArrayList<Servicio> getServicios() { return new ArrayList<>(servicios); }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public double getPrecio() {
        return precioCobrado;
    }

    
    public int getDuracionMinutos() {
        return calcularDuracionTotal(servicios);
    }

    public LocalTime getHoraFin() {
        return hora.plusMinutes(getDuracionMinutos());
    }

    
    public static double calcularPrecioTotal(ArrayList<Servicio> lista) {
        double total = 0;
        for (Servicio s : lista) {
            total = total + s.getPrecio();
        }
        return total;
    }

    public static int calcularDuracionTotal(ArrayList<Servicio> lista) {
        int total = 0;
        for (Servicio s : lista) {
            total = total + s.getDuracionMinutos();
        }
        return total;
    }

    public static String nombresDe(ArrayList<Servicio> lista) {
        ArrayList<String> nombres = new ArrayList<>();
        for (Servicio s : lista) {
            nombres.add(s.getNombre());
        }
        return String.join(" + ", nombres);
    }

    public String serviciosComoTexto() {
        return nombresDe(servicios);
    }

    
    public void restaurarEstado(EstadoTurno estadoGuardado) {
        this.estado = estadoGuardado;
    }

    // Igual que restaurarEstado: solo lo usa GestorArchivos al cargar los turnos guardados.
    public void restaurarPrecio(double precioGuardado) {
        this.precioCobrado = precioGuardado;
    }

    public boolean estaActivo() {
        return estado == EstadoTurno.PENDIENTE || estado == EstadoTurno.CONFIRMADO;
    }

    public boolean cambiarEstado(EstadoTurno nuevo) {
        boolean valido =
            (estado == EstadoTurno.PENDIENTE
                && (nuevo == EstadoTurno.CONFIRMADO || nuevo == EstadoTurno.CANCELADO))
            || (estado == EstadoTurno.CONFIRMADO
                && (nuevo == EstadoTurno.REALIZADO || nuevo == EstadoTurno.CANCELADO));
        if (valido) {
            this.estado = nuevo;
        }
        return valido;
    }

    public boolean cancelar() {
        return cambiarEstado(EstadoTurno.CANCELADO);
    }

    public void modificar(LocalDate fecha, LocalTime hora, Barbero barbero,
                          ArrayList<Servicio> servicios) {
        this.fecha = fecha;
        this.hora = hora;
        this.barbero = barbero;
        this.servicios = new ArrayList<>(servicios);
        this.precioCobrado = calcularPrecioTotal(servicios);   // el turno cambio: se recalcula
    }

    public boolean ocupa(Barbero otroBarbero, LocalDate otraFecha, LocalTime inicio, LocalTime fin) {
        if (estado == EstadoTurno.CANCELADO) {
            return false;
        }
        if (this.barbero != otroBarbero) {
            return false;
        }
        if (!this.fecha.equals(otraFecha)) {
            return false;
        }
        return this.hora.isBefore(fin) && inicio.isBefore(this.getHoraFin());
    }

    public boolean seSuperponeCon(Turno otro) {
        if (otro.estado == EstadoTurno.CANCELADO) {
            return false;
        }
        return ocupa(otro.barbero, otro.fecha, otro.hora, otro.getHoraFin());
    }

    public String resumen() {
        return fecha + " " + hora + "-" + getHoraFin()
            + " | " + cliente.nombreCompleto()
            + " | Barbero: " + barbero.nombreCompleto()
            + " | " + serviciosComoTexto()
            + " (" + String.format("$%.0f", getPrecio()) + ")"
            + " | " + estado;
    }
}
