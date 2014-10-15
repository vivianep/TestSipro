package models;

import java.util.Date;  
import java.util.Calendar;
import java.util.GregorianCalendar;


import javax.persistence.Entity;

public class Paciente {

	private long prontuario;	// primary key
	private String nome;		// not null
	private String diagnostico1;
	private String diagnostico2;
	private String diagnostico3;
	private long telefone;		// not null
	private String sexo;			// not null
	private String procedencia;
	private String endereco;
	private boolean historico;
	private Date datanasc;
	
	
	public Paciente(long pront, String name, String diag, long tel, String s, String proc, String end, boolean hist, Date d){
		prontuario = pront;
		nome= name;
		diagnostico1 = diag;
		telefone = tel;
		sexo = s;
		procedencia = proc;
		endereco = end;
		historico = hist;
		datanasc =d;
	}


	public Paciente(){
		
	}
	
	public long getProntuario() {
		return prontuario;
	}


	public void setProntuario(long prontuario) {
		this.prontuario = prontuario;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getDiagnostico1() {
		return diagnostico1;
	}


	public void setDiagnostico1(String diagnostico1) {
		this.diagnostico1 = diagnostico1;
	}


	public String getDiagnostico2() {
		return diagnostico2;
	}


	public void setDiagnostico2(String diagnostico2) {
		this.diagnostico2 = diagnostico2;
	}


	public String getDiagnostico3() {
		return diagnostico3;
	}


	public void setDiagnostico3(String diagnostico3) {
		this.diagnostico3 = diagnostico3;
	}


	public long getTelefone() {
		return telefone;
	}


	public void setTelefone(long telefone) {
		this.telefone = telefone;
	}


	public String getSexo() {
		return sexo;
	}


	public void setSexo(String sexo) {
		this.sexo = sexo;
	}


	public String getProcedencia() {
		return procedencia;
	}


	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}


	public String getEndereco() {
		return endereco;
	}


	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}


	public boolean isHistorico() {
		return historico;
	}


	public void setHistorico(boolean historico) {
		this.historico = historico;
	}


	public Date getDatanasc() {
		return datanasc;
	}


	public void setDatanasc(Date datanasc) {
		this.datanasc = datanasc;
	}
	
		

	
}
