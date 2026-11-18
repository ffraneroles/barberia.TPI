import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private GestorTurnos gestor;
    private GestorArchivos archivos;
    private Scanner teclado;

    public Main() {
        this.gestor = new GestorTurnos();
        this.archivos = new GestorArchivos();
        this.teclado = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.ejecutar();
    }

    public void ejecutar() {
        cargarDatos();

        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            opcion = leerEntero("Opcion: ");
            System.out.println();

            switch (opcion) {
                case 1:  menuBarbero(); break;
                case 2:  menuServicio(); break;
                case 3:  menuTurno(); break;
                case 4:  menuCliente(); break;
                case 5:  menuReportes(); break;
                case 0:  System.out.println("Hasta luego."); break;
                default: System.out.println("Opcion invalida.");
            }
        }
        teclado.close();
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("===== BARBERIA - GESTION DE TURNOS =====");
        System.out.println(" 1. Barbero");
        System.out.println(" 2. Servicio");
        System.out.println(" 3. Turno");
        System.out.println(" 4. Cliente");
        System.out.println(" 5. Reportes");
        System.out.println(" 0. Salir");
    }

    // ===================== Submenus =====================

    private void menuBarbero() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println();
            System.out.println("----- BARBERO -----");
            System.out.println(" 1. Registrar barbero");
            System.out.println(" 2. Consultar barberos");
            System.out.println(" 3. Modificar especialidades de un barbero");
            System.out.println(" 0. Volver");
            opcion = leerEntero("Opcion: ");
            System.out.println();

            switch (opcion) {
                case 1:  registrarBarbero(); break;
                case 2:  listarBarberos(); break;
                case 3:  modificarEspecialidadesBarbero(); break;
                case 0:  break;
                default: System.out.println("Opcion invalida.");
            }
            guardarDatos();
        }
    }

    private void menuServicio() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println();
            System.out.println("----- SERVICIO -----");
            System.out.println(" 1. Registrar servicio");
            System.out.println(" 2. Consultar servicios");
            System.out.println(" 3. Modificar precio de un servicio");
            System.out.println(" 0. Volver");
            opcion = leerEntero("Opcion: ");
            System.out.println();

            switch (opcion) {
                case 1:  registrarServicio(); break;
                case 2:  listarServicios(); break;
                case 3:  modificarPrecioServicio(); break;
                case 0:  break;
                default: System.out.println("Opcion invalida.");
            }
            guardarDatos();
        }
    }

    private void menuTurno() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println();
            System.out.println("----- TURNO -----");
            System.out.println(" 1. Crear turno");
            System.out.println(" 2. Consultar turnos de una fecha");
            System.out.println(" 3. Consultar disponibilidad de un barbero");
            System.out.println(" 4. Modificar turno");
            System.out.println(" 5. Cancelar turno");
            System.out.println(" 6. Cambiar estado de un turno");
            System.out.println(" 0. Volver");
            opcion = leerEntero("Opcion: ");
            System.out.println();

            switch (opcion) {
                case 1:  crearTurno(); break;
                case 2:  consultarTurnosPorFecha(); break;
                case 3:  consultarDisponibilidad(); break;
                case 4:  modificarTurno(); break;
                case 5:  cancelarTurno(); break;
                case 6:  cambiarEstadoTurno(); break;
                case 0:  break;
                default: System.out.println("Opcion invalida.");
            }
            guardarDatos();
        }
    }

    private void menuCliente() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println();
            System.out.println("----- CLIENTE -----");
            System.out.println(" 1. Registrar cliente");
            System.out.println(" 2. Consultar clientes");
            System.out.println(" 0. Volver");
            opcion = leerEntero("Opcion: ");
            System.out.println();

            switch (opcion) {
                case 1:  registrarCliente(); break;
                case 2:  listarClientes(); break;
                case 0:  break;
                default: System.out.println("Opcion invalida.");
            }
            guardarDatos();
        }
    }

    private void menuReportes() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println();
            System.out.println("----- REPORTES -----");
            System.out.println(" 1. Recaudacion del dia");
            System.out.println(" 0. Volver");
            opcion = leerEntero("Opcion: ");
            System.out.println();

            switch (opcion) {
                case 1:  reporteRecaudacion(); break;
                case 0:  break;
                default: System.out.println("Opcion invalida.");
            }
        }
    }

    // ===================== Archivos =====================

    private void cargarDatos() {
        if (archivos.hayDatosGuardados()) {
            try {
                archivos.cargar(gestor);
                System.out.println("(Datos cargados desde la carpeta 'datos': "
                    + gestor.getClientes().size() + " clientes, "
                    + gestor.getBarberos().size() + " barberos, "
                    + gestor.getServicios().size() + " servicios y "
                    + gestor.getTurnos().size() + " turnos.)");
                return;
            } catch (Exception e) {
                System.out.println("No se pudieron leer los datos guardados (" + e.getMessage()
                    + "). Se cargan los datos de ejemplo.");
                gestor = new GestorTurnos();   
            }
        }
        cargarDatosDeEjemplo();
    }

    private void guardarDatos() {
        try {
            archivos.guardar(gestor);
        } catch (IOException e) {
            System.out.println("Atencion: no se pudieron guardar los datos (" + e.getMessage() + ").");
        }
    }

    // ===================== Clientes, barberos y servicios =====================

    private void registrarCliente() {
        String nombre = leerTextoObligatorio("Nombre: ");
        String apellido = leerTextoObligatorio("Apellido: ");
        String telefono = leerTelefono();
        String email = leerEmail();
        gestor.registrarCliente(new Cliente(nombre, apellido, telefono, email));
        System.out.println("Cliente registrado.");
    }

    private void listarClientes() {
        ArrayList<Cliente> clientes = gestor.getClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        }
        for (int i = 0; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            System.out.println((i + 1) + ". " + c.nombreCompleto()
                + " | Tel: " + c.getTelefono() + " | " + c.getEmail());
        }
    }

    private void registrarBarbero() {
        String nombre = leerTextoObligatorio("Nombre: ");
        String apellido = leerTextoObligatorio("Apellido: ");
        String telefono = leerTelefono();
        System.out.println("Seleccione las especialidades del barbero:");
        ArrayList<String> especialidades = elegirEspecialidades(new ArrayList<String>());
        gestor.registrarBarbero(new Barbero(nombre, apellido, telefono, especialidades));
        System.out.println("Barbero registrado con: " + String.join(", ", especialidades) + ".");
    }

    private void modificarEspecialidadesBarbero() {
        Barbero barbero = elegirBarbero();
        if (barbero == null) {
            return;
        }
        System.out.println("Especialidades actuales: " + barbero.especialidadesComoTexto());
        ArrayList<String> nuevas = elegirEspecialidades(barbero.getEspecialidades());
        if (gestor.modificarEspecialidades(barbero, nuevas)) {
            System.out.println("Especialidades actualizadas: " + barbero.especialidadesComoTexto());
        } else {
            System.out.println("No se pudieron actualizar las especialidades.");
        }
    }

    
    private ArrayList<String> elegirEspecialidades(ArrayList<String> actuales) {
        ArrayList<String> elegidas = new ArrayList<>(actuales);
        GestorEspecialidad catalogo = gestor.getGestorEspecialidad();
        boolean terminar = false;

        while (!terminar) {
            ArrayList<String> disponibles = catalogo.getEspecialidades();
            System.out.println();
            System.out.println("--- ESPECIALIDADES (elija un numero para marcar/desmarcar) ---");
            for (int i = 0; i < disponibles.size(); i++) {
                String marca = "[ ]";
                if (elegidas.contains(disponibles.get(i))) {
                    marca = "[X]";
                }
                System.out.println(String.format("%2d. %s %s", i + 1, marca, disponibles.get(i)));
            }
            int opcionNueva = disponibles.size() + 1;
            System.out.println(String.format("%2d. Agregar una especialidad nueva", opcionNueva));
            System.out.println(" 0. Terminar");

            int opcion = leerEntero("Opcion: ");
            if (opcion == 0) {
                if (elegidas.isEmpty()) {
                    System.out.println("Debe elegir al menos una especialidad.");
                } else {
                    terminar = true;
                }
            } else if (opcion >= 1 && opcion <= disponibles.size()) {
                String especialidad = disponibles.get(opcion - 1);
                if (elegidas.contains(especialidad)) {
                    elegidas.remove(especialidad);
                    System.out.println("Se quito: " + especialidad);
                } else {
                    elegidas.add(especialidad);
                    System.out.println("Se agrego: " + especialidad);
                }
            } else if (opcion == opcionNueva) {
                String nueva = leerTextoObligatorio("Nombre de la nueva especialidad: ");
                if (catalogo.agregarEspecialidad(nueva)) {
                    elegidas.add(nueva);
                    System.out.println("Especialidad creada y asignada: " + nueva);
                } else {
                    System.out.println("No se pudo agregar: ya existe en la lista.");
                }
            } else {
                System.out.println("Opcion invalida.");
            }
        }
        return elegidas;
    }

    private void listarBarberos() {
        ArrayList<Barbero> barberos = gestor.getBarberos();
        if (barberos.isEmpty()) {
            System.out.println("No hay barberos registrados.");
        }
        for (int i = 0; i < barberos.size(); i++) {
            Barbero b = barberos.get(i);
            System.out.println((i + 1) + ". " + b.nombreCompleto()
                + " | Tel: " + b.getTelefono() + " | " + b.especialidadesComoTexto());
        }
    }

    private void registrarServicio() {
        String nombre = leerTextoObligatorio("Nombre: ");
        String descripcion = leerTexto("Descripcion (puede quedar vacia): ");

        int precio = leerEntero("Precio en pesos (sin centavos): ");
        while (precio < 0) {
            System.out.println("El precio no puede ser negativo.");
            precio = leerEntero("Precio en pesos (sin centavos): ");
        }
        int duracion = leerEntero("Duracion en minutos: ");
        while (duracion <= 0) {
            System.out.println("La duracion tiene que ser mayor a 0.");
            duracion = leerEntero("Duracion en minutos: ");
        }

        gestor.registrarServicio(new Servicio(nombre, descripcion, precio, duracion));
        System.out.println("Servicio registrado.");
    }

    private void listarServicios() {
        ArrayList<Servicio> servicios = gestor.getServicios();
        if (servicios.isEmpty()) {
            System.out.println("No hay servicios registrados.");
        }
        for (int i = 0; i < servicios.size(); i++) {
            Servicio s = servicios.get(i);
            System.out.println((i + 1) + ". " + s.getNombre() + " - " + s.getDescripcion()
                + " | " + formatoPesos(s.getPrecio()) + " | " + s.getDuracionMinutos() + " min");
        }
    }

    private void modificarPrecioServicio() {
        ArrayList<Servicio> servicios = gestor.getServicios();
        if (servicios.isEmpty()) {
            System.out.println("No hay servicios registrados.");
            return;
        }
        listarServicios();
        Servicio servicio = servicios.get(leerIndice("Numero de servicio: ", servicios.size()));

        int precio = leerEntero("Nuevo precio en pesos (sin centavos): ");
        while (precio < 0) {
            System.out.println("El precio no puede ser negativo.");
            precio = leerEntero("Nuevo precio en pesos (sin centavos): ");
        }
        double anterior = servicio.getPrecio();
        servicio.setPrecio(precio);
        System.out.println("Precio de " + servicio.getNombre() + " actualizado: "
            + formatoPesos(anterior) + " -> " + formatoPesos(servicio.getPrecio()));
        System.out.println("(Los turnos que ya estaban dados mantienen el precio anterior.)");
    }

    // ===================== Turnos =====================

    private void crearTurno() {
        Cliente cliente = elegirCliente();
        Barbero barbero = elegirBarbero();
        ArrayList<Servicio> servicios = elegirServicios(new ArrayList<Servicio>());
        if (cliente == null || barbero == null || servicios.isEmpty()) {
            System.out.println("Primero tiene que haber al menos un cliente, un barbero y un servicio.");
            return;
        }

        int duracion = Turno.calcularDuracionTotal(servicios);
        LocalDate fecha = leerFechaParaTurno();
        LocalTime hora = leerHoraParaTurno(fecha, duracion);
        String observaciones = leerTexto("Observaciones (puede quedar vacio): ");

        if (gestor.crearTurno(cliente, barbero, servicios, fecha, hora, observaciones)) {
            System.out.println("Turno creado en estado PENDIENTE. Total: "
                + formatoPesos(Turno.calcularPrecioTotal(servicios)) + " | " + duracion + " min.");
        } else {
            System.out.println("No se pudo crear el turno: " + barbero.nombreCompleto()
                + " ya tiene un turno en ese horario. Puede ver los horarios libres en Turno > Consultar disponibilidad.");
        }
    }

    private void consultarTurnosPorFecha() {
        LocalDate fecha = leerFecha();
        ArrayList<Turno> turnos = gestor.consultarTurnosPorFecha(fecha);
        if (turnos.isEmpty()) {
            System.out.println("No hay turnos para esa fecha.");
        }
        for (Turno t : turnos) {
            System.out.println(t.resumen());
        }
    }

    private void consultarDisponibilidad() {
        Barbero barbero = elegirBarbero();
        ArrayList<Servicio> servicios = elegirServicios(new ArrayList<Servicio>());
        if (barbero == null || servicios.isEmpty()) {
            System.out.println("Primero tiene que haber al menos un barbero y un servicio.");
            return;
        }

        LocalDate fecha = leerFechaParaTurno();
        ArrayList<LocalTime> libres = gestor.consultarDisponibilidad(barbero, fecha, servicios);
        if (libres.isEmpty()) {
            System.out.println("No hay horarios disponibles ese dia.");
        } else {
            System.out.println("Horarios disponibles para " + Turno.nombresDe(servicios)
                + " (" + Turno.calcularDuracionTotal(servicios) + " min)"
                + " con " + barbero.nombreCompleto() + ":");
            for (LocalTime h : libres) {
                System.out.print(h + "  ");
            }
            System.out.println();
        }
    }

    private void modificarTurno() {
        Turno turno = elegirTurno();
        if (turno == null) {
            return;
        }
        if (!turno.estaActivo()) {
            System.out.println("Solo se pueden modificar turnos PENDIENTES o CONFIRMADOS.");
            return;
        }

        System.out.println("Ingrese los nuevos datos del turno.");
        Barbero barbero = elegirBarbero();
        System.out.println("Servicios del turno (ya aparecen marcados los actuales):");
        ArrayList<Servicio> servicios = elegirServicios(turno.getServicios());
        int duracion = Turno.calcularDuracionTotal(servicios);
        LocalDate fecha = leerFechaParaTurno();
        LocalTime hora = leerHoraParaTurno(fecha, duracion);

        if (gestor.modificarTurno(turno, fecha, hora, barbero, servicios)) {
            System.out.println("Turno modificado: " + turno.resumen());
        } else {
            System.out.println("No se pudo modificar: " + barbero.nombreCompleto()
                + " ya tiene un turno en ese horario. El turno queda como estaba.");
        }
    }

    private void cancelarTurno() {
        Turno turno = elegirTurno();
        if (turno != null) {
            if (gestor.cancelarTurno(turno)) {
                System.out.println("Turno cancelado.");
            } else {
                System.out.println("No se puede cancelar un turno que ya esta " + turno.getEstado() + ".");
            }
        }
    }

    private void cambiarEstadoTurno() {
        Turno turno = elegirTurno();
        if (turno == null) {
            return;
        }
        System.out.println("Nuevo estado: 1. CONFIRMADO  2. REALIZADO  3. CANCELADO");
        int opcion = leerEntero("Opcion: ");

        EstadoTurno nuevo = null;
        switch (opcion) {
            case 1: nuevo = EstadoTurno.CONFIRMADO; break;
            case 2: nuevo = EstadoTurno.REALIZADO; break;
            case 3: nuevo = EstadoTurno.CANCELADO; break;
            default: break;
        }

        if (nuevo == null) {
            System.out.println("Opcion invalida.");
        } else if (gestor.cambiarEstado(turno, nuevo)) {
            System.out.println("Estado actualizado: " + turno.resumen());
        } else {
            System.out.println("Cambio no permitido desde " + turno.getEstado() + " a " + nuevo
                + ". Orden valido: PENDIENTE -> CONFIRMADO -> REALIZADO (o CANCELADO).");
        }
    }

    // ===================== Reporte =====================

    private void reporteRecaudacion() {
        LocalDate fecha = leerFecha();
        ArrayList<Turno> realizados = gestor.turnosPorEstado(fecha, EstadoTurno.REALIZADO);

        System.out.println();
        System.out.println("===== RECAUDACION DEL " + fecha + " =====");
        if (realizados.isEmpty()) {
            System.out.println("No hay turnos realizados en esa fecha.");
        }
        for (Turno t : realizados) {
            System.out.println(t.getHora() + " | " + t.getCliente().nombreCompleto()
                + " | " + t.getBarbero().nombreCompleto()
                + " | " + t.serviciosComoTexto() + " | " + formatoPesos(t.getPrecio()));
        }
        System.out.println("----------------------------------------");
        System.out.println("Turnos realizados:  " + realizados.size());
        System.out.println("Total recaudado:    " + formatoPesos(gestor.recaudacionDelDia(fecha)));
        System.out.println("Pendiente de cobro: " + formatoPesos(gestor.montoPendienteDelDia(fecha))
            + " (turnos pendientes y confirmados)");
        System.out.println("Turnos cancelados:  "
            + gestor.turnosPorEstado(fecha, EstadoTurno.CANCELADO).size());
    }

    // ===================== Elegir de una lista =====================

    private Cliente elegirCliente() {
        listarClientes();
        ArrayList<Cliente> lista = gestor.getClientes();
        if (lista.isEmpty()) {
            return null;
        }
        return lista.get(leerIndice("Numero de cliente: ", lista.size()));
    }

    private Barbero elegirBarbero() {
        listarBarberos();
        ArrayList<Barbero> lista = gestor.getBarberos();
        if (lista.isEmpty()) {
            return null;
        }
        return lista.get(leerIndice("Numero de barbero: ", lista.size()));
    }

   
    private ArrayList<Servicio> elegirServicios(ArrayList<Servicio> actuales) {
        ArrayList<Servicio> disponibles = gestor.getServicios();
        ArrayList<Servicio> elegidos = new ArrayList<>(actuales);
        if (disponibles.isEmpty()) {
            System.out.println("No hay servicios registrados.");
            return elegidos;
        }

        boolean terminar = false;
        while (!terminar) {
            System.out.println();
            System.out.println("--- SERVICIOS (elija un numero para marcar/desmarcar) ---");
            for (int i = 0; i < disponibles.size(); i++) {
                Servicio s = disponibles.get(i);
                String marca = "[ ]";
                if (elegidos.contains(s)) {
                    marca = "[X]";
                }
                System.out.println(String.format("%2d. %s %-15s %-9s %d min",
                    i + 1, marca, s.getNombre(), formatoPesos(s.getPrecio()), s.getDuracionMinutos()));
            }
            System.out.println(" 0. Terminar");
            System.out.println("Total: " + formatoPesos(Turno.calcularPrecioTotal(elegidos))
                + " | " + Turno.calcularDuracionTotal(elegidos) + " min");

            int opcion = leerEntero("Opcion: ");
            if (opcion == 0) {
                if (elegidos.isEmpty()) {
                    System.out.println("Debe elegir al menos un servicio.");
                } else {
                    terminar = true;
                }
            } else if (opcion >= 1 && opcion <= disponibles.size()) {
                Servicio s = disponibles.get(opcion - 1);
                if (elegidos.contains(s)) {
                    elegidos.remove(s);
                    System.out.println("Se quito: " + s.getNombre());
                } else {
                    elegidos.add(s);
                    System.out.println("Se agrego: " + s.getNombre());
                }
            } else {
                System.out.println("Opcion invalida.");
            }
        }
        return elegidos;
    }

    private Turno elegirTurno() {
        ArrayList<Turno> lista = gestor.getTurnos();
        if (lista.isEmpty()) {
            System.out.println("No hay turnos registrados.");
            return null;
        }
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + ". " + lista.get(i).resumen());
        }
        return lista.get(leerIndice("Numero de turno: ", lista.size()));
    }

    // ===================== Lectura y validacion de datos =====================

    private String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = teclado.nextLine().trim();
            // ";" y "|" se usan como separadores en los archivos de datos
            if (texto.contains(";") || texto.contains("|")) {
                System.out.println("No se pueden usar los caracteres ; ni |");
            } else {
                return texto;
            }
        }
    }

    
    private String leerTextoObligatorio(String mensaje) {
        String texto = leerTexto(mensaje);
        while (texto.isEmpty()) {
            System.out.println("Este campo no puede quedar vacio.");
            texto = leerTexto(mensaje);
        }
        return texto;
    }

    private String leerTelefono() {
        String telefono = leerTexto("Telefono (solo numeros): ");
        while (!telefonoValido(telefono)) {
            System.out.println("Telefono invalido: tiene que tener entre 7 y 15 numeros, sin espacios ni guiones.");
            telefono = leerTexto("Telefono (solo numeros): ");
        }
        return telefono;
    }

    private boolean telefonoValido(String telefono) {
        return telefono.matches("\\d{7,15}");
    }

    private String leerEmail() {
        String email = leerTexto("Email: ");
        while (!emailValido(email)) {
            System.out.println("Email invalido. Ejemplo valido: nombre@mail.com");
            email = leerTexto("Email: ");
        }
        return email;
    }

    private boolean emailValido(String email) {
        if (email.contains(" ")) {
            return false;
        }
        int arroba = email.indexOf("@");
        if (arroba <= 0 || arroba != email.lastIndexOf("@")) {
            return false;   
        }
        int punto = email.lastIndexOf(".");
        return punto > arroba + 1 && punto < email.length() - 1;
    }

    private int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!teclado.hasNextInt()) {
            teclado.next();
            System.out.print("Ingrese un numero entero: ");
        }
        int numero = teclado.nextInt();
        teclado.nextLine();
        return numero;
    }

    private int leerIndice(String mensaje, int cantidad) {
        int numero = leerEntero(mensaje);
        while (numero < 1 || numero > cantidad) {
            System.out.println("Ingrese un numero entre 1 y " + cantidad + ".");
            numero = leerEntero(mensaje);
        }
        return numero - 1;
    }

    
    private LocalDate leerFecha() {
        while (true) {
            int dia = leerEntero("Dia: ");
            int mes = leerEntero("Mes: ");
            int anio = leerEntero("Anio: ");
            if (anio >= 2000 && anio <= 2100) {
                try {
                    return LocalDate.of(anio, mes, dia);
                } catch (DateTimeException e) {
                    
                }
            }
            System.out.println("Fecha invalida, intente de nuevo.");
        }
    }

    private LocalDate leerFechaParaTurno() {
        LocalDate fecha = leerFecha();
        String error = gestor.validarFecha(fecha);
        while (error != null) {
            System.out.println(error + " Ingrese otra fecha.");
            fecha = leerFecha();
            error = gestor.validarFecha(fecha);
        }
        return fecha;
    }

    private LocalTime leerHora() {
        while (true) {
            int hora = leerEntero("Hora (0 a 23): ");
            int minutos = leerEntero("Minutos (0 a 59): ");
            if (hora >= 0 && hora <= 23 && minutos >= 0 && minutos <= 59) {
                return LocalTime.of(hora, minutos);
            }
            System.out.println("Hora invalida, intente de nuevo.");
        }
    }

    private LocalTime leerHoraParaTurno(LocalDate fecha, int duracion) {
        LocalTime hora = leerHora();
        String error = gestor.validarFechaHora(fecha, hora, duracion);
        while (error != null) {
            System.out.println(error + " Ingrese otra hora.");
            hora = leerHora();
            error = gestor.validarFechaHora(fecha, hora, duracion);
        }
        return hora;
    }

    private String formatoPesos(double importe) {
        return String.format("$%.0f", importe);
    }

    // ===================== Datos de ejemplo =====================

    private void cargarDatosDeEjemplo() {
        gestor.registrarCliente(new Cliente("Nicolas", "Vasone", "3534112233", "nicolas.vasone@mail.com"));
        gestor.registrarCliente(new Cliente("Luca", "Oyola", "3534998877", "luca.oyola@mail.com"));

        ArrayList<String> especialidadesFran = new ArrayList<>();
        especialidadesFran.add("Corte de cabello");
        especialidadesFran.add("Coloraciones");
        gestor.registrarBarbero(new Barbero("Fran", "Eroles", "3534445566", especialidadesFran));

        ArrayList<String> especialidadesEmi = new ArrayList<>();
        especialidadesEmi.add("Perfilado de cejas");
        especialidadesEmi.add("Arreglo de barba");
        gestor.registrarBarbero(new Barbero("Emi", "Trejo", "3534776655", especialidadesEmi));

        gestor.registrarServicio(new Servicio("Corte", "Corte de cabello", 8000, 30));
        gestor.registrarServicio(new Servicio("Barba", "Arreglo de barba", 5000, 30));
        gestor.registrarServicio(new Servicio("Cejas", "Arreglo de cejas", 3000, 15));
        System.out.println("(Primera ejecucion: se cargaron datos de ejemplo: 2 clientes, 2 barberos y 3 servicios.)");
    }
}