
package br.com.fraudekafka.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/fraudedb";

    private static final String USUARIO = "frauduser";
    private static final String SENHA = "fraudpass";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
