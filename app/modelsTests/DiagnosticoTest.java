package modelsTests;

import static org.junit.Assert.*;
import java.util.Date;

import models.Diagnostico;

import org.junit.Before;
import org.junit.Test;

public class DiagnosticoTest {

	@Test
	public void testDiagnosticoGettersAndSetters() {
		Diagnostico d = new Diagnostico();
		d.setNome("James");
		d.setGenetico(true);
		assertEquals("James",d.getNome());
		assertEquals(true,d.isGenetico());
	}

}
