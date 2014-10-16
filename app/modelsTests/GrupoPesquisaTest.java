package modelsTests;

import static org.junit.Assert.*;
import java.util.Date;

import models.GrupoPesquisa;

import org.junit.Before;
import org.junit.Test;

public class GrupoPesquisaTest {

	@Test
	public void testGrupoPesquisaGettersAndSetters() {
		GrupoPesquisa g = new GrupoPesquisa();
		g.setAno_criacao((long)2000);
		g.setId((long)1);
		g.setNome("Grupo");
		
		assertEquals((long)2000,g.getAno_criacao());
		assertEquals((long)1,g.getId());
		assertEquals("Grupo",g.getNome());
	}

}
