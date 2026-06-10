package vetcare;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaPrincipal extends JFrame {

    private GestorDatos datos;

    public VentanaPrincipal() {
        datos = GestorDatos.getInstance();
        GestorArchivos.crearCarpetaData();
        GestorArchivos.cargarTodo(datos);
        Tema.aplicarTemaGlobal();

        setTitle("VetCare - Clínica Veterinaria Huellitas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Tema.BG_DARK);

        initComponents();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                GestorArchivos.guardarTodo(datos);
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // ── HEADER ──────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 10, 60), getWidth(), 0, new Color(10, 10, 30));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // glow effect
                g2.setColor(new Color(124, 58, 237, 30));
                g2.fillOval(getWidth()/2 - 200, -80, 400, 200);
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(780, 120));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel headerLeft = new JPanel();
        headerLeft.setOpaque(false);
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));

        JLabel lblPaw = new JLabel("VetCare");
        lblPaw.setFont(new Font("Arial", Font.BOLD, 32));
        lblPaw.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Clínica Veterinaria Huellitas");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSub.setForeground(Tema.ACCENT_LIGHT);

        headerLeft.add(lblPaw);
        headerLeft.add(Box.createVerticalStrut(4));
        headerLeft.add(lblSub);

        JLabel lblVersion = new JLabel("v1.0");
        lblVersion.setFont(Tema.FONT_SMALL);
        lblVersion.setForeground(Tema.TEXT_SECONDARY);

        header.add(headerLeft, BorderLayout.WEST);
        header.add(lblVersion, BorderLayout.EAST);

        // ── SEPARADOR ───────────────────────────────────────────────────────
        JSeparator sep = new JSeparator();
        sep.setForeground(Tema.BORDER);
        sep.setBackground(Tema.BG_DARK);

        // ── GRID DE MÓDULOS ──────────────────────────────────────────────────
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(Tema.BG_DARK);
        centro.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        JLabel lblModulos = new JLabel("Módulos del Sistema");
        lblModulos.setFont(Tema.FONT_HEADER);
        lblModulos.setForeground(Tema.TEXT_SECONDARY);
        lblModulos.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setBackground(Tema.BG_DARK);

        JPanel[] cards = {
            crearModulo("", "Clientes",  "Gestionar dueños de mascotas", Tema.ACCENT),
            crearModulo("", "Mascotas",  "Registrar y consultar pacientes", new Color(52, 175, 120)),
            crearModulo("", "Citas",     "Agendar consultas médicas",  new Color(217, 148, 0)),
            crearModulo("", "Historial", "Historial clínico por mascota", new Color(150, 50, 200))
        };

        for (JPanel card : cards) grid.add(card);

        // Acciones
        cards[0].addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { new VentanaClientes(VentanaPrincipal.this).setVisible(true); }
        });
        cards[1].addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { new VentanaMascotas(VentanaPrincipal.this).setVisible(true); }
        });
        cards[2].addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { new VentanaCitas(VentanaPrincipal.this).setVisible(true); }
        });
        cards[3].addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { new VentanaHistorial(VentanaPrincipal.this).setVisible(true); }
        });

        centro.add(lblModulos, BorderLayout.NORTH);
        centro.add(grid, BorderLayout.CENTER);

        // ── FOOTER ──────────────────────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(Tema.BG_DARK);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDER));
        JLabel lblFooter = new JLabel("Sistema VetCare  •  Datos guardados automáticamente al cerrar");
        lblFooter.setFont(Tema.FONT_SMALL);
        lblFooter.setForeground(Tema.TEXT_SECONDARY);
        footer.add(lblFooter);

        add(header, BorderLayout.NORTH);
        add(sep, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        // Fix layout
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Tema.BG_DARK);
        wrapper.add(header, BorderLayout.NORTH);
        wrapper.add(centro, BorderLayout.CENTER);
        wrapper.add(footer, BorderLayout.SOUTH);
        setContentPane(wrapper);
    }

    private JPanel crearModulo(String icono, String titulo, String desc, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(Tema.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                // top accent line
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), 4, 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblIcono = new JLabel(icono + "  " + titulo);
        lblIcono.setFont(new Font("Arial", Font.BOLD, 17));
        lblIcono.setForeground(Color.WHITE);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(Tema.FONT_SMALL);
        lblDesc.setForeground(Tema.TEXT_SECONDARY);


        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(lblIcono);
        content.add(Box.createVerticalStrut(6));
        content.add(lblDesc);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 1),
                    BorderFactory.createEmptyBorder(19, 19, 19, 19)
                ));
            }
        public void mouseExited(MouseEvent e) {
            card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }
        });

        card.add(content, BorderLayout.CENTER);
        return card;
    }
}
