# Barbería al Día

**Turnos al día, caja al día.**

Sistema de gestión de turnos para barberías, desarrollado en Java con programación orientada a objetos.
Trabajo práctico integrador de la Tecnicatura Universitaria en Programación – UTN Facultad Regional Villa María.

## El problema

Muchas barberías manejan los turnos en un cuaderno o por WhatsApp. Eso genera turnos superpuestos,
clientes que esperan de más y un dueño que, al final del día, no sabe con certeza cuánto recaudó.

## Funcionalidades

- **Barberos** con una o varias especialidades, elegidas desde un menú.
- **Servicios** con precio y duración; el precio se puede actualizar.
- **Turnos** con uno o varios servicios: el precio y la duración se calculan solos.
- **Control de turnos:** no permite turnos superpuestos para el mismo barbero, fechas pasadas,
  domingos ni horarios fuera de la atención (9 a 18 hs).
- **Consulta de disponibilidad** de cada barbero.
- **Estados del turno:** pendiente → confirmado → realizado, o cancelado.
- **Reporte de recaudación del día:** total cobrado, pendiente de cobro y turnos cancelados.
- **Precio fijo por turno:** si sube el precio de un servicio, lo ya cobrado no cambia.
- **Guardado automático** en archivos de texto después de cada operación.
- **Validación de datos:** campos obligatorios, teléfono numérico y email con formato válido.

## Cómo ejecutarlo

Requiere Java 11 o superior.

    javac *.java
    java Main

La primera vez se cargan datos de ejemplo. Los datos se guardan en una carpeta `datos/`
que se crea sola al lado del programa.

## Estructura del código

| Clase | Responsabilidad |
|---|---|
| `Main` | Menús de consola y lectura de datos |
| `GestorTurnos` | Reglas del negocio: registro, turnos, validaciones y reportes |
| `GestorEspecialidad` | Catálogo de especialidades de los barberos |
| `GestorArchivos` | Guardar y cargar los datos en archivos de texto |
| `Persona` | Clase abstracta con nombre, apellido y teléfono |
| `Cliente`, `Barbero` | Heredan de `Persona` |
| `Servicio` | Nombre, descripción, precio y duración |
| `Turno` | Fecha, hora, cliente, barbero, servicios, estado y precio cobrado |
| `EstadoTurno` | Enum: PENDIENTE, CONFIRMADO, REALIZADO, CANCELADO |

## Próximas versiones

- Interfaz gráfica.
- Recordatorios al cliente el día anterior al turno.
- Historial de servicios de cada cliente.
- Reportes por semana, por mes y por barbero.

## Integrantes

- Eroles
- Oyola
- Vasone
- Trejo
