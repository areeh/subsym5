package logic;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xml.DatasetReader;
import org.jfree.data.xy.XYDataset;

import display.Plot;

public class Simulation {
	Population population;
	int generationNr;
	Settings set;
	
	ArrayList<Individual> paretoFront;
	Individual bestObj1;
	Individual bestObj2;
	Individual worstObj1;
	Individual worstObj2;

	public Simulation(Settings set) {
		this.set = set;
		population = new Population(set);
		
		generationNr = 0;
		
		
	}
	
	public void EAStep() {
		System.out.println("Ran step");
		
		generationNr += 1;
		
		if (generationNr == 1) {
			//Simple rank tournament
			population.createFirstChildren();
			System.out.println("Created first children");
		}
		
		
		List<Individual> combinedPop = new ArrayList<Individual>(set.getGenerationSize()*2);
		combinedPop.addAll(population.getAdults());
		combinedPop.addAll(population.getChildren());
		
		/*
		for (Individual ind : population.getAdults()) {
			System.out.println(ind.convertedGeno);
		}
		for (Individual ind : population.getChildren()) {
			System.out.println(ind.convertedGeno);
		}
		*/
		
		population.adults = new ArrayList<Individual>();
		
		ArrayList<ArrayList<Individual>> sorted = population.nonDominatedSorting(combinedPop);
		
		System.out.println("Ran nonDominatedSorting");
		
		//Storing pareto front
		paretoFront = sorted.get(0);

		for (ArrayList<Individual> front: sorted) {
			for (Individual ind : front) {
				population.adults.add(ind);
			}
		}
		
		System.out.println("added next adults");
		
		//Storing best for each objective
		
		for (Individual ind: population.adults) {
			ind.setSortVal("dist");
		}
		
		System.out.println("Set sort val");
		
		Collections.sort(population.adults);
		bestObj1 = population.adults.get(0);
		worstObj1 = population.adults.get(population.adults.size()-1);
		for (Individual ind: population.adults) {
			ind.setSortVal("cost");
		}
		
		System.out.println("Sorted and stored");
		
		Collections.sort(population.adults);
		bestObj2 = population.adults.get(0);
		worstObj2 = population.adults.get(population.adults.size()-1);

		System.out.println("Stored generation values");		
		
		//Crowded-comparison tournament
		population.createChildren();
		
		
		System.out.println("Created next generation children");

		
		log();
		System.out.println("Logged");			
	}
	
	private void log() {				
		System.out.println("Generation statistics: ");
		System.out.println("Generation number: " + generationNr);
		System.out.println("Best obj1: " + bestObj1.getDistanceFitness());
		System.out.println("Best obj2: " + bestObj2.getCostFitness());
		System.out.println("Worst obj1: " + worstObj1.getDistanceFitness());
		System.out.println("Worst obj2: " + worstObj2.getCostFitness());		
	}
	
	public void runLoop() {		
		System.out.println("started loop");
		
		int gens = set.getNrOfGenerations();
		
		while (gens > 0) {
			EAStep();
			gens--;
		}

		plot();
	}
	
	public void plot() {
		//System.out.println("ran plot");
		
		population.adults.remove(bestObj1);
		population.adults.remove(bestObj2);
		population.adults.remove(worstObj1);
		population.adults.remove(worstObj2);
		
		List<Individual> worst = new ArrayList<Individual>();
		worst.add(worstObj1);
		worst.add(worstObj2);
		List<Individual> best = new ArrayList<Individual>();
		best.add(bestObj1);
		best.add(bestObj2);
		
		XYDataset dataset = Plot.createDataset(population.adults, "Individual fitnesses");
		XYDataset dataset2 = Plot.createDataset(best, "Best fitnesses");
		XYDataset dataset3 = Plot.createDataset(worst, "Worst Fitnesses");
		
		Plot plot = new Plot("All", dataset);
		plot.addBestVals(dataset2);
		plot.addWorstVals(dataset3);
		
		plot.xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		
		plot.renderPlot();
		
		paretoFront.remove(bestObj1);
		paretoFront.remove(bestObj2);
		
		for (Individual ind : paretoFront) {
			ind.setSortVal("dist");
		}
		Collections.sort(paretoFront);
		worstObj1 = paretoFront.get(0);
		bestObj1 = paretoFront.get(paretoFront.size()-1);
		
		for (Individual ind : paretoFront) {
			ind.setSortVal("cost");
		}
		Collections.sort(paretoFront);
		worstObj2 = paretoFront.get(0);
		bestObj2 = paretoFront.get(paretoFront.size()-1);
		
		worst = new ArrayList<Individual>();
		worst.add(worstObj1);
		worst.add(worstObj2);
		best = new ArrayList<Individual>();
		best.add(bestObj1);
		best.add(bestObj2);
		
		
		XYDataset dataset4 = Plot.createDataset(paretoFront, "Individual fitnesses");
		XYDataset dataset5 = Plot.createDataset(best, "Best Fitnesses");
		XYDataset dataset6 = Plot.createDataset(worst, "Worst Fitnesses");
		
		Plot plot2 = new Plot("Pareto front", dataset4);
		plot2.addBestVals(dataset5);
		plot2.addWorstVals(dataset6);
		
		plot2.xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		
		plot2.renderPlot();
	}
	
	public void triplePlot(List<Individual> front1, List<Individual> front2, List<Individual> front3) {
		//System.out.println("ran plot");
				
				
		XYDataset dataset = Plot.createDataset(front1, "First front");
		XYDataset dataset2 = Plot.createDataset(front2, "Second front");
		XYDataset dataset3 = Plot.createDataset(front3, "Third front");
		
		Plot plot = new Plot("All pareto fronts", dataset);
		plot.addBestVals(dataset2);
		plot.addWorstVals(dataset3);
		
		plot.xyPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		
		plot.renderPlot();
		
	}

	public boolean isStopped() {
		return true;
	}

	public Settings getSettings() {
		return set;
	}
	
	public void start() {
		reset();
		System.out.println("Started run");
		runLoop();
		List<Individual> front1 = paretoFront;
		int bestObj1Front1 = bestObj1.getDistanceFitness();
		int bestObj2Front1 = bestObj2.getCostFitness();
		int worstObj1Front1 = worstObj1.getDistanceFitness();
		int worstObj2Front1 = worstObj2.getCostFitness();
		set = new Settings(0.6, 0.2, 100, 2000);
		population = new Population(set);
		generationNr = 0;
		runLoop();
		List<Individual> front2 = paretoFront;
		int bestObj1Front2 = bestObj1.getDistanceFitness();
		int bestObj2Front2 = bestObj2.getCostFitness();
		int worstObj1Front2 = worstObj1.getDistanceFitness();
		int worstObj2Front2 = worstObj2.getCostFitness();
		set = new Settings(0.0, 0.4, 200, 2000);
		population = new Population(set);
		generationNr = 0;
		runLoop();
		List<Individual> front3 = paretoFront;
		int bestObj1Front3 = bestObj1.getDistanceFitness();
		int bestObj2Front3 = bestObj2.getCostFitness();
		int worstObj1Front3 = worstObj1.getDistanceFitness();
		int worstObj2Front3 = worstObj2.getCostFitness();
		
		System.out.println("First front");
		System.out.println("Best objective one: "+ bestObj1Front1);
		System.out.println("Best objective two: "+ bestObj2Front1);
		System.out.println("Worst objective one: "+ worstObj1Front1);
		System.out.println("Worst objective two: "+ worstObj2Front1);
		System.out.println("Number of solutions in pareto front: " + front1.size());
		
		System.out.println("Second front");
		System.out.println("Best objective one: "+ bestObj1Front2);
		System.out.println("Best objective two: "+ bestObj2Front2);
		System.out.println("Worst objective one: "+ worstObj1Front2);
		System.out.println("Worst objective two: "+ worstObj2Front2);
		System.out.println("Number of solutions in pareto front: " + front2.size());
		
		System.out.println("Third front");
		System.out.println("Best objective one: "+ bestObj1Front3);
		System.out.println("Best objective two: "+ bestObj2Front3);
		System.out.println("Worst objective one: "+ worstObj1Front3);
		System.out.println("Worst objective two: "+ worstObj2Front3);
		System.out.println("Number of solutions in pareto front: " + front3.size());
		
		triplePlot(front1, front2, front3);
	}
	
	public void reset() {
		generationNr = 0;
		population = new Population(set);
		
	}

}
