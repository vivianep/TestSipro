package models;

import java.util.Date;

public class Consulta {

	private String observacao;
	private Date data;
	private Long crm;
	private Long prontuario;
	
	
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Long getCrm() {
		return crm;
	}
	public void setCrm(Long crm) {
		this.crm = crm;
	}
	public Long getProntuario() {
		return prontuario;
	}
	public void setProntuario(Long prontuario) {
		this.prontuario = prontuario;
	}
	
	
	
}
