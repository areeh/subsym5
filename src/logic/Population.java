package logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Population {
	
	int individualIDs;
	List<Individual> children;
	List<Individual> adults;
	Settings set;
	Random rng;
	
	

	public Population(Settings set) {
		individualIDs = 3;
		rng = new Random();
		this.set = set;
		
		adults = new ArrayList<Individual>(set.getGenerationSize());
		for (int i=0; i<set.getGenerationSize(); i++) {
			adults.add(new Individual(set, rng, individualIDs));
			incrementIndividualID();
		}
		for (Individual adult : adults) {
			adult.createInvPheno();
			adult.updateFitness();
		}
	}
	
	public Pair<Individual, Individual> addCrossover(Pair<Individual, Individual> parents) {
		Pair<Individual, Individual> kidPair = crossover(parents);
		
		
		if (kidPair == null) {
			return null;
		} else {
			
			while (containsGenotype(children, kidPair.x.genotype) || containsGenotype(children, kidPair.y.genotype) || kidPair.x.equals(kidPair.y)
					|| containsGenotype(adults, kidPair.x.genotype) || containsGenotype(adults, kidPair.y.genotype)) {
				kidPair = crossover(parents);
			}
			children.add(kidPair.x);
			children.add(kidPair.y);			
			
			return kidPair;
		}
		
	}
	
	private static List<Integer> inverse(List<Integer> permutation) {
		int m = 0;
		List<Integer> inverse = new ArrayList<Integer>(Collections.nCopies(permutation.size(), 0));
		for (int i=0; i<permutation.size(); i++) {
			m = 0;
			while (permutation.get(m) != i+1) {
				if (permutation.get(m) > i) {
					inverse.set(i, inverse.get(i)+1);
				}
				m += 1;
			}
		}
		return inverse;
	}
	
	private static List<Integer> unInverse(List<Integer> inverse) {		
		List<Integer> perm = new ArrayList<Integer>(Collections.nCopies(inverse.size()+1, 0));
		List<Integer> pos = new ArrayList<Integer>(Collections.nCopies(inverse.size(), 0));
		for (int i=inverse.size()-1; i > -1; i--) {
			for (int m = i; m<inverse.size(); m++) {
				if (pos.get(m) >= inverse.get(i)+1) {
					pos.set(m, pos.get(m)+1);
				}
				pos.set(i, inverse.get(i)+1);
			}
		}
		for (int i=0; i<inverse.size(); i++) {
			perm.set(pos.get(i), i+1);
		}
		return perm.subList(1, perm.size());
	}
	
	/*
	private Pair<Individual, Individual> crossover(Pair<Individual, Individual> parents) {
		int failureCount = 0;
		if (rng.nextDouble() > set.getCrossoverRate()) {
			Individual child1 = new Individual(parents.x.genotype, set, rng, individualIDs);
			incrementIndividualID();
			Individual child2 = new Individual(parents.y.genotype, set, rng, individualIDs);
			incrementIndividualID();
			
			child1.mutate();
			child2.mutate();
			
			return new Pair<Individual, Individual>(child1, child2);
		}
		
		int split = (int) Math.floor(rng.nextDouble()*(set.getGenotypeSize()-10));
		split+=5;
		
		List<Integer> geno = parents.x.getGenotype().subList(0, split);
		geno.addAll(parents.y.getGenotype().subList(split, set.getGenotypeSize()));
		Individual child1 = new Individual(
				geno,
				set, rng, individualIDs);
		incrementIndividualID();
		
		int split2 = (int) Math.floor(rng.nextDouble()*(set.getGenotypeSize()-10));
		split2+=5;
		
		geno = parents.y.getGenotype().subList(0, split2);
		geno.addAll(parents.x.getGenotype().subList(split2, set.getGenotypeSize()));
		Individual child2 = new Individual(
				 geno,
				set, rng, individualIDs);
		incrementIndividualID();
		
		while (parents.x.getGenotype().equals(child1.getGenotype()) && failureCount < 20) {
			failureCount++;
			System.out.println("child equal to parent (x and 1)");
			split = (int) Math.floor(rng.nextDouble()*(set.getGenotypeSize()-10));
			split+=5;
			System.out.println(split);
			
			geno = parents.x.getGenotype().subList(0, split);
			geno.addAll(parents.y.getGenotype().subList(split, set.getGenotypeSize()));
			child1 = new Individual(
					geno,
					set, rng, individualIDs);
			incrementIndividualID();
		}
		
		while (parents.y.getGenotype().equals(child2.getGenotype()) && failureCount < 20) {
			failureCount++;
			System.out.println("child equal to parent (y and 2)");
			split2 = (int) Math.floor(rng.nextDouble()*(set.getGenotypeSize()-10));
			split2+=5;
			System.out.println(split2);
			
			geno = parents.y.getGenotype().subList(0, split2);
			geno.addAll(parents.x.getGenotype().subList(split2, set.getGenotypeSize()));
			child2 = new Individual(
					 geno,
					set, rng, individualIDs);
			incrementIndividualID();
		}
		
		while (parents.x.getGenotype().equals(child2.getGenotype()) && failureCount < 10) {
			failureCount++;
			System.out.println("child equal to parent (x and 2)");
			split2 = (int) Math.floor(rng.nextDouble()*(set.getGenotypeSize()-10));
			split2+=5;
			System.out.println(split2);
			
			geno = parents.y.getGenotype().subList(0, split2);
			geno.addAll(parents.x.getGenotype().subList(split2, set.getGenotypeSize()));
			child2 = new Individual(
					 geno,
					set, rng, individualIDs);
			incrementIndividualID();
		}
		
		while (parents.y.getGenotype().equals(child1.getGenotype()) && failureCount < 10) {
			failureCount++;
			System.out.println("child equal to parent (y and 1)");
			split = (int) Math.floor(rng.nextDouble()*(set.getGenotypeSize()-10));
			split+=5;
			System.out.println(split);
			
			geno = parents.x.getGenotype().subList(0, split);
			geno.addAll(parents.y.getGenotype().subList(split, set.getGenotypeSize()));
			child1 = new Individual(
					geno,
					set, rng, individualIDs);
			incrementIndividualID();
		}
		
		if (failureCount >= 10) {
			child1.mutate();
			child2.mutate();
			return new Pair<Individual, Individual>(child1, child2);
		} else {
			return new Pair<Individual, Individual>(child1, child2);				
		}
	}
	*/
	
	protected Pair<Individual, Individual> crossover(Pair<Individual, Individual> parents) {	
		if (rng.nextDouble() > set.getCrossoverRate()) {
			Individual child1 = new Individual(parents.x.genotype, set, rng, individualIDs);
			incrementIndividualID();
			Individual child2 = new Individual(parents.y.genotype, set, rng, individualIDs);
			incrementIndividualID();
			
			child1.mutate();
			child2.mutate();
			
			return new Pair<Individual, Individual>(child1, child2);
		}
		
		
		//x point crossover
		List<Integer> crossoverPoints = new ArrayList<Integer>();
		for (int i=0; i<set.getPointsCrossover(); i++) {
			int chance = rng.nextInt(set.getGenotypeSize());
			while (crossoverPoints.contains(chance)) {
				chance = rng.nextInt(set.getGenotypeSize());
			}
			crossoverPoints.add(chance);
		}
		
		Collections.sort(crossoverPoints);
		
		String geno1 = "";
		String geno2 = "";
		for (int i=0; i<set.getPointsCrossover(); i++) {
			if (i == 0) {
				geno1 += parents.x.getGenotype().substring(0, crossoverPoints.get(i));
				geno2 += parents.y.getGenotype().substring(0, crossoverPoints.get(i));
			}else if (i % 2 == 0) {
				geno1 += parents.x.getGenotype().substring(crossoverPoints.get(i-1), crossoverPoints.get(i));
				geno2 += parents.y.getGenotype().substring(crossoverPoints.get(i-1), crossoverPoints.get(i));
			} else {
				geno1 += parents.y.getGenotype().substring(crossoverPoints.get(i-1), crossoverPoints.get(i));
				geno2 += parents.x.getGenotype().substring(crossoverPoints.get(i-1), crossoverPoints.get(i));
			}
		}
		
		geno1 += parents.y.getGenotype().substring(crossoverPoints.get(set.getPointsCrossover()-1), set.getGenotypeSize());
		geno2 += parents.x.getGenotype().substring(crossoverPoints.get(set.getPointsCrossover()-1), set.getGenotypeSize());
		
		if (geno1.length() != set.getGenotypeSize()) {
			System.out.println("wrong geno size from crossover");
		}
		
		if (geno2.length() != set.getGenotypeSize()) {
			System.out.println("wrong geno size from crossover");
		}
		
		
		Individual child1 = new Individual(
				geno1,
				set, rng, individualIDs);
		incrementIndividualID();
		
		Individual child2 = new Individual(
				geno2,
				set, rng, individualIDs);
		incrementIndividualID();
		
		return new Pair<Individual, Individual>(child1, child2);
		
	}
	
	/** TODO: source
	 * Sped up version from paper X (implemented by me)
	 * 
	 * @return List of lists where each list is a front, in order from first to last front
	 */
	public ArrayList<ArrayList<Individual>> nonDominatedSorting(List<Individual> inds) {
		
		/*
		for (int i=0; i<inds.size(); i++) {
			for (int j=0; j<inds.size(); j++) {
				if (i != j) {
					if (inds.get(i).getGenotype().equals(inds.get(j).getGenotype())) {
						System.out.println("Duplicate into sorting");
					}
				}
			}
		}
		*/
		
		
		System.out.println("Ran sorting");
		ArrayList<ArrayList<Individual>> res = new ArrayList<ArrayList<Individual>>();
		ArrayList<ArrayList<Individual>> fronts = new ArrayList<ArrayList<Individual>>();
		fronts.add(new ArrayList<Individual>());
		
		ArrayList<ArrayList<Individual>> dominates = new ArrayList<ArrayList<Individual>>();
		for (int i=0; i<inds.size(); i++) {
			dominates.add(new ArrayList<Individual>());
		}
		ArrayList<Integer> dominatedBy = new ArrayList<Integer>(Collections.nCopies(inds.size(), 0));
		
		//Calculate counts
		for (int i=0; i<inds.size(); i++){
			for (int j=0; j<inds.size(); j++) {
				//if equal, do nothing
				if (inds.get(i).getCostFitness() == inds.get(j).getCostFitness() && inds.get(i).getDistanceFitness() == inds.get(j).getDistanceFitness()) {
					//If solution dominates other solution
				} else if (inds.get(i).getCostFitness() <= inds.get(j).getCostFitness() && inds.get(i).getDistanceFitness() <= inds.get(j).getDistanceFitness()) {
					//Add to list of solutions current dominates
					dominates.get(i).add(inds.get(j));
				//If solution is dominated by other solution
				} else if (inds.get(i).getCostFitness() >= inds.get(j).getCostFitness() && inds.get(i).getDistanceFitness() >= inds.get(j).getDistanceFitness()) {
					//Increment dominatedBy counter
					dominatedBy.set(i, dominatedBy.get(i)+1);
				}
			}
			
			if (dominatedBy.get(i) == 0) {
				fronts.get(0).add(inds.get(i));
				dominatedBy.set(i, -1);
			}
		}
		
		//Sort into fronts using the domination lists and counts
		boolean nextEmpty = false;
		int frontCounter = 0;
		ArrayList<Individual> nextFront = fronts.get(0);
		while (!nextEmpty) {
			nextFront = new ArrayList<Individual>();
			for (int i=0; i<fronts.get(frontCounter).size(); i++) {
				int indexCurrent = inds.indexOf(fronts.get(frontCounter).get(i));
				for (int j=0; j<dominates.get(indexCurrent).size(); j++) {
					//Index of individual in the set current solution dominates
					int indexDominated = inds.indexOf(dominates.get(indexCurrent).get(j));
					dominatedBy.set(indexDominated, dominatedBy.get(indexDominated) - 1);
					if (dominatedBy.get(indexDominated) == 0) {
						nextFront.add(inds.get(indexDominated));
						dominatedBy.set(indexDominated, -1);
					}
				}
			}
			if (!nextFront.isEmpty()) {
				frontCounter++;
				fronts.add(nextFront);				
			} else {
				nextEmpty = true;
			}
		}
		
		int count = 0;
		for (ArrayList<Individual> front : fronts) {
			crowdingDistanceAssignment(front);
			for (Individual ind: front) {
				count++;
			}
		}
		
		if (count < inds.size()) {
			System.out.println("Sorting returned fewer items than input");
			System.out.println(count);
			System.out.println(inds.size());
		}
		
		int added = 0;
		List<Individual> lastFront = new ArrayList<Individual>();
		for (int i=0; i<fronts.size(); i++) {
			if (added+fronts.get(i).size() > set.getGenerationSize()) {
				lastFront = fronts.get(i);
				i = fronts.size();
			} else {
				res.add(fronts.get(i));
				added+=fronts.get(i).size();
			}
		}
		
		int itemsToAdd = set.getGenerationSize()-added;
		if (itemsToAdd > 0 && lastFront.size() > 0) {
			res.add(crowdTournamentSelection(lastFront, itemsToAdd));			
		} else {
			if (lastFront.size() == 0 && itemsToAdd > 0) {
				System.out.println("Found empty front when more itemsToAdd");
				System.out.println(itemsToAdd);
				System.out.println(lastFront.size());				
			}
		}
		
		int rank = 0;
		int members = 0;
		for (ArrayList<Individual> indis : res) {
			for (Individual ind : indis) {
				ind.setRank(rank);
				members++;
			}
			rank++;
		}
		
		if (members != set.getGenerationSize()) {
			System.err.println("nonDominatedSorting returned wrong number of individuals");
			System.out.println("printing nonDominatedSorting error");
			System.out.println(itemsToAdd);
			System.out.println(members);
			System.out.println(set.getGenerationSize());
		}
		
		List<Individual> test = new ArrayList<Individual>();
		for (ArrayList<Individual> front : res) {
			for (Individual ind : front) {
				test.add(ind);
			}
		}
		
		/*
		for (int i=0; i<test.size(); i++) {
			for (int j=0; j<test.size(); j++) {
				if (i != j) {
					if (test.get(i).getGenotype().equals(test.get(j).getGenotype())) {
						System.out.println("Duplicate out of sorting sorting");
					}
				}
			}
		}
		*/
		
		return res;	
	}
	
	private void crowdingDistanceAssignment(List<Individual> front) {
		if (front.size() < 3) {
			for (Individual ind : front) {
				ind.setCrowdDist(Double.POSITIVE_INFINITY);
			}
		} else {
			double fmax = 0;
			double fmin = 0;
			for (Individual ind : front) {
				ind.setSortVal("dist");
			}
			Collections.sort(front);
			fmin = front.get(0).getDistanceFitness();
			fmax = front.get(front.size()-1).getDistanceFitness();
			front.get(front.size()-1).setCrowdDist(Double.POSITIVE_INFINITY);
			front.get(0).setCrowdDist(Double.POSITIVE_INFINITY);
			double prevDist = Math.abs(front.get(1).getDistanceFitness()-front.get(0).getDistanceFitness());
			double nextDist = 0;
			for (int i=1; i<front.size()-1; i++) {
				nextDist = Math.abs(front.get(i+1).getDistanceFitness()-front.get(i).getDistanceFitness());
				
				if (nextDist == 0) {
					double chance = rng.nextDouble();
					if (chance > 0.90) {
						front.get(i).mutate();					
					}
					/*
					front.get(i).mutate();
					front.get(i).mutate();
					System.out.println("Distance between individuals 0");
					System.out.println(front.get(i+1).getDistanceFitness());
					System.out.println(front.get(i).getDistanceFitness());
					System.out.println(front.get(i+1).getPhenotype());
					System.out.println(front.get(i).getPhenotype());
					System.out.println(front.get(i).convertedGeno);
					System.out.println(front.get(i+1).convertedGeno);
					*/
				}
				
				front.get(i).setCrowdDist((nextDist+prevDist)/(fmax-fmin));
				prevDist = nextDist;
			}
			
			for (Individual ind : front) {
				ind.setSortVal("cost");
			}
			Collections.sort(front);
			fmin = front.get(0).getCostFitness();
			fmax = front.get(front.size()-1).getCostFitness();
			front.get(front.size()-1).setCrowdDist(Double.POSITIVE_INFINITY);
			front.get(0).setCrowdDist(Double.POSITIVE_INFINITY);
			prevDist = Math.abs(front.get(1).getCostFitness()-front.get(0).getCostFitness());
			nextDist = 0;
			for (int i=1; i<front.size()-1; i++) {
				nextDist = Math.abs(front.get(i+1).getCostFitness()-front.get(i).getCostFitness());
				if (front.get(i).getCrowdDist() == Double.POSITIVE_INFINITY) {
				} else {
					front.get(i).setCrowdDist(front.get(i).getCrowdDist()+((nextDist+prevDist)/(fmax-fmin)));				
				}
				prevDist = nextDist;
			}
		}
		
	}

	private ArrayList<Individual> crowdTournamentSelection(List<Individual> front, int itemsToAdd) {
		
		if (front.size() < itemsToAdd) {
			System.err.println("Front too small to add enough individuals crowdTournamentSelection");
		}
		
		ArrayList<Individual> res = new ArrayList<Individual>(itemsToAdd);
		if (front.size() == 1) {
			res.add(front.get(0));
			return res;
		} else if (front.size() == 2) {
			if (itemsToAdd == 1) {
				res.add(front.get(0));
			} else {
				res.addAll(front);
			}
			return res;
		}
		
		for (Individual ind : front) {
			ind.setSortVal("crowdDist");
		}
		Collections.sort(front);
		if (front.size() >= itemsToAdd) {
			res.addAll(front.subList(front.size()-itemsToAdd, front.size()));
			System.out.println("printing chosen in front");
			for (Individual ind : front.subList(front.size()-itemsToAdd, front.size())) {
				System.out.println(ind.getCrowdDist());
			}
		} else {
			res.addAll(front);
			System.out.println("printing chosen all in front");
			for (Individual ind : front) {
				System.out.println(ind.getCrowdDist());
			}
		}
		if (res.size() != itemsToAdd) {
			System.out.println("printing error");
			System.err.println("crowdTournamentSelection returned wrong number of individuals");
			System.out.println(res.size());
			System.out.println(itemsToAdd);
		}
		
		return res;
	}
	
	private void reprodTourSel(String type) {
		children = new ArrayList<Individual>(set.getGenerationSize());
		int repros = set.getGenerationSize()/2;
		
		while (repros > 0) {
			Pair<Individual, Individual> parents = null;
			if (type == "crowdComparison") {
				parents = selectWinnerPairCrowdComparison(adults);	
			} else {
				parents = selectWinnerPair(adults);				
			}
			if (parents.x.getGenotype().equals(parents.y.getGenotype())) {
				System.out.println("Got same parents");
				System.err.println("SAME PARENTS");
				double chance = rng.nextDouble();
				if (chance > 0.90) {
					parents.y.mutate();					
				}
				if (parents.x.id == parents.y.id) {
					System.out.println("Got true duplicate (same ID)");
					System.err.println("TRUE DUPLICATE");
				}
			}
			
			Pair<Individual, Individual> crossoverAttempt = addCrossover(parents);

			while (crossoverAttempt == null) {
				System.out.println("Got null crossoverattempt");
				if (type == "crowdComparison") {
					System.out.println("Ran crowdComparison");
					parents = selectWinnerPairCrowdComparison(adults);	
				} else {
					parents = selectWinnerPair(adults);				
				}
				crossoverAttempt = addCrossover(parents);
			}
			
			repros--;
		}
		mutateAll(children);	
	}
	
	private List<Individual> selectGroup(List<Individual> participants) {
		List<Individual> group = new ArrayList<Individual>(set.getK());
		List<Integer> prevChances = new ArrayList<Integer>();
		if (participants.size() != set.getGenerationSize()) {
			System.out.println("Got wrong number of participants selectGroup");
			System.err.println("WRONG PARTICIPANT NUMBER");
		}
		int chance = -1;
		for (int i=0; i<set.getK(); i++) {
			chance = rng.nextInt(participants.size());
			while (prevChances.contains(chance)) {
				chance = rng.nextInt(participants.size());
			}
			prevChances.add(chance);
			group.add(participants.get(chance));			
		}
		if (group.size() < set.getK()) {
			System.out.println("Too small group made selectGroup");
		}
		return group;
	}
	
	private Pair<Individual, Individual> selectWinnerPair(List<Individual> participants) {
		List<Individual> group1 = selectGroup(participants);
		//List<Individual> group2 = selectGroup(participants);
		Pair<Individual, Individual> winner1 = selectWinners(group1);
		/*
		Individual winner2 = selectWinner(group2);
		while (winner1.getGenotype().equals(winner2.getGenotype())) {
			winner2 = selectWinner(selectGroup(participants));
		}
		*/
		
		return new Pair<Individual, Individual>(winner1.x, winner1.y);
	}
	
	private Pair<Individual, Individual> selectWinnerPairCrowdComparison(List<Individual> participants) {
		List<Individual> group1 = selectGroup(participants);
		//List<Individual> group2 = selectGroup(participants);
		Pair<Individual, Individual> winner1 = selectWinnersCrowdedComparison(group1);
		/*
		Individual winner2 = selectWinnerCrowdedComparison(group2);
		while (winner1.getGenotype().equals(winner2.getGenotype())) {
			winner2 = selectWinnerCrowdedComparison(selectGroup(participants));
		}
		*/
		
		return new Pair<Individual, Individual>(winner1.x, winner1.y);
	}
	
	private Pair<Individual, Individual> selectWinners(List<Individual> group) {
		Individual winner1;
		Individual winner2;
		double chance = rng.nextDouble();
		
		if (chance > set.getEChance()) {
			for (Individual ind : group) {
				ind.setSortVal("rank");
			}
			Collections.sort(group);
			winner1 = group.get(0);
			winner2 = group.get(1);
		} else {
			int rand2 = rng.nextInt(group.size());
			int rand1 = rng.nextInt(group.size());
			winner1 = group.get(rand1);
			while (rand2 == rand1) {
				rand2 = rng.nextInt(group.size());
			}
			winner2 = group.get(rand2);
		}
		return new Pair<Individual, Individual>(winner1, winner2);
	}
	
	private Pair<Individual, Individual> selectWinnersCrowdedComparison(List<Individual> group) {
		List<Individual> group2 = new ArrayList<Individual>();
		Individual winner1;
		Individual winner2;
		double chance = rng.nextDouble();
		
		if (chance > set.getEChance()) {
			for (Individual ind : group) {
				ind.setSortVal("rank");
			}
			Collections.sort(group);
			int bestRank = group.get(0).getRank();
			for (Individual ind : group) {
				if (ind.getRank() == bestRank) {
					group2.add(ind);
				}
			}
			if (group2.size() < 2) {
				winner1 = group.get(0);
				winner2 = group.get(1);
			} else {
				for (Individual ind : group2) {
					ind.setSortVal("crowdDist");
				}
				Collections.sort(group2);
				winner1 = group2.get(group2.size()-1);
				winner2 = group2.get(group2.size()-2);
			}
		} else {
			int rand2 = rng.nextInt(group.size());
			int rand1 = rng.nextInt(group.size());
			winner1 = group.get(rand1);
			while (rand2 == rand1) {
				rand2 = rng.nextInt(group.size());
			}
			winner2 = group.get(rand2);
		}
		return new Pair<Individual, Individual>(winner1, winner2);
		
	}
	
	protected void mutateAll(List<Individual> inds) {
		for (int i=0; i<inds.size(); i++) {
			double chance = rng.nextDouble();
			if (chance < set.getMutateRate()) {
				inds.get(i).mutate();
				}
		}		
	}

	public void createFirstChildren() {
		crowdingDistanceAssignment(adults);
		System.out.println("Crowding distance run");
		nonDominatedSorting(adults);
		reprodTourSel("");
		System.out.println("Updating children");
		updateChildrenFitnesses();
		System.out.println("Updated children");
		
	}

	public List<Individual> getAdults() {
		return adults;
	}
	
	public List<Individual> getChildren() {
		return children;
	}

	public void createChildren() {
		reprodTourSel("crowdComparison");
		updateChildrenFitnesses();
	}
	

	public void updateChildrenFitnesses() {
		for (Individual ind : children) {
			ind.createInvPheno();
			ind.updateFitness();	
		}
		
	}
	
	public static void main(String[] args) {
		Settings set = new Settings();
		Population pop = new Population(set);
		pop.createFirstChildren();
		
		List<Individual> combinedPop = new ArrayList<Individual>(set.getGenerationSize()*2);
		combinedPop.addAll(pop.getAdults());
		System.out.println("Children");
		combinedPop.addAll(pop.getChildren());
		
		pop.adults = new ArrayList<Individual>();
		
		ArrayList<ArrayList<Individual>> sorted = pop.nonDominatedSorting(combinedPop);
		
		System.out.println("Ran nonDominatedSorting");
		
		//Storing pareto front
		List<Individual> paretoFront = sorted.get(0);

		
		for (ArrayList<Individual> front: sorted) {
			pop.adults.addAll(front);
		}
		
		System.out.println(pop.adults.size());
		
		//Storing best for each objective
		
		for (Individual ind: pop.adults) {
			ind.setSortVal("dist");
		}
		Collections.sort(pop.adults);
		Individual bestObj1 = pop.adults.get(0);
		Individual worstObj1 = pop.adults.get(pop.adults.size()-1);
		for (Individual ind: pop.adults) {
			ind.setSortVal("cost");
		}
		Collections.sort(pop.adults);
		Individual bestObj2 = pop.adults.get(0);
		Individual worstObj2 = pop.adults.get(pop.adults.size()-1);
		
		//Crowded-comparison tournament
		pop.createChildren();
		
		System.out.println("Finished");
	}
	
	public static boolean containsGenotype(Collection<Individual> c, String genotype) {
	    for(Individual o : c) {
	        if(o != null && o.getGenotype().equals(genotype)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public void incrementIndividualID() {
		individualIDs++;
	}
	
	public int getIndividualID() {
		return individualIDs;
	}

}
