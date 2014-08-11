package a2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FileTest {

	@Before
	public void setUp() throws Exception {
		File f = new File("f", null);
	}

	@Test
	public void testAddAndGetContends() throws Exception {
		Directory d = new Directory ("d", null);
		File f = new File("f", d);
		f.addContent("newline");
		assertEquals(f.getFileContents(), "newline");
	}

	@Test
	public void testEraseContends() throws Exception {
		Directory d = new Directory ("d", null);
		File f = new File("f", d);
		f.addContent("newline");
		f.eraseContent();
		assertEquals(f.getFileContents(), "");
	}
	
}
