
package br.com.fraudekafka.database;

import br.com.fraudekafka.model.Transacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RepositorioTransacao {

    public void salvar(Transacao transacao, boolean fraude, String tipoFraude)
            throws SQLException {

        String sql = """
                INSERT INTO transacoes
                (id, cliente_id, valor, cidade, data_hora, fraude, tipo_fraude)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, transacao.getId());
            comando.setString(2, transacao.getClienteId());
            comando.setDouble(3, transacao.getValor());
            comando.setString(4, transacao.getCidade());
            comando.setTimestamp(
                    5,
                    java.sql.Timestamp.valueOf(transacao.getDataHora())
            );
            comando.setBoolean(6, fraude);
            comando.setString(7, tipoFraude);

            comando.executeUpdate();
        }
    }
}
