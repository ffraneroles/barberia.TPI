import java.util.ArrayList;

public class Barbero extends Persona {

    
    private ArrayList<String> especialidades;

    public Barbero(String nombre, String apellido, String telefono, ArrayList<String> especialidades) {
        super(nombre, apellido, telefono);
        
        this.especialidades = new ArrayList<>(especialidades);
    }

    public ArrayList<String> getEspecialidades() {
        return new ArrayList<>(especialidades);
    }

    public void setEspecialidades(ArrayList<String> especialidades) {
        this.especialidades = new ArrayList<>(especialidades);
    }

    public boolean tieneEspecialidad(String especialidad) {
        for (String e : especialidades) {
            if (e.equalsIgnoreCase(especialidad)) {
                return true;
            }
        }
        return false;
    }

    
    public String especialidadesComoTexto() {
        if (especialidades.isEmpty()) {
            return "Sin especialidad";
        }
        return String.join(", ", especialidades);
    }
}
