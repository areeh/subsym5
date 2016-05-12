package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import logic.Individual;
import logic.Population;
import logic.Settings;

public class IndividualTest {
	Settings set1;
	Individual ind1;
	Population pop1;
	
	@Before
	public void setUp() {
		set1 = new Settings();
		ind1 = new Individual(set1);
		pop1 = new Population(set1);
		
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void createPhenotypeTest() {
		ind1.createPheno();
		Assert.assertEquals(48, ind1.getPhenotype().size());
		Assert.assertTrue(ind1.getPhenotype().containsAll(set1.getCities()) && set1.getCities().containsAll(ind1.getPhenotype()));
	}
	
	@Test
	public void mutationTest() {
		int i=10;
		while (i > 0) {
			ind1.mutate();
			i--;
		}
		Assert.assertEquals(48, ind1.getGenotype().size());
	}
	
	@Test
	public void sortingTest() {
		List<Individual> inds = new ArrayList<Individual>();
		Individual ind;
		for (int i=0; i<10; i++) {
			ind = new Individual(set1);
			ind.setSortVal(i);
			inds.add(ind);
		}
		Collections.sort(inds);
		for (Individual indi : inds) {
			System.out.println(indi.getSortVal());
		}
	}
	
	@Test
	public void nonDominatedSortingTest() {
		int resIndCount = 0;
		List<Individual> inds = new ArrayList<Individual>();
		ArrayList<ArrayList<Individual>> res = new ArrayList<ArrayList<Individual>>();
		for (int i=0; i<40; i++) {
			inds.add(new Individual(set1));			
		}
		
		res = pop1.nonDominatedSorting(inds);
		
		for (ArrayList<Individual> front : res) {
			for (Individual ind : front) {
				resIndCount++;
			}
		}
		
		Assert.assertEquals(set1.getGenerationSize(), resIndCount);
	}

}
