package tests;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.xy.XYDataset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import display.Plot;
import logic.Individual;
import logic.Settings;

public class PlotTest {
	Settings set;
	
	@Before
	public void setUp() {
		set = new Settings();
	}
	
	@After
	public void tearDown() {
		set = null;
		
	}
}
