import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaPrincipal extends JFrame implements ActionListener {

	private JLabel label;
	private JFrame cadastroFrame, alteracaoFrame;
	private JPanel painelInputs, painelCentral, painelBotoes, painelCadastro, painelAlteracao, inputs;
	private JTextField txtId, txtNome, txtCpf, txtRg, txtCargo, txtSalario, txtSetor, txtNome2, txtCpf2, txtRg2,
			txtCargo2, txtSalario2, txtSetor2;
	private JButton btnPrimeiro, btnAnterior, btnProximo, btnUltimo, btnCadastrar, btnAlterar, btnExcluir, btnImprimir,
			btnCadastrarFuncionario, btnAlterarFuncionario, btnLimpar, btnReverter, botao;
	private String nomeAtual, cpfAtual, rgAtual, cargoAtual, salarioAtual, setorAtual;

	private ArrayList<Funcionario> funcionarios;
	private int i = 0;
	private ConectarMySQL conexao;

	public static void main(String[] args) {
		new TelaPrincipal();
	}

	public TelaPrincipal() {
		super("Sistema de RH | Gerenciamento");
		conexao = new ConectarMySQL();
		funcionarios = new ArrayList<>();

		setLayout(new BorderLayout());
		setResizable(false);
		inicializarComponentes();
		adicionarPaineis();
		configurarJanela();
		carregarFuncionarios();
	}

	// Métodos de Inicialização
	private void adicionarPaineis() {
		painelInputs = criarPainelInputs();

		painelCentral = new JPanel(new BorderLayout());
		painelCentral.add(painelInputs, BorderLayout.NORTH);
		painelCentral.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
		painelCentral.setBackground(new Color(65, 105, 225));

		painelBotoes = criarPainelBotoes();
		painelCentral.add(painelBotoes, BorderLayout.SOUTH);

		add(painelCentral, BorderLayout.CENTER);
	}

	private JPanel criarPainelBotoes() {
		painelBotoes = new JPanel(new GridLayout(2, 4, 2, 2));
		painelBotoes.add(btnPrimeiro);
		painelBotoes.add(btnAnterior);
		painelBotoes.add(btnProximo);
		painelBotoes.add(btnUltimo);
		painelBotoes.add(btnCadastrar);
		painelBotoes.add(btnAlterar);
		painelBotoes.add(btnExcluir);
		painelBotoes.add(btnImprimir);
		painelBotoes.setBackground(new Color(65, 105, 225));
		return painelBotoes;
	}

	private void configurarJanela() {
		((JComponent) getContentPane())
				.setBorder(BorderFactory.createMatteBorder(15, 15, 15, 15, new Color(65, 105, 225)));
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void inicializarComponentes() {
		txtId = new JTextField(5);
		txtNome = new JTextField(15);
		txtCpf = new JTextField(15);
		txtRg = new JTextField(15);
		txtCargo = new JTextField(15);
		txtSalario = new JTextField(15);
		txtSetor = new JTextField(15);

		btnPrimeiro = criarBotao("Primeiro");
		btnAnterior = criarBotao("Anterior");
		btnProximo = criarBotao("Próximo");
		btnUltimo = criarBotao("Último");
		btnCadastrar = criarBotao("Cadastrar");
		btnAlterar = criarBotao("Alterar");
		btnExcluir = criarBotao("Excluir");
		btnImprimir = criarBotao("Imprimir");
	}

	// Métodos de Criação dos Painéis
	private JPanel criarPainelInputs() {
		inputs = new JPanel(new GridLayout(7, 2, 2, 2));
		inputs.setBackground(new Color(65, 105, 225));
		adicionarLabelsETextFields(inputs);
		return inputs;
	}

	private void adicionarLabelsETextFields(JPanel panel) {
		panel.add(criarLabel("ID:"));
		panel.add(txtId);
		panel.add(criarLabel("NOME:"));
		panel.add(txtNome);
		panel.add(criarLabel("CPF:"));
		panel.add(txtCpf);
		panel.add(criarLabel("RG:"));
		panel.add(txtRg);
		panel.add(criarLabel("CARGO:"));
		panel.add(txtCargo);
		panel.add(criarLabel("SALÁRIO:"));
		panel.add(txtSalario);
		panel.add(criarLabel("SETOR:"));
		panel.add(txtSetor);
	}

	private JLabel criarLabel(String texto) {
		label = new JLabel(texto);
		label.setForeground(Color.WHITE);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		return label;
	}

	private JButton criarBotao(String texto) {
		botao = new JButton(texto);
		botao.addActionListener(this);
		botao.setForeground(Color.WHITE);
		botao.setBackground(Color.BLACK);
		return botao;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnPrimeiro) {
			mostrarPrimeiro();
		} else if (e.getSource() == btnAnterior) {
			mostrarAnterior();
		} else if (e.getSource() == btnProximo) {
			mostrarProximo();
		} else if (e.getSource() == btnUltimo) {
			mostrarUltimo();
		} else if (e.getSource() == btnCadastrar) {
			cadastrarFuncionario();
		} else if (e.getSource() == btnAlterar) {
			alterarFuncionario();
		} else if (e.getSource() == btnExcluir) {
			excluirFuncionario();
		} else if (e.getSource() == btnImprimir) {
			imprimirFuncionarios();
		}
	}

	// Métodos de Exibição
	private void carregarFuncionarios() {
		try {
			Connection con = conexao.openDB();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM Funcionario");
			while (rs.next()) {
				funcionarios.add(new Funcionario(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"),
						rs.getString("rg"), rs.getString("cargo"), rs.getDouble("salario"), rs.getString("setor")));
			}
			if (funcionarios.size() > 0) {
				mostrarPrimeiro();
			}
			conexao.closeDB(con, st, rs);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Erro ao carregar os funcionários: " + e.getMessage());
		}
	}

	private void mostrarPrimeiro() {
		if (funcionarios.size() > 0) {
			i = 0;
			mostrarFuncionario(i);
		}
	}

	private void mostrarAnterior() {
		if (i > 0) {
			i--;
			mostrarFuncionario(i);
		}
	}

	private void mostrarProximo() {
		if (i < funcionarios.size() - 1) {
			i++;
			mostrarFuncionario(i);
		}
	}

	private void mostrarUltimo() {
		if (funcionarios.size() > 0) {
			i = funcionarios.size() - 1;
			mostrarFuncionario(i);
		}
	}

	private void mostrarFuncionario(int i) {
		Funcionario fun = funcionarios.get(i);
		txtId.setText(String.valueOf(fun.getId()));
		txtNome.setText(fun.getNome());
		txtCpf.setText(fun.getCpf());
		txtRg.setText(fun.getRg());
		txtCargo.setText(fun.getCargo());
		txtSalario.setText(fun.getSalario() + "");
		txtSetor.setText(fun.getSetor());
		
		txtId.setEditable(false);
		txtNome.setEditable(false);
		txtCpf.setEditable(false);
		txtRg.setEditable(false);
		txtCargo.setEditable(false);
		txtSalario.setEditable(false);
		txtSetor.setEditable(false);
	}

	private void limparCampos() {
		if (txtNome2 != null) {
			txtNome2.setText("");
		}
		if (txtCpf2 != null) {
			txtCpf2.setText("");
		}
		if (txtRg2 != null) {
			txtRg2.setText("");
		}
		if (txtCargo2 != null) {
			txtCargo2.setText("");
		}
		if (txtSalario2 != null) {
			txtSalario2.setText("");
		}
		if (txtSetor2 != null) {
			txtSetor2.setText("");
		}
	}

	private void reverterCampos() {
		txtNome2.setText(nomeAtual);
		txtCpf2.setText(cpfAtual);
		txtRg2.setText(rgAtual);
		txtCargo2.setText(cargoAtual);
		txtSalario2.setText(salarioAtual);
		txtSetor2.setText(setorAtual);
	}

	// Métodos do CRUD
	private void cadastrarFuncionario() {
		cadastroFrame = new JFrame("Cadastro de Funcionário");
		cadastroFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		cadastroFrame.setSize(400, 300);
		cadastroFrame.setLocationRelativeTo(null);
		cadastroFrame.setResizable(false);

		painelCadastro = new JPanel(new GridLayout(7, 2, 2, 2));
		painelCadastro.setBackground(new Color(65, 105, 225));
		painelCadastro.setBorder(new EmptyBorder(15, 15, 15, 15));

		txtNome2 = new JTextField();
		txtCpf2 = new JTextField();
		txtRg2 = new JTextField();
		txtCargo2 = new JTextField();
		txtSalario2 = new JTextField();
		txtSetor2 = new JTextField();

		btnLimpar = new JButton("Limpar");
		btnLimpar.setForeground(Color.WHITE);
		btnLimpar.setBackground(Color.RED);
		btnLimpar.addActionListener(e -> limparCampos());

		btnCadastrarFuncionario = criarBotao("Cadastrar");
		btnCadastrarFuncionario.setForeground(Color.WHITE);
		btnCadastrarFuncionario.setBackground(Color.BLACK);
		btnCadastrarFuncionario.addActionListener(e -> {
			try {
				String nome = txtNome2.getText();
				String cpf = txtCpf2.getText();
				String rg = txtRg2.getText();
				String cargo = txtCargo2.getText();
				double salario = Double.parseDouble(txtSalario2.getText());
				String setor = txtSetor2.getText();

				if (!validarCPF(cpf) || !validarRG(rg)) {
					return;
				}

				Connection con = conexao.openDB();
				String sql = "INSERT INTO Funcionario (nome, cpf, rg, cargo, salario, setor) VALUES (?, ?, ?, ?, ?, ?)";
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, nome);
				ps.setString(2, cpf);
				ps.setString(3, rg);
				ps.setString(4, cargo);
				ps.setDouble(5, salario);
				ps.setString(6, setor);
				ps.executeUpdate();

				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					funcionarios.add(new Funcionario(id, nome, cpf, rg, cargo, salario, setor));
					JOptionPane.showMessageDialog(cadastroFrame, "Funcionário cadastrado com sucesso!");
				}
				conexao.closeDB(con, ps, rs);
				cadastroFrame.dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(cadastroFrame, "Erro ao cadastrar: " + ex.getMessage());
			}
		});

		painelCadastro.add(criarLabel("NOME:"));
		painelCadastro.add(txtNome2);
		painelCadastro.add(criarLabel("CPF:"));
		painelCadastro.add(txtCpf2);
		painelCadastro.add(criarLabel("RG:"));
		painelCadastro.add(txtRg2);
		painelCadastro.add(criarLabel("CARGO:"));
		painelCadastro.add(txtCargo2);
		painelCadastro.add(criarLabel("SALÁRIO:"));
		painelCadastro.add(txtSalario2);
		painelCadastro.add(criarLabel("SETOR:"));
		painelCadastro.add(txtSetor2);

		painelCadastro.add(btnLimpar);
		painelCadastro.add(btnCadastrarFuncionario);

		cadastroFrame.add(painelCadastro);
		cadastroFrame.setVisible(true);
	}

	private void alterarFuncionario() {
		alteracaoFrame = new JFrame("Alterar Funcionário");
		alteracaoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		alteracaoFrame.setSize(400, 300);
		alteracaoFrame.setLocationRelativeTo(null);
		alteracaoFrame.setResizable(false);

		painelAlteracao = new JPanel(new GridLayout(8, 2, 2, 2));
		painelAlteracao.setBackground(new Color(65, 105, 225));
		painelAlteracao.setBorder(new EmptyBorder(15, 15, 15, 15));

		Funcionario funcionario = funcionarios.get(i);
		txtNome2 = new JTextField(funcionario.getNome());
		txtCpf2 = new JTextField(funcionario.getCpf());
		txtCpf2.setEditable(false);
		txtRg2 = new JTextField(funcionario.getRg());
		txtRg2.setEditable(false);
		txtCargo2 = new JTextField(funcionario.getCargo());
		txtSalario2 = new JTextField(String.valueOf(funcionario.getSalario()));
		txtSetor2 = new JTextField(funcionario.getSetor());

		nomeAtual = funcionario.getNome();
		cpfAtual = funcionario.getCpf();
		rgAtual = funcionario.getRg();
		cargoAtual = funcionario.getCargo();
		salarioAtual = String.valueOf(funcionario.getSalario());
		setorAtual = funcionario.getSetor();

		btnLimpar = new JButton("Limpar");
		btnLimpar.setForeground(Color.WHITE);
		btnLimpar.setBackground(Color.RED);
		btnLimpar.addActionListener(e -> limparCampos());

		btnReverter = new JButton("Reverter");
		btnReverter.setForeground(Color.WHITE);
		btnReverter.setBackground(new Color(0, 100, 0));
		btnReverter.addActionListener(e -> reverterCampos());

		btnAlterarFuncionario = criarBotao("Alterar");
		btnAlterarFuncionario.setForeground(Color.WHITE);
		btnAlterarFuncionario.setBackground(Color.BLACK);
		btnAlterarFuncionario.addActionListener(e -> {
			try {
				String nome = txtNome2.getText();
				String cpf = txtCpf2.getText();
				String rg = txtRg2.getText();
				String cargo = txtCargo2.getText();
				double salario = Double.parseDouble(txtSalario2.getText());
				String setor = txtSetor2.getText();

				Connection con = conexao.openDB();
				String sql = "UPDATE Funcionario SET nome=?, cpf=?, rg=?, cargo=?, salario=?, setor=? WHERE id=?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, nome);
				ps.setString(2, cpf);
				ps.setString(3, rg);
				ps.setString(4, cargo);
				ps.setDouble(5, salario);
				ps.setString(6, setor);
				ps.setInt(7, funcionario.getId());
				ps.executeUpdate();

				funcionario.setNome(nome);
				funcionario.setCpf(cpf);
				funcionario.setRg(rg);
				funcionario.setCargo(cargo);
				funcionario.setSalario(salario);
				funcionario.setSetor(setor);

				JOptionPane.showMessageDialog(alteracaoFrame, "Funcionário alterado com sucesso!");
				conexao.closeDB(con, ps, null);
				alteracaoFrame.dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(alteracaoFrame, "Erro ao alterar: " + ex.getMessage());
			}
		});

		painelAlteracao.add(criarLabel("NOME:"));
		painelAlteracao.add(txtNome2);
		painelAlteracao.add(criarLabel("CPF:"));
		painelAlteracao.add(txtCpf2);
		painelAlteracao.add(criarLabel("RG:"));
		painelAlteracao.add(txtRg2);
		painelAlteracao.add(criarLabel("CARGO:"));
		painelAlteracao.add(txtCargo2);
		painelAlteracao.add(criarLabel("SALÁRIO:"));
		painelAlteracao.add(txtSalario2);
		painelAlteracao.add(criarLabel("SETOR:"));
		painelAlteracao.add(txtSetor2);

		painelAlteracao.add(btnLimpar);
		painelAlteracao.add(btnAlterarFuncionario);
		painelAlteracao.add(btnReverter);

		alteracaoFrame.add(painelAlteracao);
		alteracaoFrame.setVisible(true);
	}

	private void excluirFuncionario() {
		try {
			Funcionario fun = funcionarios.get(i);
			int resposta = JOptionPane.showConfirmDialog(this, "Deseja excluir o funcionário: " + fun.getNome() + "?",
					"Confirmação", JOptionPane.YES_NO_OPTION);
			if (resposta == JOptionPane.YES_OPTION) {
				Connection con = conexao.openDB();
				PreparedStatement ps = con.prepareStatement("DELETE FROM Funcionario WHERE id = ?");
				ps.setInt(1, fun.getId());
				ps.executeUpdate();

				funcionarios.remove(i);
				if (i > 0) {
					i--;
				}
				if (funcionarios.size() > 0) {
					mostrarFuncionario(i);
				} else {
					limparCampos();
				}

				JOptionPane.showMessageDialog(this, "Funcionário excluído com sucesso!");
				conexao.closeDB(ps, null);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Erro ao excluir o funcionário: " + e.getMessage());
		}
	}

	private void imprimirFuncionarios() {
		StringBuilder sb = new StringBuilder();
		for (Funcionario fun : funcionarios) {
			sb.append(fun.toString()).append("\n\n");
		}
		JOptionPane.showMessageDialog(this, sb.toString());
	}

	// Métodos de Validação do CPF e RG
	public static boolean validarCPF(String cpf) {
		if (cpf == null || cpf.length() != 11 || !somenteDigitos(cpf)) {
			JOptionPane.showMessageDialog(null, "CPF deve ter 11 dígitos numéricos.");
			return false;
		}
		int[] numeros = { 10, 9, 8, 7, 6, 5, 4, 3, 2 };
		int digito1 = calcularDigito(cpf.substring(0, 9), numeros);
		int digito2 = calcularDigito(cpf.substring(0, 9) + digito1, new int[] { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 });
		String cpfCalculado = cpf.substring(0, 9) + digito1 + digito2;
		if (!cpf.equals(cpfCalculado)) {
			JOptionPane.showMessageDialog(null, "CPF Inválido: dígitos não conferem.");
			return false;
		}
		return true;
	}

	public static boolean validarRG(String rg) {
		if (rg == null || rg.length() != 9 || !somenteDigitos(rg)) {
			JOptionPane.showMessageDialog(null, "RG deve ter 9 dígitos numéricos.");
			return false;
		}
		int[] numeros = { 2, 3, 4, 5, 6, 7, 8, 9 };
		int soma = 0;
		for (int i = 0; i < 8; i++) {
			soma += (rg.charAt(i) - '0') * numeros[i];
		}
		int digitoVerificador = 11 - (soma % 11);
		if (digitoVerificador == 10) {
			digitoVerificador = 0;
		} else if (digitoVerificador == 11) {
			digitoVerificador = 1;
		}
		if (rg.charAt(8) - '0' != digitoVerificador) {
			JOptionPane.showMessageDialog(null, "RG Inválido: dígito não confere.");
			return false;
		}
		return true;
	}

	public static boolean somenteDigitos(String str) {
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch < '0' || ch > '9') {
				return false;
			}
		}
		return true;
	}

	public static int calcularDigito(String str, int[] pesos) {
		int soma = 0;
		for (int i = 0; i < str.length(); i++) {
			soma += (str.charAt(i) - '0') * pesos[i];
		}
		int resto = soma % 11;
		if (resto < 2) {
			return 0;
		} else {
			return 11 - resto;
		}
	}

}