package vetcare;

import java.util.*;

public class Cliente extends Persona {
    private String idCliente;
    private String direccion;
    private ArrayList<Mascota> mascotas;

    public Cliente(String idCliente, String nombre, String telefono, String email, String direccion) {
        super(nombre, telefono, email);
        this.idCliente = idCliente;
        this.direccion = direccion;
        this.mascotas = new ArrayList<>();
    }

    public String getIdCliente() { return idCliente; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public ArrayList<Mascota> getMascotas() { return mascotas; }

    public void agregarMascota(Mascota mascota) {
        mascotas.add(mascota);
    }

    @Override
    public String getTipo() { return "Cliente"; }

    @Override
    public String toString() {
        return idCliente + " | " + getNombre() + " | " + getTelefono() + " | " + getEmail() + " | " + direccion;
    }

    public String toCSV() {
        return idCliente + "," + getNombre() + "," + getTelefono() + "," + getEmail() + "," + direccion;
    }
}
