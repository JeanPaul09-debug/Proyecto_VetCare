package vetcare;

import java.io.*;
import java.util.*;

public class GestorArchivos {
    private static final String RUTA_CLIENTES  = "data/clientes.csv";
    private static final String RUTA_MASCOTAS  = "data/mascotas.csv";
    private static final String RUTA_CITAS     = "data/citas.csv";
    private static final String RUTA_HISTORIAL = "data/historial.csv";

    // ── GUARDAR ───────────────────────────────────────────
    public static void guardarClientes(ArrayList<Cliente> clientes) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_CLIENTES))) {
            for (Cliente c : clientes) {
                pw.println(c.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error guardando clientes: " + e.getMessage());
        }
    }

    public static void guardarMascotas(ArrayList<Mascota> mascotas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_MASCOTAS))) {
            for (Mascota m : mascotas) {
                pw.println(m.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error guardando mascotas: " + e.getMessage());
        }
    }

    public static void guardarCitas(ArrayList<Cita> citas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_CITAS))) {
            for (Cita c : citas) {
                pw.println(c.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error guardando citas: " + e.getMessage());
        }
    }

    public static void guardarHistorial(ArrayList<HistorialClinico> historiales) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_HISTORIAL))) {
            for (HistorialClinico h : historiales) {
                pw.println(h.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error guardando historial: " + e.getMessage());
        }
    }

    public static void guardarTodo(GestorDatos datos) {
        guardarClientes(datos.getClientes());
        guardarMascotas(datos.getMascotas());
        guardarCitas(datos.getCitas());
        guardarHistorial(datos.getHistoriales());
    }

    // ── CARGAR ────────────────────────────────────────────
    public static void cargarTodo(GestorDatos datos) {
        cargarClientes(datos);
        cargarMascotas(datos);
        cargarCitas(datos);
        cargarHistorial(datos);
    }

    private static void cargarClientes(GestorDatos datos) {
        File archivo = new File(RUTA_CLIENTES);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int maxId = 0;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",", -1);
                if (p.length >= 5) {
                    Cliente c = new Cliente(p[0], p[1], p[2], p[3], p[4]);
                    datos.agregarCliente(c);
                    try {
                        int num = Integer.parseInt(p[0].replace("CLI-", ""));
                        if (num > maxId) maxId = num;
                    } catch (NumberFormatException ignored) {}
                }
            }
            datos.setContadorClientes(maxId + 1);
        } catch (IOException e) {
            System.err.println("Error cargando clientes: " + e.getMessage());
        }
    }

    private static void cargarMascotas(GestorDatos datos) {
        File archivo = new File(RUTA_MASCOTAS);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int maxId = 0;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",", -1);
                if (p.length >= 7) {
                    try {
                        Mascota m = new Mascota(p[0], p[1], p[2], p[3],
                                Integer.parseInt(p[4]), Double.parseDouble(p[5]), p[6]);
                        datos.agregarMascota(m);
                        int num = Integer.parseInt(p[0].replace("MAS-", ""));
                        if (num > maxId) maxId = num;
                    } catch (NumberFormatException ignored) {}
                }
            }
            datos.setContadorMascotas(maxId + 1);
        } catch (IOException e) {
            System.err.println("Error cargando mascotas: " + e.getMessage());
        }
    }

    private static void cargarCitas(GestorDatos datos) {
        File archivo = new File(RUTA_CITAS);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int maxId = 0;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",", -1);
                if (p.length >= 7) {
                    Cita c = new Cita(p[0], p[1], p[2], p[3], p[4], p[5]);
                    c.setEstado(p[6]);
                    datos.agregarCita(c);
                    try {
                        int num = Integer.parseInt(p[0].replace("CIT-", ""));
                        if (num > maxId) maxId = num;
                    } catch (NumberFormatException ignored) {}
                }
            }
            datos.setContadorCitas(maxId + 1);
        } catch (IOException e) {
            System.err.println("Error cargando citas: " + e.getMessage());
        }
    }

    private static void cargarHistorial(GestorDatos datos) {
        File archivo = new File(RUTA_HISTORIAL);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int maxId = 0;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(",", -1);
                if (p.length >= 6) {
                    HistorialClinico h = new HistorialClinico(p[0], p[1], p[2], p[3], p[4], p[5]);
                    datos.agregarHistorial(h);
                    try {
                        int num = Integer.parseInt(p[0].replace("HIS-", ""));
                        if (num > maxId) maxId = num;
                    } catch (NumberFormatException ignored) {}
                }
            }
            datos.setContadorHistorial(maxId + 1);
        } catch (IOException e) {
            System.err.println("Error cargando historial: " + e.getMessage());
        }
    }

    public static void crearCarpetaData() {
        new File("data").mkdirs();
    }
}
