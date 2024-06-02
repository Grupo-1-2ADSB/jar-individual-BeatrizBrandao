package com.medtech.model.notificacaoUsuario;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Scanner;

public class NotificacaoRAMAlta {
    public static boolean exibirNotificacao() {
        boolean response = false;

        try {
            String osName = System.getProperty("os.name").toLowerCase();

            if (osName.contains("windows")) {
                // Carregar a imagem da logo a partir do recurso
                URL imageUrl = NotificacaoRAMAlta.class.getClassLoader().getResource("logoMedTech.png");
                if (imageUrl == null) {
                    throw new RuntimeException("Imagem não encontrada: image/logoMedTech.png");
                }

                // Carregar a imagem e redimensioná-la
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image originalImage = originalIcon.getImage();
                Image resizedImage = originalImage.getScaledInstance(90, 90, Image.SCALE_SMOOTH); // Definir o tamanho desejado aqui
                ImageIcon resizedIcon = new ImageIcon(resizedImage);

                // Exibir notificação na GUI
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                int option = JOptionPane.showConfirmDialog(null,
                        "<html><div style='padding: 5px 15px 0 0; width: 300px; text-align: justify;'>Devido à alta ocupação da memória do computador, precisaremos fechar alguns programas para melhorar o desempenho. Não se preocupe, isso não afetará o seu trabalho. Deseja fechar os programas agora?</div></html>",
                        "MedTech - Alerta de Alto Uso de Memória",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        resizedIcon
                );

                response = option == JOptionPane.YES_OPTION;
            } else {
                // Exibir notificação no terminal
                System.out.println("Uso alto de memória detectado.");
                System.out.println("Devido à alta ocupação da memória do computador, precisaremos fechar alguns programas para melhorar o desempenho.");
                System.out.println("Não se preocupe, isso não afetará o seu trabalho.");

                // Aguardando a resposta do usuário no terminal
                System.out.println("Você deseja fechar os programas agora? (sim/não):");
                Scanner scanner = new Scanner(System.in);
                String terminalResponse = scanner.nextLine().toLowerCase();
                response = terminalResponse.equals("sim") || terminalResponse.equals("s");
            }
        } catch (Exception ex) {
            System.err.println("Erro ao criar a notificação: " + ex.getMessage());
            ex.printStackTrace();
        }

        if (!response) {
            System.out.println("Execução interrompida pelo usuário.");
            System.exit(0);
        }

        return response;
    }
}
