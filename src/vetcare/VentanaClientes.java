package vetcare;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class VentanaClientes extends JDialog {

    private GestorDatos datos;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtTelefono, txtEmail, txtDireccion;

    public VentanaClientes(JFrame padre) {
        super(padre, "Clientes — VetCare", true);
        datos = GestorDatos.getInstance();
        setMinimumSize(new Dimension(750, 500));
        setSize(920, 600);
        setLocationRelativeTo(padre);
        getContentPane().setBackground(Tema.BG_DARK);
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        add(Tema.crearHeader("Gestión de Clientes", "Registra, consulta y elimina clientes",
                new Color(30, 10, 60), Tema.ACCENT_LIGHT, this), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(16, 0));
        body.setBackground(Tema.BG_DARK);
        body.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        // ── Formulario ──
        JPanel form = Tema.crearCard();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        form.setPreferredSize(new Dimension(240, 0));
        form.setMinimumSize(new Dimension(200, 0));

        txtNombre    = Tema.crearCampo(15);
        txtTelefono  = Tema.crearCampo(15);
        txtEmail     = Tema.crearCampo(15);
        txtDireccion = Tema.crearCampo(15);

        agregarCampo(form, "Nombre *", txtNombre);
        agregarCampo(form, "Teléfono *", txtTelefono);
        agregarCampo(form, "Email", txtEmail);
        agregarCampo(form, "Dirección", txtDireccion);
        form.add(Box.createVerticalStrut(14));

        JButton btnGuardar  = Tema.crearBoton("Guardar",   Tema.ACCENT);
        JButton btnLimpiar  = Tema.crearBoton("Limpiar",   new Color(50, 50, 80));
        JButton btnEliminar = Tema.crearBoton("Eliminar",  Tema.DANGER);
        for (JButton b : new JButton[]{btnGuardar, btnLimpiar, btnEliminar})
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        form.add(btnGuardar); form.add(Box.createVerticalStrut(8));
        form.add(btnLimpiar); form.add(Box.createVerticalStrut(8));
        form.add(btnEliminar);

        // ── Tabla ──
        JPanel panelTabla = new JPanel(new BorderLayout(0, 8));
        panelTabla.setBackground(Tema.BG_DARK);

        JLabel lbl = Tema.crearLabel("Clientes registrados");
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        String[] cols = {"ID", "Nombre", "Teléfono", "Email", "Dirección"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        Tema.estilizarTabla(tabla);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int f = tabla.getSelectedRow();
                txtNombre.setText((String) modeloTabla.getValueAt(f, 1));
                txtTelefono.setText((String) modeloTabla.getValueAt(f, 2));
                txtEmail.setText((String) modeloTabla.getValueAt(f, 3));
                txtDireccion.setText((String) modeloTabla.getValueAt(f, 4));
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        Tema.estilizarScrollPane(scroll);
        panelTabla.add(lbl, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        body.add(form, BorderLayout.WEST);
        body.add(panelTabla, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarCliente());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnEliminar.addActionListener(e -> eliminarCliente());
    }

    private void agregarCampo(JPanel form, String label, JTextField campo) {
        JLabel lbl = Tema.crearLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(lbl); form.add(Box.createVerticalStrut(4));
        form.add(campo); form.add(Box.createVerticalStrut(10));
    }

    private void guardarCliente() {
        String nombre = txtNombre.getText().trim();
        String tel    = txtTelefono.getText().trim();
        if (nombre.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y teléfono son obligatorios.", "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = datos.generarIdCliente();
        datos.agregarCliente(new Cliente(id, nombre, tel, txtEmail.getText().trim(), txtDireccion.getText().trim()));
        GestorArchivos.guardarClientes(datos.getClientes());
        cargarTabla(); limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Cliente registrado: " + id, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarCliente() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un cliente.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        String id = (String) modeloTabla.getValueAt(fila, 0);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar cliente " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            datos.eliminarCliente(id);
            GestorArchivos.guardarClientes(datos.getClientes());
            cargarTabla(); limpiarFormulario();
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Cliente c : datos.getClientes())
            modeloTabla.addRow(new Object[]{c.getIdCliente(), c.getNombre(), c.getTelefono(), c.getEmail(), c.getDireccion()});
    }

    private void limpiarFormulario() {
        txtNombre.setText(""); txtTelefono.setText(""); txtEmail.setText(""); txtDireccion.setText("");
        tabla.clearSelection();
    }
}
