import java.util.ArrayList;

public class GestorEspecialidad {

    private ArrayList<String> especialidades;

    public GestorEspecialidad() {
        this.especialidades = new ArrayList<>();
        especialidades.add("Corte de cabello");
        especialidades.add("Arreglo de barba");
        especialidades.add("Coloraciones");
        especialidades.add("Perfilado de cejas");
    }

    public ArrayList<String> getEspecialidades() {
        return new ArrayList<>(especialidades);
    }

    
    public boolean existe(String nombre) {
        for (String e : especialidades) {
            if (e.equalsIgnoreCase(nombre.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean agregarEspecialidad(String nombre) {
        if (nombre == null || nombre.trim().isEmpty() || existe(nombre)) {
            return false;
        }
        especialidades.add(nombre.trim());
        return true;
    }
}
