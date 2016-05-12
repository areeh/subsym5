package tests;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import logic.ReadXLSX;
import logic.Settings;

public class ReadXLSXTest {

	ReadXLSX reader;
	
	@Before
	public void setUp() {
		reader = new ReadXLSX();
	}
	
	@After
	public void tearDown() {
		reader = null;
	}
	
	@Test
	public void ReadTest() {
		int[][] costs = new int[0][0];
		try {
			costs = reader.Read("C:\\Users\\Are\\workspace\\Cost.xlsx");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertEquals(16, costs[17][2]);
		
		int[][] dists = new int[0][0];
		try {
			dists = reader.Read("C:\\Users\\Are\\workspace\\Distance.xlsx");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertEquals(7635, dists[44][6]);
		
	}
	
	

}
