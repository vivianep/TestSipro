package modelsTests;

import static org.junit.Assert.*;

import java.util.Date;

import models.Medico;

import org.junit.Before;
import org.junit.Test;

public class MedicoTest {

	@Test
	public void testMedicoGettersAndSetters() {
		Medico m = new Medico();

		m.setCrm((long)15);
		m.setNome("James");
		m.setEspecializacao("Urologia");
		m.setTelefone((long)99998899);
		m.setDepartamento("IMD");
		m.setEmail("a@a.com");
		m.setSenha("pass");
		m.setJustificativa("Novo medico");
		m.setAdmin(false);
		
		assertEquals((long)15, m.getCrm());
		assertEquals("James",m.getNome());
		assertEquals("Urologia",m.getEspecializacao());
		assertEquals((long)99998899,m.getTelefone());
		assertEquals("IMD",m.getDepartamento());
		assertEquals("a@a.com",m.getEmail());
		assertEquals("pass",m.getSenha());
		assertEquals("Novo medico",m.getJustificativa());
		assertEquals(false,m.getAdmin());
	}

}
