package com.medtech.dao;

import com.medtech.model.ConexaoBanco;
import com.medtech.model.usuario.Usuario;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;

public class UsuarioDAO {
    private final ConexaoBanco conexaoBanco;

    public UsuarioDAO() {
        this.conexaoBanco = new ConexaoBanco();
    }

    public Usuario retornaUsuario(String userVerificar, String senhaVerificar) {
        Usuario usuario = null;
        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            String query = "SELECT * FROM usuario WHERE nomeUser = ? AND senha = ?";
            usuario = jdbcTemplate.queryForObject(query, new Object[]{userVerificar, senhaVerificar}, new BeanPropertyRowMapper<>(Usuario.class));
            return usuario;
        } catch (Exception ex) {
            return null; // Falha na autenticação ou no acesso ao banco de dados
        }
    }

    private JdbcTemplate getJdbcTemplate() {
        if (isInternetAvailable()) {
            return conexaoBanco.getSqlServerJdbcTemplate();
        } else {
            return conexaoBanco.getMysqlJdbcTemplate();
        }
    }

    private boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return address.isReachable(1000);
        } catch (IOException e) {
            return false;
        }
    }
}
