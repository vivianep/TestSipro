package modelsTests;

import static org.junit.Assert.*;
import java.util.Date;

import models.Paciente;

import org.junit.Before;
import org.junit.Test;

public class PacienteTest {

	@Test
	public void testPacienteGettersAndSetters() {
		Date d = new Date();
		d.setMonth(01);
		d.setYear(1993);
		d.setDate(01);
		Paciente p = new Paciente();
		p.setProntuario((long)15);
		p.setNome("James");
		p.setDiagnostico1("Obesidade");
		p.setTelefone((long)99998899);
		p.setSexo("m");
		p.setProcedencia("Promater");
		p.setEndereco("Candelaria");
		p.setHistorico(true);
		p.setDatanasc(d);
		
		p.setDiagnostico2("Cancer");
		p.setDiagnostico3("Ebola");
		
		assertEquals((long)15, p.getProntuario());
		assertEquals("James",p.getNome());
		assertEquals("Obesidade",p.getDiagnostico1());
		assertEquals((long)99998899,p.getTelefone());
		assertEquals("m",p.getSexo());
		assertEquals("Promater",p.getProcedencia());
		assertEquals("Candelaria",p.getEndereco());
		assertEquals(true,p.isHistorico());
		assertEquals(d,p.getDatanasc());
		
		assertEquals("Cancer",p.getDiagnostico2());
		assertEquals("Ebola",p.getDiagnostico3());
	}
	
	@Test
	public void testPacientWithParameters(){
		Date d = new Date();
		d.setMonth(01);
		d.setYear(1993);
		d.setDate(01);
		Paciente p = new Paciente((long)15,"James","Obesidade",(long)99998899,"m","Promater","Candelaria",true,d);
		assertEquals((long)15, p.getProntuario());
		assertEquals("James",p.getNome());
		assertEquals("Obesidade",p.getDiagnostico1());
		assertEquals((long)99998899,p.getTelefone());
		assertEquals("m",p.getSexo());
		assertEquals("Promater",p.getProcedencia());
		assertEquals("Candelaria",p.getEndereco());
		assertEquals(true,p.isHistorico());
		assertEquals(d,p.getDatanasc());
	}
}
