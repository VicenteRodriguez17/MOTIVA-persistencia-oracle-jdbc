package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String HOST = "oracle.fiap.com.br";
    private static final String PORTA = "1521";
    private static final String SERVICO = "ORCL";
    private static final String USUARIO = "RM563865";
    private static final String SENHA = "090607";

    private static final String URL =
            "jdbc:oracle:thin:@//" + HOST + ":" + PORTA + "/" + SERVICO;

    private static ConexaoBD instancia;
    private Connection connection;

    private ConexaoBD() {
    }

    public static ConexaoBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoBD();
        }
        return instancia;
    }

    public Connection conectar() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conexão com o Oracle estabelecida com sucesso!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver Oracle não encontrado no classpath: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco: " + e.getMessage());
        }
        return connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void desconectar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com o Oracle encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}
