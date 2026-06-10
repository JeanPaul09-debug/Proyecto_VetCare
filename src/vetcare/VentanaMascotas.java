package vetcare;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class VentanaMascotas extends JDialog {

    private GestorDatos datos;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtEspecie, txtRaza, txtEdad, txtPeso;
    private JComboBox<String> cmbCliente;

    public VentanaMascotas(JFrame padre) {
        super(padre, "Mascotas — VetCare", true);
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
        add(Tema.crearHeader("Mascotas", "Registra y consulta los pacientes de la clínica",
                new Color(10, 40, 25), new Color(100, 220, 160), this), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(16, 0));
        body.setBackground(Tema.BG_DARK);
        body.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel form = Tema.crearCard();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        form.setPreferredSize(new Dimension(240, 0));
        form.setMinimumSize(new Dimension(200, 0));

        txtNombre  = Tema.crearCampo(15);
        txtEspecie = Tema.crearCampo(15);
        txtRaza    = Tema.crearCampo(15);
        txtEdad    = Tema.crearCampo(15);
        txtPeso    = Tema.crearCampo(15);
        cmbCliente = Tema.crearCombo();
        cargarComboClientes();

        agregarCampo(form, "Nombre *", txtNombre);
        agregarCampo(form, "Especie *", txtEspecie);
        agregarCampo(form, "Raza", txtRaza);
        agregarCampo(form, "Edad (años) *", txtEdad);
        agregarCampo(form, "Peso (kg) *", txtPeso);

        JLabel lC = Tema.crearLabel("Cliente dueño *");
        lC.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbCliente.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbCliente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(lC); form.add(Box.createVerticalStrut(4));
        form.add(cmbCliente); form.add(Box.createVerticalStrut(14));

        JButton btnGuardar  = Tema.crearBoton("Guardar",   new Color(52, 175, 120));
        JButton btnLimpiar  = Tema.crearBoton("Limpiar",   new Color(50, 50, 80));
        JButton btnEliminar = Tema.crearBoton("Eliminar",  Tema.DANGER);
        for (JButton b : new JButton[]{btnGuardar, btnLimpiar, btnEliminar})
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(btnGuardar); form.add(Box.createVerticalStrut(8));
        form.add(btnLimpiar); form.add(Box.createVerticalStrut(8));
        form.add(btnEliminar);

        JPanel panelTabla = new JPanel(new BorderLayout(0, 8));
        panelTabla.setBackground(Tema.BG_DARK);
        JLabel lbl = Tema.crearLabel("Mascotas registradas");
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        String[] cols = {"ID", "Nombre", "Especie", "Raza", "Edad", "Peso kg", "Cliente"};
        modeloTabla = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabla = new JTable(modeloTabla);
        Tema.estilizarTabla(tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        Tema.estilizarScrollPane(scroll);
        panelTabla.add(lbl, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        body.add(form, BorderLayout.WEST);
        body.add(panelTabla, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarMascota());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnEliminar.addActionListener(e -> eliminarMascota());
    }

    private void agregarCampo(JPanel form, String label, JTextField campo) {
        JLabel lbl = Tema.crearLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        form.add(lbl); form.add(Box.createVerticalStrut(4));
        form.add(campo); form.add(Box.createVerticalStrut(10));
    }

    private void cargarComboClientes() {
        cmbCliente.removeAllItems();
        for (Cliente c : datos.getClientes()) cmbCliente.addItem(c.getIdCliente() + " - " + c.getNombre());
    }

    private void guardarMascota() {
        try {
            String nombre  = txtNombre.getText().trim();
            String especie = txtEspecie.getText().trim();
            if (nombre.isEmpty() || especie.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y especie son obligatorios.", "Error", JOptionPane.WARNING_MESSAGE); return;
            }
            if (cmbCliente.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Registre un cliente primero.", "Error", JOptionPane.WARNING_MESSAGE); return;
            }
            int edad    = Integer.parseInt(txtEdad.getText().trim());
            double peso = Double.parseDouble(txtPeso.getText().trim());
            String idCliente = ((String) cmbCliente.getSelectedItem()).split(" - ")[0];
            String id = datos.generarIdMascota();
            datos.agregarMascota(new Mascota(id, nombre, especie, txtRaza.getText().trim(), edad, peso, idCliente));
            GestorArchivos.guardarMascotas(datos.getMascotas());
            cargarTabla(); limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Mascota registrada: " + id, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La edad debe ser número entero y el peso número decimal.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarMascota() {
        int f = tabla.getSelectedRow();
        if (f < 0) { JOptionPane.showMessageDialog(this, "Seleccione una mascota.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        String id = (String) modeloTabla.getValueAt(f, 0);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar mascota " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            datos.eliminarMascota(id); GestorArchivos.guardarMascotas(datos.getMascotas()); cargarTabla(); limpiarFormulario();
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Mascota m : datos.getMascotas())
            modeloTabla.addRow(new Object[]{m.getIdMascota(), m.getNombre(), m.getEspecie(), m.getRaza(), m.getEdad(), m.getPeso(), m.getIdCliente()});
    }

    private void limpiarFormulario() {
        txtNombre.setText(""); txtEspecie.setText(""); txtRaza.setText(""); txtEdad.setText(""); txtPeso.setText("");
        cargarComboClientes(); tabla.clearSelection();
    }
}
