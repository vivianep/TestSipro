package models;

public class Medico {

	private long crm;				// primary key
	private String nome;			// not null
	private String especializacao;	// not null
	private String departamento;	// not null
	private String email;			// not null unique
	private String senha;			// not null
	private long telefone;			// not null
	private String justificativa;	
	private boolean admin;
	

	public long getCrm() {
		return crm;
	}
	public void setCrm(long crm) {
		this.crm = crm;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEspecializacao() {
		return especializacao;
	}
	public void setEspecializacao(String especializacao) {
		this.especializacao = especializacao;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public long getTelefone() {
		return telefone;
	}
	public void setTelefone(long telefone) {
		this.telefone = telefone;
	}
	public String getJustificativa() {
		return justificativa;
	}
	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	
	public void setAdmin(boolean administrador){
		this.admin = administrador;
	}
	
	public boolean getAdmin(){
		return admin;
	}
	
}
