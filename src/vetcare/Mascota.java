package vetcare;

import java.util.*;

public class Mascota {
    private String idMascota;
    private String nombre;
    private String especie;
    private String raza;
    private int edad;
    private double peso;
    private String idCliente;
    private ArrayList<HistorialClinico> historial;

    public Mascota(String idMascota, String nombre, String especie, String raza, int edad, double peso, String idCliente) {
        this.idMascota = idMascota;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.peso = peso;
        this.idCliente = idCliente;
        this.historial = new ArrayList<>();
    }

    public String getIdMascota() { return idMascota; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    public String getIdCliente() { return idCliente; }
    public ArrayList<HistorialClinico> getHistorial() { return historial; }

    public void agregarHistorial(HistorialClinico registro) {
        historial.add(registro);
    }

    @Override
    public String toString() {
        return idMascota + " | " + nombre + " | " + especie + " | " + raza + " | " + edad + " años | " + peso + " kg";
    }

    public String toCSV() {
        return idMascota + "," + nombre + "," + especie + "," + raza + "," + edad + "," + peso + "," + idCliente;
    }
}
