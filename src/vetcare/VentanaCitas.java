package vetcare;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class VentanaCitas extends JDialog {

    private GestorDatos datos;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtFecha, txtHora, txtMotivo;
    private JComboBox<String> cmbMascota, cmbEstado;

    public VentanaCitas(JFrame padre) {
        super(padre, "Citas — VetCare", true);
        datos = GestorDatos.getInstance();
        setMinimumSize(new Dimension(800, 520));
        setSize(960, 620);
        setLocationRelativeTo(padre);
        getContentPane().setBackground(Tema.BG_DARK);
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(Tema.crearHeader("  Citas", "Agenda y gestiona las consultas médicas",
                new Color(40, 30, 5), Tema.WARNING, this), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(16, 0));
        body.setBackground(Tema.BG_DARK);
        body.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel form = Tema.crearCard();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        form.setPreferredSize(new Dimension(240, 0));
        form.setMinimumSize(new Dimension(200, 0));

        // ── Campos con autoformato ──
        txtFecha  = Tema.crearCampoFecha();
        txtHora   = Tema.crearCampoHora();
        txtMotivo = Tema.crearCampo(15);
        cmbMascota = Tema.crearCombo();
        cmbEstado  = Tema.crearCombo();
        cmbEstado.addItem("Pendiente");
        cmbEstado.addItem("Completada");
        cmbEstado.addItem("Cancelada");
        cargarComboMascotas();

        // Placeholder visual
        JLabel lblFechaHint = Tema.crearLabel("DD/MM/AAAA");
        lblFechaHint.setFont(new Font("Arial", Font.ITALIC, 10));
        lblFechaHint.setForeground(new Color(100, 100, 140));

        JLabel lblHoraHint = Tema.crearLabel("HH:MM  (se formatea automático)");
        lblHoraHint.setFont(new Font("Arial", Font.ITALIC, 10));
        lblHoraHint.setForeground(new Color(100, 100, 140));

        agregarCampoConHint(form, "Fecha *", txtFecha, lblFechaHint);
        agregarCampoConHint(form, "Hora *",  txtHora,  lblHoraHint);
        agregarCampo(form, "Motivo *", txtMotivo);

        JLabel lM = Tema.crearLabel("Mascota *");
        lM.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMascota.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMascota.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(lM); form.add(Box.createVerticalStrut(4));
        form.add(cmbMascota); form.add(Box.createVerticalStrut(10));

        JLabel lE = Tema.crearLabel("Estado");
        lE.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbEstado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(lE); form.add(Box.createVerticalStrut(4));
        form.add(cmbEstado); form.add(Box.createVerticalStrut(14));

        JButton btnGuardar = Tema.crearBoton("Agendar",          new Color(200, 130, 0));
        JButton btnActual  = Tema.crearBoton("Actualizar estado", new Color(70, 130, 180));
        JButton btnElim    = Tema.crearBoton("Eliminar",          Tema.DANGER);
        for (JButton b : new JButton[]{btnGuardar, btnActual, btnElim})
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(btnGuardar); form.add(Box.createVerticalStrut(8));
        form.add(btnActual);  form.add(Box.createVerticalStrut(8));
        form.add(btnElim);

        // ── Tabla ──
        JPanel panelTabla = new JPanel(new BorderLayout(0, 8));
        panelTabla.setBackground(Tema.BG_DARK);
        JLabel lbl = Tema.crearLabel("Citas agendadas");
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        String[] cols = {"ID", "Fecha", "Hora", "Motivo", "Mascota", "Cliente", "Estado"};
        modeloTabla = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabla = new JTable(modeloTabla);
        Tema.estilizarTabla(tabla);

        // Color por estado
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                Color bg = (r % 2 == 0) ? Tema.BG_CARD : new Color(25, 25, 50);
                setBackground(sel ? new Color(124, 58, 237, 120) : bg);
                setForeground(Tema.TEXT_PRIMARY);
                if (!sel && c == 6) {
                    String est = (String) t.getValueAt(r, 6);
                    if ("Completada".equals(est)) setForeground(Tema.SUCCESS);
                    else if ("Cancelada".equals(est)) setForeground(Tema.DANGER);
                    else setForeground(Tema.WARNING);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int f = tabla.getSelectedRow();
                txtFecha.setText((String) modeloTabla.getValueAt(f, 1));
                txtHora.setText((String) modeloTabla.getValueAt(f, 2));
                txtMotivo.setText((String) modeloTabla.getValueAt(f, 3));
                cmbEstado.setSelectedItem(modeloTabla.getValueAt(f, 6));
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        Tema.estilizarScrollPane(scroll);
        panelTabla.add(lbl, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        body.add(form, BorderLayout.WEST);
        body.add(panelTabla, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarCita());
        btnActual.addActionListener(e -> actualizarEstado());
        btnElim.addActionListener(e -> eliminarCita());
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

    private void guardarCita() {
        String fecha  = txtFecha.getText().trim();
        String hora   = txtHora.getText().trim();
        String motivo = txtMotivo.getText().trim();
        if (fecha.length() < 10 || hora.length() < 5 || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa fecha (DD/MM/AAAA), hora (HH:MM) y motivo.", "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cmbMascota.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Registre una mascota primero.", "Error", JOptionPane.WARNING_MESSAGE); return;
        }
        String idMascota = ((String) cmbMascota.getSelectedItem()).split(" - ")[0];
        Mascota m = datos.buscarMascotaPorId(idMascota);
        String id = datos.generarIdCita();
        datos.agregarCita(new Cita(id, fecha, hora, motivo, idMascota, m != null ? m.getIdCliente() : ""));
        GestorArchivos.guardarCitas(datos.getCitas());
        cargarTabla(); limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Cita agendada: " + id, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarEstado() {
        int f = tabla.getSelectedRow();
        if (f < 0) { JOptionPane.showMessageDialog(this, "Seleccione una cita.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        String id = (String) modeloTabla.getValueAt(f, 0);
        for (Cita c : datos.getCitas()) {
            if (c.getIdCita().equals(id)) { c.setEstado((String) cmbEstado.getSelectedItem()); break; }
        }
        GestorArchivos.guardarCitas(datos.getCitas()); cargarTabla();
    }

    private void eliminarCita() {
        int f = tabla.getSelectedRow();
        if (f < 0) { JOptionPane.showMessageDialog(this, "Seleccione una cita.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        String id = (String) modeloTabla.getValueAt(f, 0);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar cita " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            datos.eliminarCita(id); GestorArchivos.guardarCitas(datos.getCitas()); cargarTabla(); limpiarFormulario();
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Cita c : datos.getCitas())
            modeloTabla.addRow(new Object[]{c.getIdCita(), c.getFecha(), c.getHora(), c.getMotivo(), c.getIdMascota(), c.getIdCliente(), c.getEstado()});
    }

    private void limpiarFormulario() {
        txtFecha.setText(""); txtHora.setText(""); txtMotivo.setText("");
        cargarComboMascotas(); tabla.clearSelection();
    }
}
