package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Settings {
	
	private List<Integer> cities;
	private int maxGenotypeValue;
	private int genotypeSize;
	private int generationSize;
	private int[][] costs;
	private int[][] dists;
	private double mutateRate;
	private double crossoverRate;
	private double eChance;
	private int nrOfGenerations;
	private int k;
	private int bitSize;
	private int pointsCrossover;

	public Settings() {
		cities = new ArrayList<Integer>(48);
		for (int i=1; i<49; i++) {
			cities.add(i);
		}
		
		bitSize = 6;
		/** max value for ints in the genotype */
		maxGenotypeValue = 48;
		genotypeSize = 48*6;
		crossoverRate = 0.9;
		mutateRate = 0.1;
		generationSize = 100;
		nrOfGenerations = 2000;
		setPointsCrossover(20);
		
		k = 5;
		eChance = 0.0d; //0-1
		
		
		ReadXLSX reader = new ReadXLSX();
		try {
			costs = reader.Read("C:\\Users\\Are\\workspace\\Cost.xlsx");
			dists = reader.Read("C:\\Users\\Are\\workspace\\Distance.xlsx");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public Settings(double crossoverRate, double mutateRate, int generationSize, int nrOfGenerations) {
		cities = new ArrayList<Integer>(48);
		for (int i=1; i<49; i++) {
			cities.add(i);
		}
		
		bitSize = 6;
		/** max value for ints in the genotype */
		maxGenotypeValue = 48;
		genotypeSize = 48*6;
		this.crossoverRate = crossoverRate;
		this.mutateRate = mutateRate;
		this.generationSize = generationSize;
		this.nrOfGenerations = nrOfGenerations;
		setPointsCrossover(8);
		
		k = 60;
		eChance = 0.05d; //0-1
		
		
		ReadXLSX reader = new ReadXLSX();
		try {
			costs = reader.Read("C:\\Users\\Are\\workspace\\Cost.xlsx");
			dists = reader.Read("C:\\Users\\Are\\workspace\\Distance.xlsx");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

	public List<Integer> getCities() {
		return cities;
	}

	public int getMaxGenotypeValue() {
		return maxGenotypeValue;
	}
	
	public double getCrossoverRate() {
		return crossoverRate;
	}
	
	public int getGenotypeSize() {
		return genotypeSize;
	}
	
	public int getGenerationSize() {
		return generationSize;
	}
	
	public int[][] getCosts() {
		return costs;
	}
	
	public int[][] getDists() {
		return dists;
	}
	
	
	public static void main(String[] args) {
		Settings set = new Settings();
	}


	public double getMutateRate() {
		return mutateRate;
	}


	public double getEChance() {
		return eChance;
	}


	public int getK() {
		return k;
	}
	
	public int getNrOfGenerations() {
		return nrOfGenerations;
	}


	public void setCrossoverRate(double doubleValue) {
		this.crossoverRate = doubleValue;
	}


	public void setMutateRate(double doubleValue) {
		this.mutateRate = doubleValue;
		
	}


	public void setNrOfGenerations(int intValue) {
		this.nrOfGenerations = intValue;
		
	}


	public void setGenerationSize(int intValue) {
		this.generationSize = intValue;
		
	}


	public void setK(int intValue) {
		this.k = intValue;
		
	}


	public void setEChance(double doubleValue) {
		this.eChance = doubleValue;
		
	}


	public int getBitSize() {
		return bitSize;
	}


	public int getPointsCrossover() {
		return pointsCrossover;
	}


	public void setPointsCrossover(int pointsCrossover) {
		this.pointsCrossover = pointsCrossover;
	}
}
