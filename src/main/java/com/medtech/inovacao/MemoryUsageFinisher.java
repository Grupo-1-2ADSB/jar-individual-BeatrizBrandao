package com.medtech.inovacao;

import com.medtech.model.notificacaoUsuario.NotificacaoRAMAlta;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemoryUsageFinisher {

    private static final double ACCEPTABLE_MEMORY_USAGE_PERCENTAGE = 20.0;
    private static final List<String> ESSENTIAL_PROCESSES = Arrays.asList(
            "mysqld.exe", "MySQLWorkbench.exe", "System", "java.exe", "idea64.exe", "smss.exe", "csrss.exe", "wininit.exe", "services.exe", "lsass.exe", "lsm.exe", "svchost.exe", "winlogon.exe", "explorer.exe", "Windows Explorer", "taskhostw.exe", "taskbar.exe", "Taskbar", "shellExperienceHost.exe", "dwm.exe", "Desktop Window Manager", "POWERPNT.EXE", "Microsoft PowerPoint Background Task Handler", "OfficeService.exe"
    );

    public static void checkMemoryUsage() {
        double memoryUsage = getSystemMemoryUsage();

        if (memoryUsage > ACCEPTABLE_MEMORY_USAGE_PERCENTAGE) {
            finishHighMemoryProcesses();
        }
    }

    private static double getSystemMemoryUsage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        long totalMemory = 0;
        long freeMemory = 0;

        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
            totalMemory = sunOsBean.getTotalPhysicalMemorySize();
            freeMemory = sunOsBean.getFreePhysicalMemorySize();
        }

        long usedMemory = totalMemory - freeMemory;
        return (double) usedMemory / totalMemory * 100;
    }

    private static void finishHighMemoryProcesses() {
        try {
            if (!NotificacaoRAMAlta.exibirNotificacao()) {
                System.out.println("Usuário negou confirmação. Abortando a finalização de processos.");
                return;
            }

            List<ProcessInfo> processes = new ArrayList<>();
            Process process = Runtime.getRuntime().exec("tasklist /FO CSV /NH");
            Scanner scanner = new Scanner(process.getInputStream());
            scanner.useDelimiter("\n");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(",");
                if (tokens.length >= 5) {
                    String processName = tokens[0].replaceAll("\"", "");
                    String memoryUsageStr = tokens[4].replaceAll("\"", "").replaceAll(" K", "");

                    Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
                    Matcher matcher = pattern.matcher(memoryUsageStr);

                    if (matcher.find()) {
                        double memoryUsageMB = Double.parseDouble(matcher.group()) / 1024.0;
                        processes.add(new ProcessInfo(processName, memoryUsageMB));
                    }
                }
            }

            processes.sort(Comparator.comparingDouble(ProcessInfo::getMemoryUsage).reversed());

            System.out.println("Uso da memória acima do limite aceitável! Começando finalização preventiva de processos...");
            System.out.println();

            int limiteDaLeva = Math.min(10, processes.size());

            for (int i = 0; i < limiteDaLeva; i++) {
                String processName = processes.get(i).getProcessName();

                if (isEssentialProcess(processName)) {
                    System.out.println("Processo essencial encontrado: " + processName + ". Ignorando.");
                    continue;
                }

                System.out.println("Encerrando o processo: " + processName);
                finishProcessByName(processName);
            }

            process.waitFor();
        } catch (Exception e) {
            System.err.println("Erro ao verificar os processos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isEssentialProcess(String processName) {
        return ESSENTIAL_PROCESSES.contains(processName);
    }

    private static void finishProcessByName(String processName) {
        try {
            Process process = Runtime.getRuntime().exec("taskkill /F /IM " + processName);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
