package controllerTests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.Paciente;

import org.apache.commons.mail.EmailException;
import org.junit.Before;
import org.junit.Test;

import controllers.Application;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationTest {

	@Test // Teste valido consultar ficha de paciente
	public void testaConsultarPacientesValido() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.buscarPacientes("Viviane", null, 0, 1, 1990);//Único campo que influencia no resultado eh o da data
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
	}

	@Test// Teste invalido consultar ficha de paciente algum campo da data é nulo
	public void testaConsultarPacientesInvalido() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.buscarPacientes("", null, null, 1, 1990);//Quando a variável dia é vazia 
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.buscarPacientes("", null, 3, null, 1990);//Quando a variável mes é vazia 
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.buscarPacientes("", null, 3, 1, null);//Quando a variável ano é vazia 
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
	}


	@Test//solicita uma busca por um paciente que está cadastrado, fornecendo seu número de identificação
	public void buscarPacienteTrue() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste=true;
		long prontuario =10;
		assertEquals(true, myApplication.buscarPaciente(prontuario));
	}

	@Test//solicita uma busca por um paciente que não esta cadastrado fornecendo um número de identificação inválido
	public void buscarPacienteFalse(){
		Application myApplication = new Application();
		boolean retorno = true;
		try {
			retorno = Application.buscarPaciente((long) -1); //nao existe
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(false,retorno);
	}

	@Test//solicita uma busca por um médico que não está cadastrado

	public void buscarMedicoTrue(){
		Application myApplication = new Application();
		boolean retorno = true;
		try {
			retorno = Application.buscarMedico((long) 10); //existe
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true,retorno);
	}

	@Test//se solicita uma busca por um médico que não está cadastrado
	public void buscarMedicoFalse(){
		Application myApplication = new Application();
		boolean retorno = true;
		try {
			retorno = Application.buscarMedico((long) -1); //nao existe
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(false,retorno);
	}
	@Test //Teste invalido por causa de um email que nao segue o padrao de emails
	public void testaPermitirLoginEmailInvalido() throws SQLException {
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.permitirLogin("vivianesipro.com", "123");

		assertEquals(true, myApplication.errorFile.errorMessages.contains("Email inválido"));
	}

	@Test//teste inválido para campos de e-mail ou senha vazio no login
	public void testaPermitirLoginCamposVazios() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.permitirLogin("viviane@sipro.com", "");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Invalid fields in Login"));
		myApplication.permitirLogin("", "123");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Invalid fields in Login"));//teste inválido para campos de email vazio no login
		myApplication.permitirLogin("", "");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Invalid fields in Login"));

	}
	@Test//teste válido para campos não vazios no login
	public void testaPermitirLoginValido() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.permitirLogin("vivi@sipro.com", "123");
		assertEquals (false,myApplication.errorFile.errorMessages.contains("Invalid fields in Login"));
		assertEquals(false,myApplication.errorFile.errorMessages.contains("Login inexistente"));
	}

	@Test//teste inválido, para email nao esta cadastrado
	public void testaPermitirLoginEmailNCadastrado() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.permitirLogin("viviane@sipro.com", "123");
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Login inexistente"));
	}
	@Test//teste inválido, para senha nao correspondente a cadastrada
	public void testaPermitirLoginSenhaIncorreta() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.permitirLogin("vivi@sipro.com", "1234");

		assertEquals (true,myApplication.errorFile.errorMessages.contains("Login inexistente"));
	}
	@Test//teste invalido para Aceitacao medico, crm invalido
	public void testaAceitarMedicoInvalido() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		long crm = 123457;
		myApplication.aceitarMedico(crm);
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));

	}
	@Test//teste valido para Aceitacao medico, crm invalido
	public void testaAceitarMedicoValido() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		long crm = 12345;
		myApplication.aceitarMedico(crm);
		assertEquals (false,myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));

	}

	@Test//teste invalido por causa dos Campos Vazios
	public void testaMudarSenhaCamposVazios() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.mudarSenha("","123", "1234", "1234");
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));// testa mudar senha com campo email vazio
		myApplication.mudarSenha("vivi@sipro.com","", "1234", "1234");
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//testa mudar senha com campo senha inicial vazia
		myApplication.mudarSenha("vivi@sipro.com","123", "", "1234");
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//testa mudar senha com campo senha final vazia
		myApplication.mudarSenha("vivi@sipro.com","123", "1234", "");
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//testa mudar senha com campo senha final confirmação vazia
	}

	@Test//teste invalido para quando a nova senha n concidir com a confirmacao
	public void testaMudarSenhasNCoincidem() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.mudarSenha("vivi@sipro.com","123", "1234", "123");
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));// testa mudar senha com campo email vazio

	}

	@Test//teste invalido para quando a senha  atual nao for verdadeira
	public void testaMudarSenhaValido() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.mudarSenha("vivi@sipro.com","123", "123", "123");
		assertEquals (false,myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		assertEquals (false,myApplication.errorFile.errorMessages.contains("Dados inseridos inválidos"));
	}

	@Test//teste Invalido para a mudanca de senha por dados incorretos nos campos
	public void testaMudarDadosIncorretos() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.mudarSenha("vivi@sipro.com","1234", "123", "123");
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Dados inseridos inválidos"));// testa mudar senha com campo senha invalida
		myApplication.mudarSenha("viviane@sipro.com","123", "1234", "1234");
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Dados inseridos inválidos"));// testa mudar senha  com campo email invalido
	}


	@Test//teste valido para a adicao de solicitacao
	public void testaAddSolicitacaoValida() throws Exception{

		Application myApplication = new Application();
		long telefone=96999224;
		long crm = 3456;
		myApplication.teste = true;
		myApplication.addsolicitacao("Viviane", "123","123","Dermatologia",crm,"Medicina","vivi@sipro.com",telefone,"Contratado pelo Hospital");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//testa que nao existe nenhum campo requerido vazio e as senhas coincidem
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Email inválido"));//testa que o email eh valido

	}

	@Test//teste inválido para campos vazios requeridos quando uma solicitacao eh aceita
	public void testaAddSolicitacaoCamposVazios() throws Exception{
		Application myApplication = new Application();
		long telefone=96999224;
		long crm = 3456;
		myApplication.teste = true;
		myApplication.addsolicitacao("", "123","123","Dermatologia",crm,"Medicina","vivi@sipro.com",telefone,"Contratado pelo Hospital");//teste inválido para campos de Nome vazia no login
		myApplication.addsolicitacao("Viviane", "","123","Dermatologia",crm,"Medicina","vivi@sipro.com",telefone,"Contratado pelo Hospital");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addsolicitacao("Viviane", "123","","Dermatologia",crm,"Medicina","vivi@sipro.com",telefone,"Contratado pelo Hospital");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addsolicitacao("Viviane", "123","123","",crm,"Medicina","vivi@sipro.com",telefone,"Contratado pelo Hospital");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addsolicitacao("Viviane", "123","123","Dermatologista",null,"Medicina","vivi@sipro.com",telefone,"Contratado pelo Hospital");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addsolicitacao("Viviane", "123","123","Dermatologista",crm,"","vivi@sipro.com",telefone,"Contratado pelo Hospital");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addsolicitacao("Viviane", "123","123","Dermatologista",crm,"Medicina","",telefone,"Contratado pelo Hospital");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addsolicitacao("Viviane", "123","123","Dermatologista",crm,"Medicina","vivi@sipro.com",null,"Contratado pelo Hospital");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addsolicitacao("Viviane", "123","123","Dermatologista",crm,"Medicina","vivi@sipro.com",telefone,"");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));

	}

	@Test //Teste inválido, onde a senha e a confirmaçao nao coincidem
	public void testaAddSolicitacaoSenhasNCoincidem() throws Exception{
		Application myApplication = new Application();
		long telefone=96999224;
		long crm = 3456;
		myApplication.teste = true;
		myApplication.addsolicitacao("Viviane", "1234","123","Dermatologia",crm,"Medicina","vivi@sipro.com",telefone,"Contratado pelo Hospital");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));

	}
	@Test//Teste invalido, email fornecido pelo usuario eh invalido
	public void testaAddSolicitacaoEmailInvalido() throws Exception{

		Application myApplication = new Application();
		long telefone=96999224;
		long crm = 3456;
		myApplication.teste = true;
		myApplication.addsolicitacao("Viviane", "1234","1234","Dermatologia",crm,"Medicina","vivi@sipro",telefone,"Contratado pelo Hospital");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Email inválido"));

	}
	@Test//teste inválido por campos vazios requeridos 
	public void testaAddPacienteCamposVazios() throws SQLException {

		Application myApplication = new Application();
		long telefone=96999224;
		long prontuario = 3456;
		myApplication.teste = true;
		myApplication.addpaciente(null, "Viviane","","","", telefone, "f", "", "", true, 1, 11, 2000, "");//prontuario vazio
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addpaciente(prontuario, "","","","", telefone, "f", "", "", true, 1, 11, 2000, "");//nome vazio
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addpaciente(prontuario, "Viviane","","","", null, "", "", "", true, 1, 11, 2000, "");//telefone invalido
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "", "", "", true, 1, 11, 2000, "");//sexo invalido
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "", "", "", true, null, 11, 2000, "");//dia invalido
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, null, 2000, "");//mes invalido
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 11, null, "");//ano invalido
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
	}

	@Test//teste valido para adicao de paciente
	public void testaAddPacienteValido() throws SQLException{
		Application myApplication = new Application();
		long telefone=96999224;
		long prontuario = 3456;
		myApplication.teste = true;
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 11, 2000, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//testa se os campos sao validos
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//testa se a data é válida
	}
	@Test//Testa valores limites para o valor do dia minímo de 1 e para o valor maximo de 31
	public void testaAddPacienteDiaInvalido() throws SQLException{
		Application myApplication = new Application();
		long telefone=96999224;
		long prontuario = 3456;
		myApplication.teste = true;
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 0, 11, 2000, "");//dia invalido
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 11, 2000, "");//dia valido
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 2, 11, 2000, "");//dia valido
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 30, 11, 2000, "");//dia valido
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 31, 11, 2000, "");//dia valido
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 32, 11, 2000, "");//dia invalido
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Data inválida"));
	}
	@Test//Testa valores limites para o valor do mes minímo de 1 e para o valor maximo de 12
	public void testaAddPacienteMesInvalido() throws SQLException{
		Application myApplication = new Application();
		long telefone=96999224;
		long prontuario = 3456;
		myApplication.teste = true;
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 0, 2000, "");//mes invalido
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 1, 2000, "");//mes valido
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 2, 2000, "");//mes valido
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 11, 2000, "");//mes valido
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 12, 2000, "");//mes valido
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 13, 2000, "");//mes invalido
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Data inválida"));
	}
	@Test//Testa valores limites do ano para o valor minímo de 1900 e para o valor maximo de 2100
	public void testaAddPacienteAnoInvalido() throws SQLException{
		Application myApplication = new Application();
		long telefone=96999224;
		long prontuario = 3456;
		myApplication.teste = true;
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 1, 1899, "");//ano invalido
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 1, 1900, "");//ano valido
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 1, 1901, "");//ano valido
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 1, 2099, "");//ano valido
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 1, 2100, "");//ano valido
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));
		myApplication.addpaciente(prontuario, "Viviane","","","", telefone, "f", "", "", true, 1, 1, 2101, "");//ano invalido
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Data inválida"));
	}

	@Test//teste valido para adição de consultas 
	public void testaAddConsultaValido()throws SQLException{
		Application myApplication = new Application();
		long crm=96999224;
		long prontuario = 3456;
		myApplication.teste = true;
		myApplication.addConsulta(prontuario,crm, 1, 1, 2014, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//testa que nao existe campos invalidos
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//testa que a data nao  e invalida 
	}
	@Test//teste inválido para campos vazios requeridos para uma add de consulta
	public void testaAddConsultaCamposVazios()throws SQLException{
		Application myApplication = new Application();
		long crm=96999224;
		long prontuario = 3456;
		myApplication.teste = true;
		myApplication.addConsulta(null,crm, 1, 1, 2014, "");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//prontuario invalido
		myApplication.addConsulta(prontuario,null, 1, 1, 2014, "");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//crm invalido
		myApplication.addConsulta(prontuario,crm, null, 1, 2014, "");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//dia invalido
		myApplication.addConsulta(prontuario,crm, 1, null, 2014, "");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//mes invalido
		myApplication.addConsulta(prontuario,crm, 1, 1, null, "");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//ano invalido

	}
	@Test//Testa valores limites para o valor do dia minímo de 1 e para o valor maximo de 31
	public void testaAddConsultaDiaInvalido() throws SQLException{
		Application myApplication = new Application();
		long crm=96999224;
		long prontuario = 3456;
		myApplication.teste = true;
		myApplication.addConsulta(prontuario,crm, 0, 11, 2000, "");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Data inválida")); //dia invalido
		myApplication.addConsulta(prontuario,crm, 1, 11, 2000, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//dia valido
		myApplication.addConsulta(prontuario,crm, 2, 11, 2000, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//dia valido
		myApplication.addConsulta(prontuario,crm, 30, 11, 2000, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//dia valido
		myApplication.addConsulta(prontuario,crm, 31, 11, 2000, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//dia valido
		myApplication.addConsulta(prontuario,crm, 32, 11, 2000, "");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Data inválida"));//dia invalido
	}
	@Test//Testa valores limites para o valor do mes minímo de 1 e para o valor maximo de 12
	public void testaAddConsultaMesInvalido() throws SQLException{
		Application myApplication = new Application();
		long crm=96999224;
		long prontuario = 3456;
		myApplication.teste = true;
		myApplication.addConsulta(prontuario,crm, 1, 0, 2000, "");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Data inválida"));//mes invalido
		myApplication.addConsulta(prontuario,crm, 1, 1, 2000, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//mes valido
		myApplication.addConsulta(prontuario,crm, 1, 2, 2000, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//mes valido
		myApplication.addConsulta(prontuario,crm, 1, 11, 2000, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//mes valido
		myApplication.addConsulta(prontuario,crm, 1, 12, 2000, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//mes valido
		myApplication.addConsulta(prontuario,crm, 1, 13, 2000, "");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Data inválida"));//mes invalido
	}

	@Test//Testa valores limites do ano para o valor minímo de 1900 e para o valor maximo de 2100
	public void testaAddConsultaAnoInvalido() throws SQLException{
		Application myApplication = new Application();
		long crm=96999224;
		long prontuario = 3456;
		myApplication.teste = true;
		myApplication.addConsulta(prontuario,crm, 1, 1, 1899, "");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Data inválida"));//ano invalido
		myApplication.addConsulta(prontuario,crm, 1, 1, 1900, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//ano valido
		myApplication.addConsulta(prontuario,crm, 1, 1, 1901, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//ano valido
		myApplication.addConsulta(prontuario,crm, 1, 1, 2099, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//ano valido
		myApplication.addConsulta(prontuario,crm, 1, 1, 2100, "");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Data inválida"));//ano valido
		myApplication.addConsulta(prontuario,crm, 1, 1, 2101, "");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Data inválida"));//ano invalido
	}

	@Test//Teste valido para metodo fazer contato
	public void testaFazerContatoValido() throws EmailException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.fazerContato("vivi@sipro.com", "Problema com cadastro", false);//caso valido para email valido e solicitacao nao privada
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Email inválido"));
		myApplication.fazerContato("vivi@sipro.com", "Problema com cadastro", true);//caso valido para email valido e solicitacao  privada
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Email inválido"));
		myApplication.fazerContato("vivi@sipro.com", "Problema com cadastro", false);
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//teste falso para campos inválidos
	}

	@Test//teste inválido para campos vazios requeridos para a realizacao de um contanto com administrador
	public void testaFazerContatoCamposVazios() throws EmailException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.fazerContato("", "Problema com cadastro", false);
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//campo email vazio 
		myApplication.fazerContato("vivi@sipro.com", "", true);
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));//campo observacao vazio 


	}

	@Test//Teste invalido por causa de um email que nao segue o padrao de emails
	public void testaFazerContatoEmailInvalido() throws EmailException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.fazerContato("vivisipro.com", "Problema com cadastro", false);
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Email inválido"));


	}

	@Test//teste valido para o metodo recuperar senha
	public void testaRecuperarSenhaValido() throws EmailException, SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.recuperarSenha("vivi@sipro.com");
		assertEquals(false, myApplication.errorFile.errorMessages.contains("Email inválido"));


	}
	@Test//Teste invalido por causa de um email que nao segue o padrao de emails
	public void testaRecuperarSenhaEmailInvalido() throws EmailException, SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.recuperarSenha("vivanesipro.com");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Email inválido"));


	}
	@Test//Teste invalido por causa de um email que nao esta cadastrado no banco de dados
	public void testaRecuperarSenhaEmailNCadastrado() throws EmailException, SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.recuperarSenha("vivane@sipro.com");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("E-mail nao cadastrado"));


	}






}
