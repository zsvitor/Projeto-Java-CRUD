public class Funcionario {

	private int id;
	private String nome;
	private String cpf;
	private String rg;
	private String cargo;
	private double salario;
	private String setor;

	public Funcionario(int id, String nome, String cpf, String rg, String cargo, double salario, String setor) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.rg = rg;
		this.cargo = cargo;
		this.salario = salario;
		this.setor = setor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	@Override
	public String toString() {
		return "ID: " + id + " | Nome: " + nome + " | CPF: " + formatarCPF(cpf) + " | RG: " + formatarRG(rg)
				+ " | Cargo: " + cargo + " | Salário: " + String.format("R$ %.2f", salario) + " | Setor: " + setor;
	}

	private String formatarCPF(String cpf) {
		if (cpf != null && cpf.matches("\\d{11}")) {
			return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
		}
		return cpf;
	}

	private String formatarRG(String rg) {
		if (rg != null && rg.matches("\\d{9}")) {
			return rg.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{1})", "$1.$2.$3-$4");
		}
		return rg;
	}

}