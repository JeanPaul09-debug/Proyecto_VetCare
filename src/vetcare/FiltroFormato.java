package vetcare;

import javax.swing.*;
import javax.swing.text.*;

public class FiltroFormato extends DocumentFilter {

    public enum Tipo { FECHA, HORA }

    private final Tipo tipo;

    public FiltroFormato(Tipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset, text);
        aplicar(fb, sb.toString());
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.replace(offset, offset + length, text == null ? "" : text);
        aplicar(fb, sb.toString());
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.delete(offset, offset + length);
        aplicar(fb, sb.toString());
    }

    private void aplicar(FilterBypass fb, String raw) throws BadLocationException {
        // Solo dígitos
        String digits = raw.replaceAll("[^0-9]", "");
        String formatted;

        if (tipo == Tipo.FECHA) {
            // Máximo 8 dígitos → DD/MM/AAAA
            if (digits.length() > 8) digits = digits.substring(0, 8);
            formatted = formatearFecha(digits);
        } else {
            // Máximo 4 dígitos → HH:MM
            if (digits.length() > 4) digits = digits.substring(0, 4);
            formatted = formatearHora(digits);
        }

        fb.replace(0, fb.getDocument().getLength(), formatted, null);
    }

    private String formatearFecha(String d) {
        if (d.length() <= 2) return d;
        if (d.length() <= 4) return d.substring(0, 2) + "/" + d.substring(2);
        return d.substring(0, 2) + "/" + d.substring(2, 4) + "/" + d.substring(4);
    }

    private String formatearHora(String d) {
        if (d.length() <= 2) return d;
        return d.substring(0, 2) + ":" + d.substring(2);
    }

    // ── Método utilitario para aplicar el filtro a un JTextField ──
    public static void aplicarFecha(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new FiltroFormato(Tipo.FECHA));
        campo.setToolTipText("Formato automático: DD/MM/AAAA");
    }

    public static void aplicarHora(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new FiltroFormato(Tipo.HORA));
        campo.setToolTipText("Formato automático: HH:MM");
    }
}
