import org.junit.Test;

import play.mvc.Http.Response;
import play.test.FunctionalTest;


public class TesteFuncional extends FunctionalTest {

	public class ApplicationTest extends FunctionalTest {
	     
	    @Test
	    public void testTheHomePage() {
	        Response response = GET("/");
	        assertStatus(200, response);
	        
	        
	    }
	     
	}
	
}
