package utils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ThemeManager {

    public static boolean darkMode = false;

    public static void applyLightTheme(JPanel leftPanel, JTextArea area, List<JButton> buttons, JFrame frame) {
        Color bg = new Color(240, 240, 240);
        Color btnBg = Color.WHITE;
        Color text = Color.BLACK;

        leftPanel.setBackground(bg);
        area.setBackground(Color.WHITE);
        area.setForeground(text);

        for (JButton btn : buttons) {
            btn.setBackground(btnBg);
            btn.setForeground(text);
        }

        frame.getContentPane().setBackground(bg);
    }

    public static void applyDarkTheme(JPanel leftPanel, JTextArea area, List<JButton> buttons, JFrame frame) {
        Color bg = new Color(45, 45, 45);
        Color btnBg = new Color(65, 65, 65);
        Color text = Color.WHITE;

        leftPanel.setBackground(bg);
        area.setBackground(new Color(30, 30, 30));
        area.setForeground(text);

        for (JButton btn : buttons) {
            btn.setBackground(btnBg);
            btn.setForeground(text);
        }

        frame.getContentPane().setBackground(bg);
    }

    public static void toggleTheme(JPanel leftPanel, JTextArea area, List<JButton> buttons, JFrame frame) {
        darkMode = !darkMode;
        if (darkMode) applyDarkTheme(leftPanel, area, buttons, frame);
        else applyLightTheme(leftPanel, area, buttons, frame);
    }
}
