import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class GestorArchivos {

    private static final String SEPARADOR = ";";
    private static final String SEPARADOR_LISTA = "|";

    private File carpeta;

    public GestorArchivos() {
        this.carpeta = new File("datos");
    }

    
    public boolean hayDatosGuardados() {
        return new File(carpeta, "clientes.txt").exists();
    }

    // ===================== GUARDAR =====================

    public void guardar(GestorTurnos gestor) throws IOException {
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        ArrayList<Cliente> clientes = gestor.getClientes();
        ArrayList<Barbero> barberos = gestor.getBarberos();
        ArrayList<Servicio> servicios = gestor.getServicios();

        // Especialidades
        PrintWriter salida = abrirParaEscribir("especialidades.txt");
        for (String e : gestor.getGestorEspecialidad().getEspecialidades()) {
            salida.println(limpiar(e));
        }
        salida.close();

        // Clientes
        salida = abrirParaEscribir("clientes.txt");
        for (Cliente c : clientes) {
            salida.println(limpiar(c.getNombre()) + SEPARADOR + limpiar(c.getApellido()) + SEPARADOR
                + limpiar(c.getTelefono()) + SEPARADOR + limpiar(c.getEmail()));
        }
        salida.close();

        // Barberos
        salida = abrirParaEscribir("barberos.txt");
        for (Barbero b : barberos) {
            ArrayList<String> especialidades = new ArrayList<>();
            for (String e : b.getEspecialidades()) {
                especialidades.add(limpiar(e));
            }
            salida.println(limpiar(b.getNombre()) + SEPARADOR + limpiar(b.getApellido()) + SEPARADOR
                + limpiar(b.getTelefono()) + SEPARADOR + String.join(SEPARADOR_LISTA, especialidades));
        }
        salida.close();

        // Servicios
        salida = abrirParaEscribir("servicios.txt");
        for (Servicio s : servicios) {
            salida.println(limpiar(s.getNombre()) + SEPARADOR + limpiar(s.getDescripcion()) + SEPARADOR
                + s.getPrecio() + SEPARADOR + s.getDuracionMinutos());
        }
        salida.close();

        // Turnos
        salida = abrirParaEscribir("turnos.txt");
        for (Turno t : gestor.getTurnos()) {
            ArrayList<String> indicesServicios = new ArrayList<>();
            for (Servicio s : t.getServicios()) {
                indicesServicios.add(String.valueOf(servicios.indexOf(s)));
            }
            salida.println(t.getFecha() + SEPARADOR + t.getHora() + SEPARADOR + t.getEstado() + SEPARADOR
                + clientes.indexOf(t.getCliente()) + SEPARADOR
                + barberos.indexOf(t.getBarbero()) + SEPARADOR
                + String.join(SEPARADOR_LISTA, indicesServicios) + SEPARADOR
                + limpiar(t.getObservaciones()) + SEPARADOR
                + t.getPrecio());
        }
        salida.close();
    }

    // ===================== CARGAR =====================

    public void cargar(GestorTurnos gestor) throws IOException {
        // Especialidades (las que ya estan en el constructor no se duplican)
        for (String linea : leerLineas("especialidades.txt")) {
            gestor.getGestorEspecialidad().agregarEspecialidad(linea);
        }

        // Clientes
        for (String linea : leerLineas("clientes.txt")) {
            String[] campos = linea.split(SEPARADOR, -1);
            gestor.registrarCliente(new Cliente(campos[0], campos[1], campos[2], campos[3]));
        }

        // Barberos
        for (String linea : leerLineas("barberos.txt")) {
            String[] campos = linea.split(SEPARADOR, -1);
            ArrayList<String> especialidades = new ArrayList<>();
            for (String e : campos[3].split("\\" + SEPARADOR_LISTA)) {
                if (!e.isEmpty()) {
                    especialidades.add(e);
                }
            }
            gestor.registrarBarbero(new Barbero(campos[0], campos[1], campos[2], especialidades));
        }

        // Servicios
        for (String linea : leerLineas("servicios.txt")) {
            String[] campos = linea.split(SEPARADOR, -1);
            double precio = Double.parseDouble(campos[2]);
            int duracion = Integer.parseInt(campos[3]);
            gestor.registrarServicio(new Servicio(campos[0], campos[1], precio, duracion));
        }

        // Turnos
        ArrayList<Cliente> clientes = gestor.getClientes();
        ArrayList<Barbero> barberos = gestor.getBarberos();
        ArrayList<Servicio> servicios = gestor.getServicios();
        for (String linea : leerLineas("turnos.txt")) {
            String[] campos = linea.split(SEPARADOR, -1);
            LocalDate fecha = LocalDate.parse(campos[0]);
            LocalTime hora = LocalTime.parse(campos[1]);
            EstadoTurno estado = EstadoTurno.valueOf(campos[2]);
            Cliente cliente = clientes.get(Integer.parseInt(campos[3]));
            Barbero barbero = barberos.get(Integer.parseInt(campos[4]));

            ArrayList<Servicio> serviciosTurno = new ArrayList<>();
            for (String indice : campos[5].split("\\" + SEPARADOR_LISTA)) {
                serviciosTurno.add(servicios.get(Integer.parseInt(indice)));
            }

            Turno turno = new Turno(fecha, hora, cliente, barbero, serviciosTurno);
            turno.restaurarEstado(estado);
            turno.setObservaciones(campos[6]);
            // Los archivos guardados con la version anterior no tienen el precio:
            // en ese caso queda el calculado con los precios actuales.
            if (campos.length > 7) {
                turno.restaurarPrecio(Double.parseDouble(campos[7]));
            }
            gestor.agregarTurnoCargado(turno);
        }
    }

    

    private PrintWriter abrirParaEscribir(String nombreArchivo) throws IOException {
        return new PrintWriter(new FileWriter(new File(carpeta, nombreArchivo), StandardCharsets.UTF_8));
    }

    
    private ArrayList<String> leerLineas(String nombreArchivo) throws IOException {
        ArrayList<String> lineas = new ArrayList<>();
        File archivo = new File(carpeta, nombreArchivo);
        if (!archivo.exists()) {
            return lineas;
        }
        BufferedReader entrada = new BufferedReader(new FileReader(archivo, StandardCharsets.UTF_8));
        String linea = entrada.readLine();
        while (linea != null) {
            if (!linea.trim().isEmpty()) {
                lineas.add(linea);
            }
            linea = entrada.readLine();
        }
        entrada.close();
        return lineas;
    }

    
    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace(SEPARADOR, ",").replace(SEPARADOR_LISTA, "/")
                    .replace("\n", " ").replace("\r", " ");
    }
}
