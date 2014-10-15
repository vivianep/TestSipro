package models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PacienteTest {

	@Test
	public void testGetProntuario() {
		
		Paciente p = new Paciente();
		p.setProntuario(15);
		assertEquals(15,p.getProntuario());
		
	}

	@Test
	public void testSetProntuario() {
		Paciente p = new Paciente();
		p.setProntuario(15);
		assertEquals(15,p.getProntuario());
	}

}
