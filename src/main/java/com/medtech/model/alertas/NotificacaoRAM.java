package com.medtech.model.alertas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NotificacaoRAM {
    public static void exibirNotificacao() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

            // Criando o ícone para a bandeja do sistema
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("C:\\Users\\Beatriz Brandão\\OneDrive - SPTech School\\PI\\medTech-jar-individual\\jar-individual-BeatrizBrandao\\src\\main\\java\\org\\example\\logoMedTech.png");
            TrayIcon trayIcon = new TrayIcon(image, "MedTech");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("MedTech");
            tray.add(trayIcon);

            // Adicionando a ação de clique ao ícone da bandeja
            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Abrir a janela de confirmação
                    exibirJanelaConfirmacao();
                }
            });
        } catch (Exception ex) {
            System.err.println("Erro ao criar a notificação: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void exibirJanelaConfirmacao() {
        // Criando e configurando a janela de confirmação
        JFrame frame = new JFrame("Uso Alto da Memória - Confirmação para fechar processos");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 170);
        frame.setLocationRelativeTo(null);

        JButton button1 = new JButton("Ok");
        JButton button2 = new JButton("Negar");

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Fechar processos!");
                // Chamar função para fechar processos
                System.exit(0);
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Processo interrompido! Usuário negou confirmação.");
                System.exit(0);
            }
        });

        JLabel label = new JLabel("<html><div style='padding: 20px 15px 0 15px; text-align: justify;'>Devido à alta ocupação da memória do computador, precisaremos fechar alguns programas para melhorar o desempenho. Não se preocupe, isso não afetará o seu trabalho. Mas, primeiro precisamos da sua confirmação:</div></html>");

        frame.add(label, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.add(button1);
        panel.add(button2);
        frame.add(panel);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Processo interrompido! Janela fechada pelo usuário.");
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }
}

