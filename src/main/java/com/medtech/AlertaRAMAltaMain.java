package com.medtech;

import com.github.britooo.looca.api.group.memoria.Memoria;
import com.medtech.model.alertas.NotificacaoRAM;
import com.medtech.model.componente.memoria.MonitoramentoMemoria;

public class alertaRAMAltaMain {
    private static final double VALOR_MEMORIA_ALTA = 0.1;

    public static void main(String[] args) {
        MonitoramentoMemoria mon1 = new MonitoramentoMemoria();

        Memoria memoria = mon1.exibeMemoria();

        double limiteMemoriaAlta = memoria.getTotal() * VALOR_MEMORIA_ALTA;

        if (memoria.getEmUso() > limiteMemoriaAlta) {
            double porcentagemEmUso = (double) memoria.getEmUso() / memoria.getTotal() * 100;

            System.out.println("ALERTA: Uso de memória alto!");
            System.out.printf("Porcentagem de memória em uso: %.2f%%\n", porcentagemEmUso);

            NotificacaoRAM.exibirJanelaConfirmacao();
        } else {
            System.out.println("Uso de memória dentro do limite.");
        }
    }
}
