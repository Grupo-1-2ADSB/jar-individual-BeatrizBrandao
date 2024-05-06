package com.medtech;

import com.github.britooo.looca.api.group.memoria.Memoria;
import com.medtech.model.alertas.NotificacaoRAM;
import com.medtech.model.componente.memoria.MonitoramentoMemoria;
import com.medtech.model.mensagemSlack.ChatGeralAvisoRAMAlta;

public class AlertaRAMAltaMain {
    private static final double VALOR_MEMORIA_ALTA = 0.4;
    private static final double VALOR_MEMORIA_CRITICA = 0.9;

    public static void main(String[] args) {
        MonitoramentoMemoria mon1 = new MonitoramentoMemoria();

        Memoria memoria = mon1.exibeMemoria();

        double limiteMemoriaAlta = memoria.getTotal() * VALOR_MEMORIA_ALTA;
        double limiteMemoriaCritica = memoria.getTotal() * VALOR_MEMORIA_CRITICA;

        double porcentagemEmUso = (double) memoria.getEmUso() / memoria.getTotal() * 100;

        if (memoria.getEmUso() >= limiteMemoriaAlta && memoria.getEmUso() < limiteMemoriaCritica) {

            System.out.println("ALERTA: Uso de memória alto!");
            System.out.printf("Porcentagem de memória em uso: %.2f%%\n", porcentagemEmUso);

            NotificacaoRAM.exibirJanelaConfirmacao();
        } else if (memoria.getEmUso() >= limiteMemoriaCritica) {
            System.out.println("ALERTA: Uso de memória está critíco!");
            System.out.printf("Porcentagem de memória em uso: %.2f%%\n", porcentagemEmUso);

            ChatGeralAvisoRAMAlta.enviarMensagem();
        } else {
            System.out.println("Uso de memória dentro do limite.");
            System.out.printf("Porcentagem de memória em uso: %.2f%%\n", porcentagemEmUso);
        }
    }
}
