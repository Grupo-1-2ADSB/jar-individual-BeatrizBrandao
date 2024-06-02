package com.medtech.dao;

import com.medtech.model.ConexaoBanco;
import com.medtech.model.componente.armazenamento.Armazenamento;
import com.medtech.model.componente.cpu.MonitoramentoCpu;
import com.medtech.model.componente.memoria.MonitoramentoMemoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.net.InetAddress;

public class ComponenteDAO {

    private final ConexaoBanco conexaoBanco;

    public ComponenteDAO() {
        this.conexaoBanco = new ConexaoBanco();
    }

    private MonitoramentoCpu cpu01 = new MonitoramentoCpu();

    private boolean temConexaoInternet() {
        try {
            InetAddress.getByName("www.google.com").isReachable(3000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Connection obterConexaoParaOperacao() throws SQLException {
        if (temConexaoInternet()) {
            return conexaoBanco.getSqlServerConexao();
        } else {
            return conexaoBanco.getMysqlConexao();
        }
    }

    private boolean computadorExiste(Connection conexao, String idComputador) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Computador WHERE idComputador = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, idComputador);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private void inserirComputadorSeNecessario(Connection conexao, String idComputador, String nomeUsuario) throws SQLException {
        if (!computadorExiste(conexao, idComputador)) {
            int fkUnidadeHospitalar = obterFkUnidadeHospitalarDoUsuario(conexao, nomeUsuario);
            String sql = "INSERT INTO Computador (idComputador, nome, localizacao, statusPC, fkUnidadeHospitalar, fkSO) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setString(1, idComputador);
                stmt.setString(2, "NomePadrao"); // Substitua com valores apropriados
                stmt.setString(3, "LocalizacaoPadrao"); // Substitua com valores apropriados
                stmt.setString(4, "ativado"); // Substitua com o status padrão desejado
                stmt.setInt(5, fkUnidadeHospitalar); // Usando o fkUnidadeHospitalar do usuário
                stmt.setInt(6, 1); // Substitua com a fkSO apropriada
                stmt.executeUpdate();
            }
        }
    }

    private void inserirRegistro(Connection conexao, double valor, String idComputador, int fkHardware) throws SQLException {
        String sql = "INSERT INTO Registro (valor, dataHora, fkComputador, fkHardware) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setDouble(1, valor);
            stmt.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
            stmt.setString(3, idComputador);
            stmt.setInt(4, fkHardware);
            stmt.executeUpdate();
        }
    }

    private void inserirComputadorSeNecessarioEmAmbos(String idComputador, String nomeUsuario) throws SQLException {
        try (Connection conexao = obterConexaoParaOperacao()) {
            inserirComputadorSeNecessario(conexao, idComputador, nomeUsuario);
        }
    }

    private void inserirRegistroEmAmbos(double valor, String idComputador, int fkHardware) throws SQLException {
        try (Connection conexao = obterConexaoParaOperacao()) {
            inserirRegistro(conexao, valor, idComputador, fkHardware);
        }
    }

    private int obterFkUnidadeHospitalarDoUsuario(Connection conexao, String nomeUsuario) throws SQLException {
        String sql = "SELECT fkUnidadeHospitalar FROM Usuario WHERE nomeUser = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nomeUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("fkUnidadeHospitalar");
                }
            }
        }
        throw new SQLException("Usuário não encontrado: " + nomeUsuario);
    }

    // Métodos de inserção

    public void inserirUsoMemoria(MonitoramentoMemoria memoria, String nomeUsuario) throws SQLException {
        inserirComputadorSeNecessarioEmAmbos(cpu01.getIdCPU(), nomeUsuario);
        inserirRegistroEmAmbos(memoria.getMemoriaEmUsoGB(), cpu01.getIdCPU(), 1); // 1 é o ID correspondente ao hardware de memória RAM
    }

    public void inserirUsoArmazenamento(Armazenamento armazenamento, String nomeUsuario) throws SQLException {
        inserirComputadorSeNecessarioEmAmbos(cpu01.getIdCPU(), nomeUsuario);
        inserirRegistroEmAmbos(armazenamento.getVolumes(), cpu01.getIdCPU(), 2); // 2 é o ID correspondente ao hardware de armazenamento
    }

    public void inserirUsoCpu(MonitoramentoCpu cpu, String nomeUsuario) throws SQLException {
        inserirComputadorSeNecessarioEmAmbos(cpu01.getIdCPU(), nomeUsuario);
        inserirRegistroEmAmbos(cpu.getCpuUsoGHz(), cpu01.getIdCPU(), 3); // 3 é o ID correspondente ao hardware de CPU
    }

    public void inserirVelocidadeRede(double velocidade, String idComputador) throws SQLException {
        inserirComputadorSeNecessarioEmAmbos(cpu01.getIdCPU(), idComputador);
        inserirRegistroEmAmbos(velocidade, cpu01.getIdCPU(), 4); // 4 é o ID correspondente ao hardware de rede
    }

}
