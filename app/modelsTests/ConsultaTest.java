package modelsTests;

import static org.junit.Assert.*;
import java.util.Date;

import models.Consulta;

import org.junit.Before;
import org.junit.Test;

public class ConsultaTest {

	@Test
	public void testConsultaGettersAndSetters() {
		Consulta c = new Consulta();
		Date d = new Date();
		d.setMonth(01);
		d.setYear(1993);
		d.setDate(01);
		c.setCrm(Long.getLong("10"));
		c.setObservacao("obs");
		c.setProntuario(Long.getLong("10"));
		c.setData(d);
		
		assertEquals(Long.getLong("10"),c.getCrm());
		assertEquals("obs",c.getObservacao());
		assertEquals(Long.getLong("10"),c.getProntuario());
		assertEquals(d,c.getData());
		
	}
	
}
