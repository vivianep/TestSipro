package controllers;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.Paciente;

import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationTest {
	
	private static Connection con;  
	private static Statement comando;
	
	
	@Before
	public void setUp() throws ClassNotFoundException,SQLException {
		    
	    	  Class.forName("org.postgresql.Driver");  
	            con = (Connection) DriverManager.getConnection(  
	                    "jdbc:postgresql://localhost:5432/Sipro",  
	                    "postgres",  
	                    "senha123");  
	            comando = (Statement) con.createStatement();  
	      
	}

	
	@Test
	public void buscarPacienteTrue(){
		Application myApplication = new Application();
		boolean retorno = true;
		try {
			retorno = Application.buscarPaciente((long) 777); //existe
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true,retorno);
	}
	
	@Test
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
	
	@Test
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
	
	@Test
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
	@Test 
	public void testarLoginEmailInvalido() throws SQLException {
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.permitirLogin("viviane@sipro.com", "123");
		
		assert myApplication.errorFile.errorMessages.contains("Invalid e-mail");
	}
	
	@Test//teste inválido para campos de senha vazio no login
	public void testaLoginCamposVazios() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.permitirLogin("viviane@sipro.com", "");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Invalid fields in Login"));
		myApplication.permitirLogin("", "123");
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Invalid fields in Login"));//teste inválido para campos de email vazio no login
		myApplication.permitirLogin("", "");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Invalid fields in Login"));
		;
	}
	@Test//teste válido para campos não vazios no login
	public void testaLoginCamposNVazios() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.permitirLogin("vivi@sipro.com", "123");
		assertEquals (false,myApplication.errorFile.errorMessages.contains("Invalid fields in Login"));
		assertEquals(false,myApplication.errorFile.errorMessages.contains("Login inexistente"));
	}
	
	@Test
	public void testaLoginEmailNCadastrado() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.permitirLogin("viviane@sipro.com", "123");
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Login inexistente"));
	}
	@Test
	public void testaLoginSenhaErrada() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.permitirLogin("vivi@sipro.com", "1234");
		
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Login inexistente"));
	}
	
	@Test
	public void testarAceitarMedicoExiste(){
		Application myApplication = new Application();
		myApplication.teste = true;
		long crm = 112233;
		ResultSet rs;
		try {
			rs = comando.executeQuery("SELECT * FROM solicitacao WHERE crm = " + crm);
			assertEquals(true,rs.next());
			rs = comando.executeQuery("SELECT * FROM medico WHERE crm = " + crm);
			assertEquals(false,rs.next());
			myApplication.aceitarMedico(crm);
			rs = comando.executeQuery("SELECT * FROM solicitacao WHERE crm = " + crm);
			assertEquals(false,rs.next());
			rs = comando.executeQuery("SELECT * FROM medico WHERE crm = " + crm);
			assertEquals(true,rs.next());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test//teste invalido
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
	
	@Test//teste valido para a mudanca de senha
	public void testaMudarDadosIncorretos() throws SQLException{
		Application myApplication = new Application();
		myApplication.teste = true;
		myApplication.mudarSenha("vivi@sipro.com","1234", "123", "123");
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Dados inseridos inválidos"));// testa mudar senha com campo senha invalida
		myApplication.mudarSenha("viviane@sipro.com","123", "1234", "1234");
		assertEquals (true,myApplication.errorFile.errorMessages.contains("Dados inseridos inválidos"));// testa mudar senha  com campo email invalido
	}
	
	@Test//teste inválido para campos vazios requeridos quando uma solicitacao eh aceita
	public void testaAddSolicitacaoCamposVazios() throws Exception{
		Application myApplication = new Application();
		long telefone=96999224;
		long crm = 3456;
		myApplication.teste = true;
		myApplication.addsolicitacao("", "123","123","Dermatologia",crm,"Medicina","vivi@sipro.com",telefone,"Contratado pelo Hospital");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
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
	
	@Test 
	public void testaAddSolicitacaoSenhasNCoincidem() throws Exception{
		Application myApplication = new Application();
		long telefone=96999224;
		long crm = 3456;
		myApplication.teste = true;
		myApplication.addsolicitacao("Viviane", "1234","123","Dermatologia",crm,"Medicina","vivi@sipro.com",telefone,"Contratado pelo Hospital");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Campos inválidos ou vazios"));
	
	}
	@Test
	public void testaAddSolicitacaoEmailInvalido() throws Exception{
		Application myApplication = new Application();
		long telefone=96999224;
		long crm = 3456;
		myApplication.teste = true;
		myApplication.addsolicitacao("Viviane", "1234","1234","Dermatologia",crm,"Medicina","vivi@sipro",telefone,"Contratado pelo Hospital");//teste inválido para campos de senha vazia no login
		assertEquals(true, myApplication.errorFile.errorMessages.contains("Email inválido"));
	
	}

	
	
	
	
}
