package vetcare;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class VentanaHistorial extends JDialog {

    private GestorDatos datos;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtFecha, txtDiagnostico, txtTratamiento, txtObservaciones;
    private JComboBox<String> cmbMascota;

    public VentanaHistorial(JFrame padre) {
        super(padre, "Historial Clínico — VetCare", true);
        datos = GestorDatos.getInstance();
        setMinimumSize(new Dimension(820, 540));
        setSize(1000, 640);
        setLocationRelativeTo(padre);
        getContentPane().setBackground(Tema.BG_DARK);
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(Tema.crearHeader(" Historial Clínico", "Registros médicos por mascota",
                new Color(30, 5, 40), Tema.ACCENT_LIGHT, this), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(16, 0));
        body.setBackground(Tema.BG_DARK);
        body.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel form = Tema.crearCard();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        form.setPreferredSize(new Dimension(250, 0));
        form.setMinimumSize(new Dimension(200, 0));

        txtFecha         = Tema.crearCampoFecha();
        txtDiagnostico   = Tema.crearCampo(15);
        txtTratamiento   = Tema.crearCampo(15);
        txtObservaciones = Tema.crearCampo(15);
        cmbMascota       = Tema.crearCombo();
        cargarComboMascotas();

        JLabel lM = Tema.crearLabel("Mascota *");
        lM.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMascota.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMascota.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(lM); form.add(Box.createVerticalStrut(4));
        form.add(cmbMascota); form.add(Box.createVerticalStrut(10));

        JLabel lblFechaHint = Tema.crearLabel("DD/MM/AAAA  (se formatea automático)");
        lblFechaHint.setFont(new Font("Arial", Font.ITALIC, 10));
        lblFechaHint.setForeground(new Color(100, 100, 140));
        agregarCampoConHint(form, "Fecha *", txtFecha, lblFechaHint);
        agregarCampo(form, "Diagnóstico *", txtDiagnostico);
        agregarCampo(form, "Tratamiento *", txtTratamiento);
        agregarCampo(form, "Observaciones", txtObservaciones);

        form.add(Box.createVerticalStrut(6));
        JButton btnGuardar = Tema.crearBoton("Guardar registro",   new Color(150, 50, 200));
        JButton btnFiltrar = Tema.crearBoton("Filtrar por mascota", new Color(70, 130, 180));
        JButton btnTodos   = Tema.crearBoton("Ver todos",           new Color(50, 50, 80));
        for (JButton b : new JButton[]{btnGuardar, btnFiltrar, btnTodos})
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(btnGuardar); form.add(Box.createVerticalStrut(8));
        form.add(btnFiltrar); form.add(Box.createVerticalStrut(8));
        form.add(btnTodos);

        JPanel panelTabla = new JPanel(new BorderLayout(0, 8));
        panelTabla.setBackground(Tema.BG_DARK);
        JLabel lbl = Tema.crearLabel("Registros clínicos");
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        String[] cols = {"ID", "Fecha", "Diagnóstico", "Tratamiento", "Observaciones", "Mascota"};
        modeloTabla = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabla = new JTable(modeloTabla);
        Tema.estilizarTabla(tabla);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int f = tabla.getSelectedRow();
                txtFecha.setText((String) modeloTabla.getValueAt(f, 1));
                txtDiagnostico.setText((String) modeloTabla.getValueAt(f, 2));
                txtTratamiento.setText((String) modeloTabla.getValueAt(f, 3));
                txtObservaciones.setText((String) modeloTabla.getValueAt(f, 4));
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        Tema.estilizarScrollPane(scroll);
        panelTabla.add(lbl, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        body.add(form, BorderLayout.WEST);
        body.add(panelTabla, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarRegistro());
        btnFiltrar.addActionListener(e -> filtrarPorMascota());
        btnTodos.addActionListener(e -> { cargarTabla(); tabla.clearSelection(); });
    }

    private void agregarCampo(JPanel form, String label, JTextField campo) {
        JLabel lbl = Tema.crearLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(lbl); form.add(Box.createVerticalStrut(4));
        form.add(campo); form.add(Box.createVerticalStrut(10));
    }

    private void agregarCampoConHint(JPanel form, String label, JTextField campo, JLabel hint) {
        JLabel lbl = Tema.crearLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(lbl); form.add(Box.createVerticalStrut(4));
        form.add(campo); form.add(Box.createVerticalStrut(2));
        form.add(hint);  form.add(Box.createVerticalStrut(8));
    }

    private void cargarComboMascotas() {
        cmbMascota.removeAllItems();
        for (Mascota m : datos.getMascotas()) cmbMascota.addItem(m.getIdMascota() + " - " + m.getNombre());
    }

    private void guardarRegistro() {
        String fecha = txtFecha.getText().trim();
        String diag  = txtDiagnostico.getText().trim();
        String trat  = txtTratamiento.getText().trim();
        if (fecha.length() < 10 || diag.isEmpty() || trat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fecha, diagnóstico y tratamiento son obligatorios.", "Error", JOptionPane.WARNING_MESSAGE); return;
        }
        if (cmbMascota.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una mascota.", "Error", JOptionPane.WARNING_MESSAGE); return;
        }
        String idMascota = ((String) cmbMascota.getSelectedItem()).split(" - ")[0];
        String id = datos.generarIdHistorial();
        datos.agregarHistorial(new HistorialClinico(id, fecha, diag, trat, txtObservaciones.getText().trim(), idMascota));
        GestorArchivos.guardarHistorial(datos.getHistoriales());
        cargarTabla(); limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Registro guardado: " + id, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void filtrarPorMascota() {
        if (cmbMascota.getSelectedItem() == null) { cargarTabla(); return; }
        String idMascota = ((String) cmbMascota.getSelectedItem()).split(" - ")[0];
        modeloTabla.setRowCount(0);
        for (HistorialClinico h : datos.getHistorialPorMascota(idMascota))
            modeloTabla.addRow(new Object[]{h.getIdRegistro(), h.getFecha(), h.getDiagnostico(), h.getTratamiento(), h.getObservaciones(), h.getIdMascota()});
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (HistorialClinico h : datos.getHistoriales())
            modeloTabla.addRow(new Object[]{h.getIdRegistro(), h.getFecha(), h.getDiagnostico(), h.getTratamiento(), h.getObservaciones(), h.getIdMascota()});
    }

    private void limpiarFormulario() {
        txtFecha.setText(""); txtDiagnostico.setText(""); txtTratamiento.setText(""); txtObservaciones.setText("");
        cargarComboMascotas(); tabla.clearSelection();
    }
}
