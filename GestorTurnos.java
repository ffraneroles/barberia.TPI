import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class GestorTurnos {

    private LocalTime horaApertura;
    private LocalTime horaCierre;
    private int pasoMinutos;
    private DayOfWeek diaCerrado;   

    private ArrayList<Cliente> clientes;
    private ArrayList<Barbero> barberos;
    private ArrayList<Servicio> servicios;
    private ArrayList<Turno> turnos;

    private GestorEspecialidad gestorEspecialidad;

    public GestorTurnos() {
        this.horaApertura = LocalTime.of(9, 0);
        this.horaCierre = LocalTime.of(18, 0);
        this.pasoMinutos = 30;
        this.diaCerrado = DayOfWeek.SUNDAY;
        this.clientes = new ArrayList<>();
        this.barberos = new ArrayList<>();
        this.servicios = new ArrayList<>();
        this.turnos = new ArrayList<>();
        this.gestorEspecialidad = new GestorEspecialidad();
    }

    // ===================== Registro =====================

    public void registrarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void registrarBarbero(Barbero barbero) {
        barberos.add(barbero);
    }

    public void registrarServicio(Servicio servicio) {
        servicios.add(servicio);
    }

    
    // Agrega un turno leido desde el archivo SIN validar fecha ni horario:
    // los turnos guardados de dias anteriores ya estan en el pasado y tienen que cargarse igual.
    // Solo lo usa GestorArchivos.
    public void agregarTurnoCargado(Turno turno) {
        turnos.add(turno);
    }

    // ===================== Getters =====================
   

    public ArrayList<Cliente> getClientes() { return new ArrayList<>(clientes); }
    public ArrayList<Barbero> getBarberos() { return new ArrayList<>(barberos); }
    public ArrayList<Servicio> getServicios() { return new ArrayList<>(servicios); }
    public ArrayList<Turno> getTurnos() { return new ArrayList<>(turnos); }
    public LocalTime getHoraApertura() { return horaApertura; }
    public LocalTime getHoraCierre() { return horaCierre; }
    public GestorEspecialidad getGestorEspecialidad() { return gestorEspecialidad; }


    public boolean modificarEspecialidades(Barbero barbero, ArrayList<String> especialidades) {
        if (!barberos.contains(barbero) || especialidades.isEmpty()) {
            return false;
        }
        barbero.setEspecialidades(especialidades);
        return true;
    }

    // ===================== Validacion de fecha y hora =====================

    
    public String validarFecha(LocalDate fecha) {
        if (fecha.isBefore(LocalDate.now())) {
            return "No se pueden dar turnos en una fecha que ya paso.";
        }
        if (fecha.getDayOfWeek() == diaCerrado) {
            return "La barberia no atiende los domingos.";
        }
        return null;
    }

    
    public String validarFechaHora(LocalDate fecha, LocalTime hora, int duracionMinutos) {
        String error = validarFecha(fecha);
        if (error != null) {
            return error;
        }
        if (fecha.equals(LocalDate.now()) && hora.isBefore(LocalTime.now())) {
            return "Esa hora de hoy ya paso.";
        }
        LocalTime fin = hora.plusMinutes(duracionMinutos);
        if (!dentroDelHorario(hora, fin)) {
            return "El turno (" + hora + " a " + fin + ") queda fuera del horario de atencion ("
                + horaApertura + " a " + horaCierre + ").";
        }
        return null;
    }

    // ===================== Consultas =====================

    public ArrayList<Turno> consultarTurnosPorFecha(LocalDate fecha) {
        ArrayList<Turno> resultado = new ArrayList<>();
        for (Turno t : turnos) {
            if (t.getFecha().equals(fecha)) {
                resultado.add(t);
            }
        }

        for (int i = 0; i < resultado.size() - 1; i++) {
            for (int j = 0; j < resultado.size() - 1 - i; j++) {
                if (resultado.get(j).getHora().isAfter(resultado.get(j + 1).getHora())) {
                    Turno auxiliar = resultado.get(j);
                    resultado.set(j, resultado.get(j + 1));
                    resultado.set(j + 1, auxiliar);
                }
            }
        }
        return resultado;
    }

    public ArrayList<LocalTime> consultarDisponibilidad(Barbero barbero, LocalDate fecha,
                                                       ArrayList<Servicio> serviciosTurno) {
        ArrayList<LocalTime> libres = new ArrayList<>();
        int duracion = Turno.calcularDuracionTotal(serviciosTurno);
        LocalTime inicio = horaApertura;
        while (inicio.isBefore(horaCierre)) {
            LocalTime fin = inicio.plusMinutes(duracion);
            
            if (validarFechaHora(fecha, inicio, duracion) == null
                    && estaLibre(barbero, fecha, inicio, fin, null)) {
                libres.add(inicio);
            }
            inicio = inicio.plusMinutes(pasoMinutos);
        }
        return libres;
    }

    // ===================== Reporte de recaudacion =====================

    
    public ArrayList<Turno> turnosPorEstado(LocalDate fecha, EstadoTurno estado) {
        ArrayList<Turno> resultado = new ArrayList<>();
        for (Turno t : consultarTurnosPorFecha(fecha)) {
            if (t.getEstado() == estado) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    
    public double recaudacionDelDia(LocalDate fecha) {
        double total = 0;
        for (Turno t : turnosPorEstado(fecha, EstadoTurno.REALIZADO)) {
            total = total + t.getPrecio();
        }
        return total;
    }

    
    public double montoPendienteDelDia(LocalDate fecha) {
        double total = 0;
        for (Turno t : consultarTurnosPorFecha(fecha)) {
            if (t.estaActivo()) {
                total = total + t.getPrecio();
            }
        }
        return total;
    }

    // ===================== Turnos =====================

    public boolean crearTurno(Cliente cliente, Barbero barbero, ArrayList<Servicio> serviciosTurno,
                              LocalDate fecha, LocalTime hora) {
        return crearTurno(cliente, barbero, serviciosTurno, fecha, hora, "");
    }

    public boolean crearTurno(Cliente cliente, Barbero barbero, ArrayList<Servicio> serviciosTurno,
                              LocalDate fecha, LocalTime hora, String observaciones) {
        if (!clientes.contains(cliente) || !barberos.contains(barbero)
                || !serviciosValidos(serviciosTurno)) {
            return false;
        }

        int duracion = Turno.calcularDuracionTotal(serviciosTurno);
        if (validarFechaHora(fecha, hora, duracion) != null) {
            return false;
        }
        if (!estaLibre(barbero, fecha, hora, hora.plusMinutes(duracion), null)) {
            return false;
        }

        Turno nuevo = new Turno(fecha, hora, cliente, barbero, serviciosTurno);
        nuevo.setObservaciones(observaciones);
        turnos.add(nuevo);
        return true;
    }

    public boolean cancelarTurno(Turno turno) {
        return turno.cancelar();
    }

    public boolean cambiarEstado(Turno turno, EstadoTurno nuevoEstado) {
        return turno.cambiarEstado(nuevoEstado);
    }

    public boolean modificarTurno(Turno turno, LocalDate fecha, LocalTime hora,
                                  Barbero barbero, ArrayList<Servicio> serviciosTurno) {
        if (!turnos.contains(turno) || !turno.estaActivo()) {
            return false;
        }
        if (!barberos.contains(barbero) || !serviciosValidos(serviciosTurno)) {
            return false;
        }

        int duracion = Turno.calcularDuracionTotal(serviciosTurno);
        if (validarFechaHora(fecha, hora, duracion) != null) {
            return false;
        }
        if (!estaLibre(barbero, fecha, hora, hora.plusMinutes(duracion), turno)) {
            return false;
        }

        turno.modificar(fecha, hora, barbero, serviciosTurno);
        return true;
    }

    // ===================== Metodos auxiliares =====================

    
    private boolean serviciosValidos(ArrayList<Servicio> serviciosTurno) {
        if (serviciosTurno == null || serviciosTurno.isEmpty()) {
            return false;
        }
        for (Servicio s : serviciosTurno) {
            if (!servicios.contains(s)) {
                return false;
            }
        }
        return true;
    }

    private boolean dentroDelHorario(LocalTime inicio, LocalTime fin) {
        return !inicio.isBefore(horaApertura)
            && fin.isAfter(inicio)
            && !fin.isAfter(horaCierre);
    }

    private boolean estaLibre(Barbero barbero, LocalDate fecha, LocalTime inicio,
                              LocalTime fin, Turno ignorar) {
        for (Turno t : turnos) {
            if (t != ignorar && t.ocupa(barbero, fecha, inicio, fin)) {
                return false;
            }
        }
        return true;
    }
}
