package org.example;

import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationExample {
    public static void main(String[] args) throws AWTException {
        try {
            SystemTray tray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit().createImage("C:\\Users\\Beatriz Brandão\\OneDrive - SPTech School\\PI\\testeEnvioDeNotificacaoJarIndi\\src\\main\\java\\org\\example\\logoMedTech.png");

            TrayIcon trayIcon = new TrayIcon(image, "Java AWT Tray Demo");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("System tray icon demo");

            tray.add(trayIcon);

            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://localhost:3333/login.html"));
                    } catch (Exception ex) {
                        System.err.println("Erro ao abrir a página da web: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });

            trayIcon.displayMessage("Hello, World", "Java Notification Demo", TrayIcon.MessageType.WARNING);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            }, 10000);
        } catch (Exception ex) {
            System.err.println("Erro ao criar a notificação: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
