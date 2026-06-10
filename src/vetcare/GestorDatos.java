package vetcare;

import java.util.*;

public class GestorDatos {
    private static GestorDatos instancia;
    private ArrayList<Cliente> clientes;
    private ArrayList<Mascota> mascotas;
    private ArrayList<Cita> citas;
    private ArrayList<HistorialClinico> historiales;
    private int contadorClientes = 1;
    private int contadorMascotas = 1;
    private int contadorCitas = 1;
    private int contadorHistorial = 1;

    private GestorDatos() {
        clientes = new ArrayList<>();
        mascotas = new ArrayList<>();
        citas = new ArrayList<>();
        historiales = new ArrayList<>();
    }

    public static GestorDatos getInstance() {
        if (instancia == null) {
            instancia = new GestorDatos();
        }
        return instancia;
    }

    // ── CLIENTES ──────────────────────────────────────────
    public String generarIdCliente() {
        return "CLI-" + String.format("%03d", contadorClientes++);
    }

    public void agregarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public Cliente buscarClientePorId(String id) {
        for (Cliente c : clientes) {
            if (c.getIdCliente().equals(id)) return c;
        }
        return null;
    }

    public boolean eliminarCliente(String id) {
        return clientes.removeIf(c -> c.getIdCliente().equals(id));
    }

    // ── MASCOTAS ──────────────────────────────────────────
    public String generarIdMascota() {
        return "MAS-" + String.format("%03d", contadorMascotas++);
    }

    public void agregarMascota(Mascota mascota) {
        mascotas.add(mascota);
        Cliente cliente = buscarClientePorId(mascota.getIdCliente());
        if (cliente != null) {
            cliente.agregarMascota(mascota);
        }
    }

    public ArrayList<Mascota> getMascotas() {
        return mascotas;
    }

    public Mascota buscarMascotaPorId(String id) {
        for (Mascota m : mascotas) {
            if (m.getIdMascota().equals(id)) return m;
        }
        return null;
    }

    public ArrayList<Mascota> getMascotasPorCliente(String idCliente) {
        ArrayList<Mascota> resultado = new ArrayList<>();
        for (Mascota m : mascotas) {
            if (m.getIdCliente().equals(idCliente)) resultado.add(m);
        }
        return resultado;
    }

    public boolean eliminarMascota(String id) {
        return mascotas.removeIf(m -> m.getIdMascota().equals(id));
    }

    // ── CITAS ──────────────────────────────────────────────
    public String generarIdCita() {
        return "CIT-" + String.format("%03d", contadorCitas++);
    }

    public void agregarCita(Cita cita) {
        citas.add(cita);
    }

    public ArrayList<Cita> getCitas() {
        return citas;
    }

    public ArrayList<Cita> getCitasPorMascota(String idMascota) {
        ArrayList<Cita> resultado = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getIdMascota().equals(idMascota)) resultado.add(c);
        }
        return resultado;
    }

    public boolean eliminarCita(String id) {
        return citas.removeIf(c -> c.getIdCita().equals(id));
    }

    // ── HISTORIAL ──────────────────────────────────────────
    public String generarIdHistorial() {
        return "HIS-" + String.format("%03d", contadorHistorial++);
    }

    public void agregarHistorial(HistorialClinico registro) {
        historiales.add(registro);
        Mascota mascota = buscarMascotaPorId(registro.getIdMascota());
        if (mascota != null) {
            mascota.agregarHistorial(registro);
        }
    }

    public ArrayList<HistorialClinico> getHistoriales() {
        return historiales;
    }

    public ArrayList<HistorialClinico> getHistorialPorMascota(String idMascota) {
        ArrayList<HistorialClinico> resultado = new ArrayList<>();
        for (HistorialClinico h : historiales) {
            if (h.getIdMascota().equals(idMascota)) resultado.add(h);
        }
        return resultado;
    }

    // ── CONTADORES (para carga desde archivo) ──────────────
    public void setContadorClientes(int n) { this.contadorClientes = n; }
    public void setContadorMascotas(int n) { this.contadorMascotas = n; }
    public void setContadorCitas(int n) { this.contadorCitas = n; }
    public void setContadorHistorial(int n) { this.contadorHistorial = n; }
}
