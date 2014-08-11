package a2;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ItemTest {

	@Before
	public void setUp() throws Exception {
		Item i = new Item("root", null);
	}

	@Test
	public void testSetAndGetParentItem() throws Exception{
		Directory i = new Directory("root", null);
		Directory i2 = new Directory("d1", i);
		Item i1 = new Item("i1", i);
		i1.setParentDir(i2);
		assertEquals(i1.getParentDir(), i2);
	}

	@Test
	public void testGetName() throws Exception{
		Directory i = new Directory("root", null);
		Item i1 = new Item("i1", i);
		assertEquals(i1.getName(), "i1");
		
	}

	@Test
	public void testAddItem() throws Exception{
		Directory i = new Directory("root", null);
		Directory i2 = new Directory("d1", i);
		Item i1 = new Item("i1", i);
		i2.addItem(i1);
		assertEquals(i1.getParentDir(), i2);
	}
	
	@Test
	public void testRemoveItem() throws Exception{
		Directory i = new Directory("root", null);
		Directory i2 = new Directory("d1", i);
		Item i1 = new Item("i1", i);
		i.removeItem(i1);
		assertEquals(i1.getParentDir(), null);
	}
	
	@Test
	public void testGetWholePath() throws Exception{
		Directory i = new Directory("root", null);
		Directory i2 = new Directory("d1", i);
		Item i1 = new Item("i1", i2);
		assertEquals(i1.getWholePath(), "root/d1/i1");
	}
}
