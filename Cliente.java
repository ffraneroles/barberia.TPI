
public class Cliente extends Persona {

    private String email;

    public Cliente(String nombre, String apellido, String telefono, String email) {
        super(nombre, apellido, telefono);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
