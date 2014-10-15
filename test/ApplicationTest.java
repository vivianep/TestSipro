import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.*;

import controllers.Application;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
    
    @Test
    public void loginOk(){
    	Application a = new Application();
    	//a.permitirLogin(email, senha);
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
    public void loginEmailNaoOk(){
    	
    }
    
    @Test
    public void loginSenhaNaoOk(){
    	
    }
}