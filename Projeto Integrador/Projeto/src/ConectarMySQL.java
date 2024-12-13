import java.sql.*;

public class ConectarMySQL {

	private final static String url = "jdbc:mysql://localhost:3306/sistemaRH";
	private final static String username = "root";
	private final static String password = "1234";

	private static Connection con;
	private ResultSet rs;

	public static void main(String args[]) {
		ConectarMySQL b = new ConectarMySQL();
		b.openDB();
		b.closeDB();
	}

	public static Connection openDB() {
		try {
			con = DriverManager.getConnection(url, username, password);
			con.createStatement();
		} catch (Exception e) {
			System.out.println("\nNão foi possível estabelecer conexão " + e + "\n");
			System.exit(1);
		}
		return con;
	}

	public void closeDB() {
		try {
			con.close();
		} catch (Exception e) {
			System.out.println("\nNão foi possível fechar conexão " + e + "\n");
			System.exit(1);
		}
	}

	public void closeDB(Connection cn, Statement st, ResultSet rs2) throws SQLException {
		if (cn != null) {
			cn.close();
		}
		if (st != null) {
			st.close();
		}
		if (rs != null) {
			rs.close();
		}
	}

	public void closeDB(Statement st, ResultSet rs2) throws SQLException {
		if (st != null) {
			st.close();
		}
		if (rs != null) {
			rs.close();
		}
	}

}

/*
Banco de dados utilizado:

CREATE SCHEMA sistemaRH;

USE sistemaRH;

CREATE TABLE Funcionario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    rg VARCHAR(9) UNIQUE NOT NULL,
    cargo VARCHAR(30) NOT NULL,
    salario DECIMAL(10, 2) NOT NULL,
    setor VARCHAR(30) NOT NULL
);

INSERT INTO Funcionario (nome, cpf, rg, cargo, salario, setor) VALUES
('Ana Silva', '12345678901', '123456789', 'Analista de Recursos Humanos', 4500.00, 'Recursos Humanos'),
('Bruno Almeida', '23456789012', '234567890', 'Assistente Administrativo', 3200.00, 'Administração'),
('Carla Souza', '34567890123', '345678901', 'Desenvolvedor de Software', 6000.00, 'TI'),
('Diego Ferreira', '45678901234', '456789012', 'Gerente de Projetos', 7800.00, 'Projetos'),
('Evelyn Martins', '56789012345', '567890123', 'Auxiliar Financeiro', 2900.00, 'Financeiro');
*/