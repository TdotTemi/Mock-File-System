package a2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CommandTest {

	@Before
	public void setUp() throws Exception {
		Command cmd= new Command();
	}

	@Test
	public void testMkdir() {
		Directory root = new Directory("/", null);
		Item current = (Item)root;
		Command cmd= new Command();
		String[] input = new String[]{"d1"};
		cmd.mkdir(input, current);
		Directory d1 = new Directory("d1", root);
		assertEquals(root.getSubItem("d1"), d1);
	}
	
	@Test
	public void testCd() {
		Directory root = new Directory("/", null);
		Directory d1 = new Directory("d1", root);
		Item current = (Item)root;
		Command cmd= new Command();
		cmd.cd("d1", current, root);
		assertEquals(cmd.cd("d1", current, root), d1);
	}

}
