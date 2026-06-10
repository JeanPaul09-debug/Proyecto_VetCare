package vetcare;

public class HistorialClinico {
    private String idRegistro;
    private String fecha;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private String idMascota;

    public HistorialClinico(String idRegistro, String fecha, String diagnostico, String tratamiento, String observaciones, String idMascota) {
        this.idRegistro = idRegistro;
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.observaciones = observaciones;
        this.idMascota = idMascota;
    }

    public String getIdRegistro() { return idRegistro; }
    public String getFecha() { return fecha; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public String getIdMascota() { return idMascota; }

    @Override
    public String toString() {
        return idRegistro + " | " + fecha + " | " + diagnostico + " | " + tratamiento;
    }

    public String toCSV() {
        return idRegistro + "," + fecha + "," + diagnostico + "," + tratamiento + "," + observaciones + "," + idMascota;
    }
}
