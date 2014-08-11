package a2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DirectoryTest {

	@Before
	public void setUp() throws Exception {
		Directory d = new Directory ("d", null);
	}

	@Test
	public void testGetSubDirExists() throws Exception{
		Directory d = new Directory ("d", null);
		Directory d1 = new Directory ("d1", d);
		assertEquals(d.getSubDir("d1"), d1);
	}
	
	@Test
	public void testGetSubDirNotExists() throws Exception{
		Directory d = new Directory ("d", null);
		Directory d1 = new Directory ("d1", d);
		assertEquals(d.getSubDir("d2"), null);
	}
	
	@Test
	public void testGetSubFileNotExists() throws Exception{
		Directory d = new Directory ("d", null);
		Directory d1 = new Directory ("d1", d);
		File f = new File("f", d);
		assertEquals(d.getSubFile("d2"), null);
	}
	
	@Test
	public void testGetSubFileExists() throws Exception{
		Directory d = new Directory ("d", null);
		Directory d1 = new Directory ("d1", d);
		File f = new File("f", d);
		assertEquals(d.getSubFile("f"), f);
	}
	

}
