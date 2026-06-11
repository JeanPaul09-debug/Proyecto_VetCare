package vetcare;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class Tema {

    // ── COLORES ──────────────────────────────────────────────────────────────
    public static final Color BG_DARK        = new Color(10,  10,  25);
    public static final Color BG_CARD        = new Color(20,  20,  45);
    public static final Color BG_INPUT       = new Color(28,  28,  55);
    public static final Color ACCENT         = new Color(124, 58, 237);
    public static final Color ACCENT_LIGHT   = new Color(167,120, 255);
    public static final Color ACCENT_HOVER   = new Color(109, 40, 217);
    public static final Color SUCCESS        = new Color(52, 211, 153);
    public static final Color WARNING        = new Color(251,191,  36);
    public static final Color DANGER         = new Color(239, 68,  68);
    public static final Color TEXT_PRIMARY   = new Color(240,240, 255);
    public static final Color TEXT_SECONDARY = new Color(148,148, 180);
    public static final Color BORDER         = new Color(55,  55,  90);

    // ── FUENTES ──────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE  = new Font("Arial", Font.BOLD,  22);
    public static final Font FONT_HEADER = new Font("Arial", Font.BOLD,  15);
    public static final Font FONT_BODY   = new Font("Arial", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("Arial", Font.PLAIN, 11);
    public static final Font FONT_BUTTON = new Font("Arial", Font.BOLD,  13);

    // ── APLICAR TEMA GLOBAL (arregla JOptionPane, JComboBox popup, etc.) ─────
    public static void aplicarTemaGlobal() {
        // Fondo oscuro para diálogos del sistema
        UIManager.put("OptionPane.background",          BG_CARD);
        UIManager.put("OptionPane.messageForeground",   TEXT_PRIMARY);
        UIManager.put("Panel.background",               BG_DARK);
        // ComboBox popup
        UIManager.put("ComboBox.background",            BG_INPUT);
        UIManager.put("ComboBox.foreground",            TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground",   ACCENT);
        UIManager.put("ComboBox.selectionForeground",   TEXT_PRIMARY);
        UIManager.put("ComboBox.buttonBackground",      BG_INPUT);
        UIManager.put("ComboBox.disabledForeground",    TEXT_SECONDARY);
        UIManager.put("List.background",                BG_CARD);
        UIManager.put("List.foreground",                TEXT_PRIMARY);
        UIManager.put("List.selectionBackground",       ACCENT);
        UIManager.put("List.selectionForeground",       TEXT_PRIMARY);
        // ScrollBar
        UIManager.put("ScrollBar.background",           BG_CARD);
        UIManager.put("ScrollBar.thumb",                BORDER);
        UIManager.put("ScrollBar.track",                BG_CARD);
        // Botones de diálogos
        UIManager.put("Button.background",  new Color(80, 50, 180));
        UIManager.put("Button.foreground",  new Color(80, 50, 180));
        UIManager.put("Button.select",      new Color(60, 30, 150));
        UIManager.put("Button.focus",       new Color(80, 50, 180));
        UIManager.put("Button.border",      BorderFactory.createEmptyBorder(6, 16, 6, 16));
    }

    // ── BOTÓN ────────────────────────────────────────────────────────────────
    public static JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BUTTON);
        btn.setBackground(color);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 20, 36));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }

    // ── INPUT ────────────────────────────────────────────────────────────────
    public static JTextField crearCampo(int cols) {
        JTextField campo = new JTextField(cols);
        campo.setBackground(BG_INPUT);
        campo.setForeground(TEXT_PRIMARY);
        campo.setCaretColor(ACCENT_LIGHT);
        campo.setFont(FONT_BODY);
        campo.setOpaque(true);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        // Focus highlight
        campo.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_LIGHT, 1),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)
                ));
            }
            public void focusLost(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)
                ));
            }
        });
        return campo;
    }

    // ── INPUT FECHA (auto-inserta "/") ───────────────────────────────────────
    public static JTextField crearCampoFecha() {
        JTextField campo = crearCampo(10);
        campo.setToolTipText("Formato: DD/MM/AAAA");

        PlainDocument doc = new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;
                String actual = getText(0, getLength());
                StringBuilder sb = new StringBuilder(actual);
                for (char c : str.toCharArray()) {
                    int pos = sb.length();
                    if (pos >= 10) break;
                    if (Character.isDigit(c)) {
                        sb.append(c);
                        if (sb.length() == 2 || sb.length() == 5) sb.append('/');
                    }
                }
                super.remove(0, getLength());
                super.insertString(0, sb.toString(), a);
            }
        };
        campo.setDocument(doc);
        return campo;
    }

    // ── INPUT HORA (auto-inserta ":") ────────────────────────────────────────
    public static JTextField crearCampoHora() {
        JTextField campo = crearCampo(5);
        campo.setToolTipText("Formato: HH:MM");

        PlainDocument doc = new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;
                String actual = getText(0, getLength());
                StringBuilder sb = new StringBuilder(actual);
                for (char c : str.toCharArray()) {
                    int pos = sb.length();
                    if (pos >= 5) break;
                    if (Character.isDigit(c)) {
                        sb.append(c);
                        if (sb.length() == 2) sb.append(':');
                    }
                }
                super.remove(0, getLength());
                super.insertString(0, sb.toString(), a);
            }
        };
        campo.setDocument(doc);
        return campo;
    }

    // ── COMBO ────────────────────────────────────────────────────────────────
public static JComboBox<String> crearCombo() {
    JComboBox<String> combo = new JComboBox<String>() {
        @Override
        public void updateUI() {
            super.updateUI();
            setUI(new javax.swing.plaf.basic.BasicComboBoxUI());
        }
    };
    combo.setBackground(BG_INPUT);
    combo.setForeground(TEXT_PRIMARY);
    combo.setFont(FONT_BODY);
    combo.setOpaque(true);
    combo.setBorder(BorderFactory.createLineBorder(BORDER, 1));

    combo.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            // Fondo oscuro siempre
            list.setBackground(BG_INPUT);
            list.setSelectionBackground(ACCENT);
            list.setSelectionForeground(TEXT_PRIMARY);
            setBackground(isSelected ? ACCENT : BG_INPUT);
            setForeground(TEXT_PRIMARY);
            setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            setFont(FONT_BODY);
            setOpaque(true);
            return this;
        }
    });
    return combo;
}


    // ── CARD ─────────────────────────────────────────────────────────────────
    public static JPanel crearCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        return card;
    }

    // ── LABELS ───────────────────────────────────────────────────────────────
    public static JLabel crearTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    public static JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FONT_BODY);
        lbl.setForeground(TEXT_SECONDARY);
        return lbl;
    }

    // ── TABLA ────────────────────────────────────────────────────────────────
    public static void estilizarTabla(JTable tabla) {
        tabla.setBackground(BG_CARD);
        tabla.setForeground(TEXT_PRIMARY);
        tabla.setFont(FONT_BODY);
        tabla.setGridColor(BORDER);
        tabla.setRowHeight(36);
        tabla.setSelectionBackground(new Color(124, 58, 237, 120));
        tabla.setSelectionForeground(TEXT_PRIMARY);
        tabla.setShowVerticalLines(false);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setBackground(new Color(15, 15, 35));
        tabla.getTableHeader().setForeground(ACCENT_LIGHT);
        tabla.getTableHeader().setFont(FONT_HEADER);
        tabla.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        tabla.setIntercellSpacing(new Dimension(0, 1));
        // Renderer oscuro por defecto
        tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setBackground(sel ? new Color(124, 58, 237, 120) : (r % 2 == 0 ? BG_CARD : new Color(25, 25, 50)));
                setForeground(TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
    }

    public static void estilizarScrollPane(JScrollPane scroll) {
        scroll.setBackground(BG_CARD);
        scroll.getViewport().setBackground(BG_CARD);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1));
    }

    // ── BOTÓN VOLVER ─────────────────────────────────────────────────────────
    public static JButton crearBotonVolver(JDialog dialog) {
        JButton btn = crearBoton("← Volver", new Color(40, 40, 70));
        btn.addActionListener(e -> dialog.dispose());
        return btn;
    }

    // ── HEADER GENÉRICO ──────────────────────────────────────────────────────
    public static JPanel crearHeader(String titulo, String subtitulo, Color gradLeft, Color accentColor, JDialog dialog) {
        JPanel h = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, gradLeft, getWidth(), 0, new Color(10, 10, 30)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        h.setPreferredSize(new Dimension(0, 72));
        h.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        JLabel lT = new JLabel(titulo);
        lT.setFont(FONT_TITLE);
        lT.setForeground(Color.WHITE);

        JLabel lS = new JLabel(subtitulo);
        lS.setFont(FONT_SMALL);
        lS.setForeground(accentColor);

        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.add(lT);
        col.add(Box.createVerticalStrut(3));
        col.add(lS);

        JButton btnVolver = crearBotonVolver(dialog);
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelVolver.setOpaque(false);
        panelVolver.add(btnVolver);

        h.add(col, BorderLayout.WEST);
        h.add(panelVolver, BorderLayout.EAST);
        return h;
    }
}
