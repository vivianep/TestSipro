package controllers;

import play.*;
import play.data.validation.Validation;
//import play.data.validation.Error;
import play.data.validation.Required;
import play.mvc.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;  

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import models.*;

public class Application extends Controller {
	
	private static ValidationTest validating;
	private static Connection con;  
	private static Statement comando;  
	
	private static List<Paciente> pacientesLista;
	private static List<GrupoPesquisa> gruposLista;
	private static List<Diagnostico> diagnosticosLista;
	private static List<Integer> masculinoLista;
	private static List<Integer> femininoLista;
	private static List<Medico> solicitacaoLista;
	private static Medico medicoLogado;
	public static ErrorFile errorFile;
	public static boolean teste;
	
	public void Application(){
		teste=false;
	}
	
	public String getEmail(){
		return "emailLogin";
	}
	
	private static void conectar() {  
	      try {  
	    	  Class.forName("org.postgresql.Driver");  
	            con = (Connection) DriverManager.getConnection(  
	                    "jdbc:postgresql://localhost:5432/Sipro",  
	                    "postgres",  
	                    "senha123");  
	            comando = (Statement) con.createStatement();  
	      } catch (ClassNotFoundException ex) {  
	            Logger.getLogger(Paciente.class.getName()).log(Level.SEVERE, null, ex);  
	        } catch (SQLException ex) {  
	            Logger.getLogger(Paciente.class.getName()).log(Level.SEVERE, null, ex);  
	        }   
	   }

	@Before (unless={"cadastroNovoMedico", "gerarRelatorio", "getDiagG", "getDiagGrupo", "getDiagnosticos","getDiag", "permitirLogin", "index","indexErro",
						"addsolicitacao", "buscarRelatorios", "buscaSQL", "relatorioLista", "contatoAdmin", "esqueciSenha","recuperarSenha","contato","fazerContato"})
	static void checkAutenticacao () {
		if (!session.contains("login")) {
			index();
		}
	}
	
    
    public static void addpaciente(Long prontuario, 
    							   String nome, 
    							   String diagnostico1,
    							   String diagnostico2,
    							   String diagnostico3,
    							   Long telefone, 
    							   String sexo, 
    							   String procedencia,
    							   String endereco, 
    							   boolean historico, 
    							   Integer dia, 
    							   Integer mes, 
    							   Integer ano, String grupo) throws SQLException{
    	
 	
    	validation.required("nome", nome);
     	validation.required("prontuario",prontuario);  
    	validation.required("telefone",telefone);
    	validation.required("sexo",sexo);
    	validation.required("dia",dia);
    	validation.range("dia",dia,1,31);
    	validation.range("mes",mes,1,12);
    	validation.range("ano",ano,1900,2050);
    	validation.required("mes",mes);
    	validation.required("ano",ano);
    	
        // Handle errors
        if(validation.hasErrors()) {
        	List<Diagnostico> dList = diagnosticosLista;
	    	List<GrupoPesquisa> gList = gruposLista;
	    	render("@cadastroFicha",dList,gList);
         }
    	
        
    	conectar();
    	
    	String data = dia + "/" + mes + "/" + ano;
    	long id = -1;
    	ResultSet rs = comando.executeQuery("SELECT * FROM grupo_pesquisa WHERE nome = '" + grupo + "'");
    	while (rs.next()){
    		id = rs.getLong("id");
    	}
    	
    	
    	String sql = "INSERT INTO paciente VALUES(" + prontuario + ",'" + nome + "',"
    					+ telefone + ",'" + sexo + "','" + procedencia +
    					"','" + endereco + "','" + historico + "','" + data + "','" + diagnostico1 + "','" +
    					diagnostico2 + "','" + diagnostico3 + "')";
    	String sql2 = "INSERT INTO membro VALUES (" + prontuario + "," + id + ")"; 
    	
    	
    		if (prontuario == null || nome.equals(null) || telefone == null || sexo.equals(null) || dia == null || mes == null || ano == null)
        		cadastroFicha(); 
    			
        	else {
					try {
						comando.executeUpdate(sql);
						if (id != -1) comando.executeUpdate(sql2);
					} catch (SQLException e) {
					}
        	}
    	
    	con.close();
    	flash.success("Cadastro feito com sucesso");
    	telaPrincipal();
    }
        

    public static void addsolicitacao(String nome,
			  						  String senha1, 
			                          String senha2,  								   
    								  String especializacao, 
    								  Long crm, 
    								  String departamento,
    								  String email,
    								  Long telefone, 
    								  String justificativa) throws Exception{
    
 	   validation.required("crm",crm);
 	   validation.required(nome);
	   validation.required(especializacao);
 	   validation.required(departamento);
       validation.email(email);
       validation.required(email);
	   validation.required(senha1);
	   validation.required(senha2);
	   validation.equals(senha1, senha2);
	   validation.required(telefone);
	   validation.required(justificativa);
	
       // Handle errors
      if(validation.hasErrors()) {
           render("@cadastroNovoMedico");
       }

    		conectar();
    		String sql = "INSERT INTO solicitacao VALUES(" + crm + ",'" + nome + "','" + especializacao +
					"','" + departamento + "','" + email + "','" + senha1 +
					"'," + telefone + ",'" + justificativa + "')";
    		try {
    			if (senha1.equals(senha2))
    				comando.executeUpdate(sql);

    		} catch (Exception dataI){
  			
    		}
    		con.close();    		
    	    flash.success("Solicitação feita com sucesso");
    		index();
    }
    
   public static void permitirLogin(String email, String senha) throws SQLException{
	  errorFile= new ErrorFile();
	
	  
	  validating= new ValidationTest();
	  validating.required(email);
	  validating.required(senha);
	  if(validating.email(email))
    	  errorFile.errorMessages.add("Invalid e-mail");
       // Handle errors
     
	  if(validating.hasErrors()) {
		   if (teste)
			   errorFile.errorMessages.add("Invalid fields in Login");
		   else
			   render("@index");
	  }
     
	   conectar();
	   ResultSet rs;
	   Medico m = new Medico();
	   rs = comando.executeQuery("SELECT * FROM medico WHERE email = '" + email +"' AND senha = '" + senha + "'");
		
			if (rs.next()) {
				   m.setCrm(rs.getLong("crm"));
				   m.setDepartamento(rs.getString("departamento"));
				   m.setEmail(rs.getString("email"));
				   m.setEspecializacao(rs.getString("especializacao"));
				   m.setJustificativa(rs.getString("justificativa"));
				   m.setNome(rs.getString("nome"));
				   m.setSenha(rs.getString("senha"));
				   m.setTelefone(rs.getLong("telefone"));
				   m.setAdmin(rs.getBoolean("admin"));
				   medicoLogado = m;
				   if(!teste){
					session.put("login", email);
					getDiagGrupo(3);
				   }
			}
			else{
				if(!teste){
					flash.error("Senha ou e-mail inválido!");
					index();
				}else
					errorFile.errorMessages.add("Login inexistente");
						
				
		}
		
					   
	   
   }
   
   public static void aceitarMedico(Long crm) throws SQLException{
	   
	   conectar();
	   ResultSet rs;
	   Medico m = new Medico();
	   
	   rs = comando.executeQuery("SELECT * FROM solicitacao WHERE crm = " + crm);
	   
	   while (rs.next()){
		   m.setCrm(rs.getLong("crm"));
		   m.setDepartamento(rs.getString("departamento"));
		   m.setEmail(rs.getString("email"));
		   m.setEspecializacao(rs.getString("especializacao"));
		   m.setJustificativa(rs.getString("justificativa"));
		   m.setNome(rs.getString("nome"));
		   m.setSenha(rs.getString("senha"));
		   m.setTelefone(rs.getLong("telefone"));
		   m.setAdmin(false);
	   }
	   String sqlAdd = "INSERT INTO medico VALUES (" + m.getCrm() + ",'" + m.getNome() + "','" + m.getEspecializacao() +
			   		"','" + m.getDepartamento() + "', '" + m.getEmail() + "', '" + m.getSenha() +
			   		"', " + m.getTelefone() + ", '" + m.getJustificativa() + "')";
	   
	   String sqlRemover = "DELETE FROM solicitacao WHERE crm = " + crm;
	   
	   comando.executeUpdate(sqlAdd);
	   comando.executeUpdate(sqlRemover);
	   if(!teste)
		   verSolicitacao();
   }
   
   public static void recusarMedico(Long crm) throws SQLException{
	   
	   conectar();
	   
	   comando.executeUpdate("DELETE FROM solicitacao WHERE crm = " + crm);
	   verSolicitacao();
	   
   }
   
   public static void verSolicitacao() throws SQLException{
	   
	   conectar();
	   ResultSet rs;
	   List<Medico> solicitacoes = new ArrayList<Medico>();
	   
	   rs = comando.executeQuery("SELECT * FROM solicitacao");
	   while (rs.next()){
		   Medico m = new Medico();
		   m.setCrm(rs.getLong("crm"));
		   m.setDepartamento(rs.getString("departamento"));
		   m.setEmail(rs.getString("email"));
		   m.setEspecializacao(rs.getString("especializacao"));
		   m.setJustificativa(rs.getString("justificativa"));
		   m.setNome(rs.getString("nome"));
		   m.setSenha(rs.getString("senha"));
		   m.setTelefone(rs.getLong("telefone"));
		   solicitacoes.add(m);
	   }
	   
	   solicitacaoLista = solicitacoes;
	   verSolicitacoes();
	   
   }
    

   public static void mudarSenha(String email, 
		   						 String senhaantiga, 
		   						 String senhanova1, 
		   						 String senhanova2) throws SQLException {
	   validation.required(email);
	   validation.email(email);
	   validation.required(senhaantiga);
	   validation.required(senhanova1);
	   validation.equals(senhanova1,senhanova2);
	   validation.required(senhanova2);
	   
       // Handle errors
      if(validation.hasErrors()) {
           render("@alterarSenha");
       }
	   
	   ResultSet rs;
	   String sql = null;
	   conectar();
	   
		   rs = comando.executeQuery("SELECT * FROM medico WHERE email = '" + email +"' AND senha = '" + senhaantiga + "'");
		   if (rs.next()) {
		   		sql = "UPDATE medico SET senha = '" + senhanova1 + "' WHERE email = '" + email + "' AND senha = '" + senhaantiga + "'";
		   		comando.executeUpdate(sql);
		   		flash.success("Senha alterada com sucesso!");
		   		telaPrincipal();
		   }
			else{
				flash.error("Os dados inseridos são inválidos!");
				alterarSenha(); 
			}
	   
	   con.close();

   }
   
   public static void logout () {
	   session.remove("login");
	   index();
   }
   
   public static void alterarFicha(long prontuario, String diagnostico1, String diagnostico2, String diagnostico3, Long telefone, String endereco, boolean historico) throws SQLException{	        
       conectar();
       String sql = "UPDATE paciente set telefone =" + telefone + ", endereco = '" + endereco + "', historico = '" + historico + "', diagnostico1 = '" +
    		   		diagnostico1 + "', diagnostico2 = '" + diagnostico2 + "', diagnostico3 = '" + diagnostico3 + "' WHERE prontuario = " + prontuario;
       comando.executeUpdate(sql);
       con.close();
       telaPrincipal();
      }
   
   public static void buscarPacientes(String nome, Long prontuario, Integer dia, Integer mes, Integer ano) throws SQLException{
   	
	conectar();
   	List<Paciente> pacientes = new ArrayList<Paciente>();
   	ResultSet rs;
   		

   		boolean nomeNulo, prontNulo, dataNula;

   		if (nome.equals("")) nomeNulo = true;
   		else nomeNulo = false;

   		if (prontuario == null) prontNulo = true;
   		else prontNulo = false;

   		if (dia == null || mes == null || ano == null) dataNula = true;
   		else dataNula = false;  		
   		
   		String consulta = "select DISTINCT p.prontuario, nome, diagnostico1, diagnostico2, diagnostico3, telefone, sexo, procedencia, endereco, historico, datanasc from consulta as c, paciente as p where c.prontuario = p.prontuario ";
   		if (!dataNula)  consulta = consulta + " AND data = '" + ano + "-" + mes + "-" + dia +"'";
   		else consulta = "select DISTINCT p.prontuario, nome, diagnostico1, diagnostico2, diagnostico3, telefone, sexo, procedencia, endereco, historico, datanasc from  paciente as p where prontuario != -1";
   		if (!nomeNulo) consulta = consulta + "AND nome = '" + nome + "'";
   		if (!prontNulo) consulta = consulta + " AND p.prontuario = " + prontuario; 

   	    rs = comando.executeQuery(consulta + "order by nome");
   	       	     
   		   while (rs.next()){
   		   Paciente p = new Paciente();
   	       	p.setProntuario(rs.getLong("prontuario"));            	
   	       	p.setNome(rs.getString("nome"));
   	       	p.setDiagnostico1(rs.getString("diagnostico1"));
   	       	p.setDiagnostico2(rs.getString("diagnostico2"));
   	       	p.setDiagnostico3(rs.getString("diagnostico3"));
   	       	p.setTelefone(rs.getLong("telefone"));
   	      	p.setSexo(rs.getString("sexo"));
   	       	p.setProcedencia(rs.getString("procedencia"));
   	       	p.setEndereco(rs.getString("endereco"));
   	       	p.setHistorico(rs.getBoolean("historico"));
   	       	p.setDatanasc(rs.getDate("datanasc"));
   	       	pacientes.add(p);
   	        }
   		   

   		   pacientesLista = pacientes;
   		   resultadoFicha();    	
   }
   
   public static void addConsulta(Long prontuario, 
		                          Long crm, 
		                          Integer dia, 
		                          Integer mes, 
		                          Integer ano, 
		                          String obs) throws SQLException{
	   validation.required("prontuario",prontuario);
	    validation.required("crm",crm);
	    validation.required("dia",dia);
	    validation.range(dia,1,31);
	    validation.range(mes,1,12);
	    validation.range(ano,1900,2050);
	    validation.required("mes",mes);
	    validation.required("ano",ano);
	    
	       // Handle errors
	       if(validation.hasErrors()) {
	            render("@adicionarConsulta");
	        }
	   
	   conectar();
	   ResultSet rs;
	   
	   rs = comando.executeQuery("SELECT * FROM paciente WHERE prontuario = " + prontuario);
	   if (!rs.next()){
		   // Paciente nao existente
		   flash.error("Paciente nao existente!");
		   render("@adicionarConsulta");
	   }
	   rs = comando.executeQuery("SELECT * FROM medico where crm = " + crm);
	   if (!rs.next()){
		   // Medico nao existente
		   flash.error("Médico nao existente!");
		   render("@adicionarConsulta");
	   }
	   rs = comando.executeQuery("SELECT * FROM consulta WHERE prontuario = " + prontuario + " AND data = '" + dia + "/" + mes + "/" + ano + "'");
	   
	   if (rs.next()){
		   flash.error("Ja foi cadastrada uma consulta para este paciente, nessa data!");
		   render("@adicionarConsulta");
	   }
	   
	   if (obs == "null") obs = " ";
	   
	   String sql = "INSERT INTO consulta VALUES ('" + dia + "/" + mes + "/" + ano + "','" + obs + "'," + prontuario + "," + crm + ")";
	   comando.executeUpdate(sql);
	   con.close();
	   telaPrincipal();
   }
	
	public static Date getData(int idade){
		
		Date nascimento = new Date();
		nascimento.setYear(nascimento.getYear() - idade);
		
		return nascimento;
	}
	
	public static void buscaSQL(String diagnostico, String procedencia, Integer idade, String sexo, Boolean historico, String projeto, int id) throws SQLException {
		
		conectar();
		   List<Paciente> relatorio = new ArrayList<Paciente>();
		   List<Integer> dadosM = new ArrayList<Integer>();
		   List<Integer> dadosF = new ArrayList<Integer>();
		   ResultSet rs;
		   
		   
		   boolean diagNulo, procNulo, idadNulo, sexoNulo, histNulo, projNulo;
		   Date data1, data2;
		   String datA1 = null, datA2 = null;
		   int projetoId = 0, masculino = 0, feminino = 0;
		   
		   String consulta;
		   
		   if (diagnostico.equals("Todos")) diagNulo = true;
		   else diagNulo = false;
		   if (procedencia.equals(""))  procNulo = true;
		   else procNulo = false;
		   if (idade == null) idadNulo = true;
		   else idadNulo = false;
		   if (sexo == null) sexoNulo = true;
		   else sexoNulo = false;
		   if (historico == null) histNulo = true;
		   else histNulo = false;
		   if (projeto.equals("Todos")) projNulo = true;
		   else projNulo = false;
		   
		   
		   if (!idadNulo){
			   data1 = getData(idade);
			   data2 = data1;
			   String dia = ""+data1.getDate(), mes = ""+(data1.getMonth()+1), ano = ""+(data1.getYear()+1899);
			   if (data1.getDate() < 10) dia = "0" + dia;
			   if (data1.getMonth() < 10) mes = "0" + mes;
			   		datA1 = dia + "/" + mes + "/" + ano;
			   
			   ano = ""+(data1.getYear()+1900);
			   dia = ""+data1.getDate();
			   mes = ""+(data1.getMonth()+1);
			   if (data2.getDate() < 10) dia = "0" + dia;
			   if (data2.getMonth() < 10) mes = "0" + mes;
			   		datA2 = dia + "/" + mes + "/" + ano;
		   }
		   
		   
		   rs = comando.executeQuery("SELECT COUNT (*) FROM paciente WHERE sexo = 'm' GROUP BY sexo");
		   while (rs.next()){
			   masculino = rs.getInt("count");
		   }
		   
		   rs = comando.executeQuery("SELECT COUNT (*) FROM paciente WHERE sexo = 'f' GROUP BY sexo");
		   while (rs.next()){
			   feminino = rs.getInt("count");
		   }
		   
		   dadosM.add(masculino);
		   dadosF.add(feminino);
		   
		 
		   rs = comando.executeQuery("SELECT id FROM grupo_pesquisa WHERE nome = '" + projeto + "'");
		   while (rs.next()){
			   projetoId = rs.getInt("id");
		   }
		   
		   
		   consulta = "SELECT * FROM paciente WHERE prontuario != -1 ";
		   
		   if (!diagNulo) consulta = consulta + " AND (diagnostico1 = '" + diagnostico + "' OR diagnostico2 = '" + diagnostico + "' OR diagnostico3 = '" + diagnostico + "')";
		   if (!procNulo) consulta = consulta + " AND procedencia = '" + procedencia + "'";
		   if (!idadNulo) consulta = consulta + " AND datanasc > '" + datA1 + "' AND datanasc < '" + datA2 + "' ";
		   if (!sexoNulo) consulta = consulta + " AND sexo = '" + sexo + "'";
		   if (!histNulo) consulta = consulta + " AND historico = '" + historico + "'";
		   if (!projNulo) consulta = consulta + " AND prontuario IN (SELECT prontuario FROM membro WHERE id = " + projetoId + ")";
		   
		   rs = comando.executeQuery(consulta + " order by nome");
		   
		   while (rs.next()){
			    Paciente p = new Paciente();
	  	       	p.setProntuario(rs.getLong("prontuario"));            	
	  	       	p.setNome(rs.getString("nome"));
	  	       	p.setDiagnostico1(rs.getString("diagnostico1"));
	  	       	p.setDiagnostico2(rs.getString("diagnostico2"));
	  	       	p.setDiagnostico3(rs.getString("diagnostico3"));
	  	       	p.setTelefone(rs.getLong("telefone"));
	  	      	p.setSexo(rs.getString("sexo"));
	  	       	p.setProcedencia(rs.getString("procedencia"));
	  	       	p.setEndereco(rs.getString("endereco"));
	  	       	p.setHistorico(rs.getBoolean("historico"));
	  	       	p.setDatanasc(rs.getDate("datanasc"));
	  	       	relatorio.add(p);
	  	       	
		   }
		   
		   pacientesLista = relatorio;
		   masculinoLista = dadosM;
		   femininoLista = dadosF;
		   if(id == 0) {
			   relatorioLista();
		   }
		   else {
			   relatorioListaPrivado();
		   }
			   
	}
 
	public static void getDiagGrupo(int id) throws SQLException{
		
	
		conectar();
		
		List<Diagnostico> diagnosticos = new ArrayList<Diagnostico>();
		List<GrupoPesquisa> grupos = new ArrayList<GrupoPesquisa>();
		
		ResultSet rs;
		
		rs = comando.executeQuery("SELECT * FROM diagnostico order by nome");
		
		while (rs.next()){
			Diagnostico d = new Diagnostico();
			d.setNome(rs.getString("nome"));
			d.setGenetico(rs.getBoolean("genetico"));
			diagnosticos.add(d);
		}
		
		rs = comando.executeQuery("SELECT * FROM grupo_pesquisa order by nome");
		
		while (rs.next()){
			GrupoPesquisa g = new GrupoPesquisa();
			g.setId(rs.getLong("id"));
			g.setNome(rs.getString("nome"));
			g.setAno_criacao(rs.getLong("ano_criacao"));
			grupos.add(g);
		}
		
		diagnosticosLista = diagnosticos;
		gruposLista = grupos;
		
		if (id == 0)
			gerarRelatorio();
		else if (id == 1)
			gerarRelatorioPrivado();
		else if (id == 2)
			cadastroFicha();
		else if (id == 3)
			telaPrincipal();
		
	}
	
	public static boolean buscarPaciente(long prontuario) throws SQLException{
		
		conectar();
		
		ResultSet rs;
		rs = comando.executeQuery("SELECT * FROM paciente WHERE prontuario = " + prontuario);
		
		if (rs.next())
				return true;
		else
			return false;
		
	}
	
public static boolean buscarMedico(long crm) throws SQLException{
		
		conectar();
		
		ResultSet rs;
		rs = comando.executeQuery("SELECT * FROM medico WHERE crm = " + crm);
		
		if (rs.next())
			return true;
		else
			return false;
		
	}
	
   public static void buscarRelatorios(String diagnostico, String procedencia, Integer idade, String sexo, Boolean historico, String projeto) throws SQLException{

	   buscaSQL(diagnostico, procedencia, idade, sexo, historico, projeto, 0);
	   
   }
   
   public static void buscarRelatoriosPrivado(String diagnostico, String procedencia, Integer idade, String sexo, Boolean historico, String projeto) throws SQLException{
	   
	   buscaSQL(diagnostico, procedencia, idade, sexo, historico, projeto, 1);
	   
   }
   
	
	public static void getDiagG() throws SQLException{
		
		getDiagGrupo(0);
		
	}
	
	public static void getDiagGPrivado() throws SQLException{
		
		getDiagGrupo(1);
		
	}
	
	public static void getDiagCadastro() throws SQLException{
		
		getDiagGrupo(2);
		
	}
   
   public static void index() {
        render();
    }
    
    public static  void cadastroNovoMedico() {
	        render();
	    }

        public static  void gerarRelatorio() throws SQLException {
        	
        	List<GrupoPesquisa> pList = gruposLista;
        	List<Diagnostico> dList = diagnosticosLista;
	    	render(pList,dList);
	    }

        public static  void relatorioLista() {
        	List<Paciente> pList = pacientesLista;
	    	render(pList);
	    }
        
        public static  void relatorioListaPrivado() {
        	List<Paciente> pList = pacientesLista;
        	List<Integer> mList = masculinoLista;
        	List<Integer> fList = femininoLista;
	    	render(pList,mList,fList);
	    }

        public static  void telaPrincipal() {
        	Medico med = medicoLogado;
	        render(med);
	    }

	    public static  void cadastroFicha() {
	    	List<Diagnostico> dList = diagnosticosLista;
	    	List<GrupoPesquisa> gList = gruposLista;
	    	render(dList,gList);
	    }

	    public static  void consultarFicha() {
	        render();
	    }
	    
	 
	    public static  void resultadoFicha() {
   	        List<Paciente> pList = pacientesLista;
	    	render(pList);
	    }
	    
		
	    
	    public static  void atualizarFicha(int p) throws SQLException{
	    	Paciente pacienteEscolhido = pacientesLista.get(p-1);
	    	
			conectar();
			List<Consulta> consultas = new ArrayList<Consulta>();
			List<Diagnostico> diagnosticos = new ArrayList<Diagnostico>();
			
			ResultSet rs = comando.executeQuery("SELECT * FROM consulta where prontuario = " + pacienteEscolhido.getProntuario());
			while (rs.next()){
				Consulta c = new Consulta();
				c.setCrm(rs.getLong("crm"));
				c.setData(rs.getDate("data"));
				c.setObservacao(rs.getString("observacao"));
				c.setProntuario(rs.getLong("prontuario"));
				consultas.add(c);			
			}
	    	    	
			
	    	//List<Consulta> cList = consultasLista;
			diagnosticos = diagnosticosLista;
	        render(pacienteEscolhido,consultas, diagnosticos);
	    }
	    
	    public static void recuperarSenha(String email) throws SQLException, EmailException{
	    	validation.required(email);
	    	validation.email(email);	    	
	        // Handle errors
	        if(validation.hasErrors()) {
	             render("@esqueciSenha");
	         }
	        
	 	   conectar();
		   ResultSet rs;
		   String senha = "";
		   String nome = "";
		   
		   rs = comando.executeQuery("SELECT * FROM medico WHERE email = '" + email + "'");
		   if (rs.next()){
			   senha = rs.getString("senha");
			   nome = rs.getString("nome");
		   }
		   
		   if (senha ==""){
			   flash.error("Email não existe no banco de dados!");
			   render("@esqueciSenha");
			   // email nao existente
		   }
		   else{
			   
				SimpleEmail oEmail = new SimpleEmail();
				oEmail.setHostName("smtp.gmail.com");
				oEmail.setSmtpPort(587);
				oEmail.setAuthenticator(new DefaultAuthenticator(
						"damger.ciencomp@gmail.com", "ciencomp2011.2"));
				oEmail.setTLS(true);
				oEmail.setFrom("damger.ciencomp@gmail.com");
				oEmail.setSubject("Sua senha do Sipro");
				oEmail.setMsg("Caro(a) "+nome+" como foi solicitado, aqui esta a sua senha do sistema Sipro: " + senha);
				oEmail.addTo(email);
				oEmail.send();

				flash.success("A senha foi enviada para seu e-mail com sucesso!");
				index();			   
		   }
		   
	    }
	    
	    public static  void esqueciSenha() {
	    	render();
	    }
	    
	    public static  void alterarSenha() {
	    	Medico med = medicoLogado;
	        render(med);	        
	    }
	    
	    public static  void gerarRelatorioPrivado() {
	    	List<GrupoPesquisa> pList = gruposLista;
        	List<Diagnostico> dList = diagnosticosLista;
	    	render(pList,dList);
	    }
	    
	    public static void contato(String email, String conteudo) throws EmailException{
	    	fazerContato(email, conteudo, false);
	    }
	    
	    public static void contatoPrivado(String email, String conteudo) throws EmailException{
	    	fazerContato(email, conteudo, true);
	    }

	public static void fazerContato(String email, String conteudo,boolean privado)
			throws EmailException {

		validation.required(email);
		validation.email(email);
		validation.required(conteudo);

		// Handle errors
		if (!privado) {
			if (validation.hasErrors()) {
				render("@contatoAdmin");
			}
		} else {
			if (validation.hasErrors()) {
				render("@contatoAdminPrivado");
			}
		}

		SimpleEmail oEmail = new SimpleEmail();
		oEmail.setHostName("smtp.gmail.com");
		oEmail.setSmtpPort(587);
		oEmail.setAuthenticator(new DefaultAuthenticator(
				"damger.ciencomp@gmail.com", "ciencomp2011.2"));
		oEmail.setTLS(true);
		oEmail.setFrom(email);
		oEmail.setSubject("Contato do Sipro");
		oEmail.setMsg(conteudo);
		oEmail.addTo("damger.ciencomp@gmail.com");
		oEmail.send();

		flash.success("Mensagem enviada com sucesso!");
		if (!privado)
			index();
		else
			telaPrincipal();
	}
	    
	    public static void contatoAdmin() {
	    	render();
	    }
	    
	    public static void contatoAdminPrivado() {
	    	Medico med = medicoLogado;
	        render(med);
	    }
	    
	    public static void adicionarConsulta(Integer prontuario) {
	    	Integer prontPaciente = prontuario;
	    	render(prontPaciente);
	    }
	    
	    public static void verSolicitacoes(){
	    	List<Medico> sList = solicitacaoLista;
	    	render(sList);
	    }

	    
	    
}