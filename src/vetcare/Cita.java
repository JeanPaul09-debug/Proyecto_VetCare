package vetcare;

public class Cita {
    private String idCita;
    private String fecha;
    private String hora;
    private String motivo;
    private String idMascota;
    private String idCliente;
    private String estado;

    public Cita(String idCita, String fecha, String hora, String motivo, String idMascota, String idCliente) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.idMascota = idMascota;
        this.idCliente = idCliente;
        this.estado = "Pendiente";
    }

    public String getIdCita() { return idCita; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getIdMascota() { return idMascota; }
    public String getIdCliente() { return idCliente; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return idCita + " | " + fecha + " " + hora + " | " + motivo + " | " + estado;
    }

    public String toCSV() {
        return idCita + "," + fecha + "," + hora + "," + motivo + "," + idMascota + "," + idCliente + "," + estado;
    }
}
