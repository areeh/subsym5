package logic;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Individual implements Comparable<Individual>{
	
	int id;
	Settings set;
	public String genotype;
	//List<Integer> genotype;
	List<Integer> convertedGeno;
	List<Integer> invPhenotype;
	List<Integer> phenotype;
	Random rng;
	private int distFit;
	private int costFit;
	private int rank;
	private double sortVal;
	private double crowdDist;
	
	/**
	 * Constructor for individual with random genotype
	 * 
	 * @param set Settings for the simulation
	 */

	/*
	public Individual(Settings set, Random rng, int id) {
		this.rng = rng;
		this.set = set;
		genotype = new ArrayList<Integer>(48);
		for (int i=0; i<48; i++) {
			genotype.add(rng.nextInt(set.getMaxGenotypeValue()));
		}
		this.id = id;
	}
	
	
	
	public Individual(List<Integer> genotype, Settings set, Random rng, int id) {
		this.rng = rng;
		this.set = set;
		this.genotype = new ArrayList<Integer>(genotype);
		if (genotype == null) {
			System.out.println("Genotype is null");
		}
		this.id = id;
	}
	*/
	
	public Individual(String genotype, Settings set, Random rng, int id) {
		this.rng = rng;
		this.set = set;
		this.genotype = new String(genotype);
		this.id = id;
		crowdDist = -1;
	}

	public Individual(Settings set, Random rng, int id) {
		this.rng = rng;
		this.set = set;
		this.id = id;
		phenotype = new ArrayList<Integer>(48);
		for (int i=0; i<48; i++) {
			phenotype.add(i+1);
		}
		Collections.shuffle(phenotype, rng);
		phenotype = inverse(phenotype);
		genotype = invConvXBit(phenotype, set.getBitSize());
		crowdDist = -1;
	}
	
	private static List<Integer> inverse(List<Integer> permutation) {
		Set test = new HashSet<Integer>(permutation);
		if (test.size() != permutation.size()) {
			System.out.println("duplicates found");
		}
		
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
	
	public static String pad(String in, int digits) {
		StringBuffer temp = new StringBuffer(in);
		int remainingInserts = digits - in.length();
		
		while (remainingInserts-- > 0) {
			temp.insert(0, "0");
		}
		
		return temp.toString();
	}
	
	//Efficiency?
	public void develop() {
		convertedGeno = convXBit(genotype, set.getBitSize());
	}
	
	public List<Integer> convXBit(String binary, int bitSize) {
		if (binary.length() % bitSize != 0) {
			System.err.println("Genotype size not divisible by bitsize");
			System.out.println(binary.length());
			System.out.println(bitSize);
			return null;
		}
		int len = binary.length()/bitSize;		
		List<Integer> res = new ArrayList<Integer>(len);
		for (int i=0; i<len; i++) {
    		Integer digit = Integer.parseInt(binary.substring(i*bitSize,
    		        i*bitSize+bitSize), 2);
    		digit = digit % (48-i);
    		res.add(digit);			
	}
		return res;
	}
	
	public String invConvXBit(List<Integer> ints, int bitSize) {
		String res = "";
		
		if (ints.size()*bitSize != set.getGenotypeSize()) {
			System.err.println("phenotype times bitsize incorrect genosize");
			System.out.println(ints.size());
			System.out.println(bitSize);
			return null;
		}
		
		for (Integer digit : ints) {
			String temp = Integer.toBinaryString(digit);
			temp = pad(temp, set.getBitSize());
			res+= temp;
		}
		
		if (res.length() != set.getGenotypeSize()) {
			System.out.println("invConvXBit returned wrong binary string length");
			System.err.println("WRONG OUTPUT INVCONVXBIT");
		}
		
		return res;
	}
	
	/**
	 * Creates pheno from genotype.
	 * Sets current phenotype to the created one.
	 * 
	 * @return the phenotype created.
	 */
	
	/*
	public List<Integer> createPheno() {
		List<Integer> citiesToAdd = new ArrayList<Integer>(set.getCities());
		int currentIndex = 0;
		List<Integer> phenotype = new ArrayList<Integer>(48);
		for (int val : genotype) {
			currentIndex += val;
			while (currentIndex > citiesToAdd.size() -1) {
				if (citiesToAdd.size() == 0) {
					this.phenotype = phenotype;
					return phenotype;
				}
				currentIndex-=citiesToAdd.size();
			}
			phenotype.add(citiesToAdd.remove(currentIndex));
			
		}
		this.phenotype = phenotype;
		return phenotype;
	}
	*/
	
	/*
	public List<Integer> createPheno() {
		develop();
		List<Integer> citiesToAdd = new ArrayList<Integer>(set.getCities());
		int currentIndex = 0;
		List<Integer> phenotype = new ArrayList<Integer>(48);
		for (int val : convertedGeno) {
			currentIndex += val;
			while (currentIndex > citiesToAdd.size() -1) {
				currentIndex-=citiesToAdd.size();
			}
			phenotype.add(citiesToAdd.remove(currentIndex));
			
		}
		this.phenotype = phenotype;
		return phenotype;
	}
	*/
	
	public List<Integer> createInvPheno() {
		invPhenotype = convXBit(genotype, set.getBitSize());
		return invPhenotype;
	}
	
	public List<Integer> createPheno() {
		phenotype = unInverse(invPhenotype);
		return phenotype;
	}

	/*
	public void mutate() {
		int index = rng.nextInt(genotype.size());
		genotype.set(index, rng.nextInt(set.getMaxGenotypeValue()));				
	}
	*/

	public void mutate() {
		int target = rng.nextInt(genotype.length());
		
		//dumb bit flip
		StringBuilder temp = new StringBuilder(genotype);
		temp.setCharAt(target, genotype.charAt(target) == '0' ? '1' : '0');
		
		genotype = temp.toString();
	}
	
	/*
	//per bit
	public void mutate() {
		double chance = rng.nextDouble();
		
		StringBuilder temp = new StringBuilder(genotype);
		for (int i=0; i<genotype.length(); i++) {
			chance = rng.nextDouble();
			if (chance < set.getMutateRate()) {
				temp.setCharAt(i, genotype.charAt(i) == '0' ? '1' : '0');
			}
		}		
		genotype = temp.toString();
	}
	*/
	
	public void updateFitness() {
		createPheno();
		costFitness();
		distanceFitness();	
	}

	/**
	 * 
	 * @return
	 */
	public int costFitness() {
		List<Integer> costlist = new ArrayList<Integer>(48);
		int[][] costs = set.getCosts();
		for (int i=0; i<phenotype.size(); i++) {
			if (phenotype.size() != 48) {
				System.out.println("Wrong phenotype size");
			}
			//Distance from last to first city
			if (i == phenotype.size()-1) {
				if (phenotype.get(i) > phenotype.get(0)) {
					costlist.add(costs[phenotype.get(i)-1][phenotype.get(0)-1]);
				} else {
					costlist.add(costs[phenotype.get(0)-1][phenotype.get(i)-1]);
				}
			} else {
				//rest of cities
				if (phenotype.get(i) > phenotype.get(i+1)) {
					costlist.add(costs[phenotype.get(i)-1][phenotype.get(i+1)-1]);
				} else {
					costlist.add(costs[phenotype.get(i+1)-1][phenotype.get(i)-1]);
				}
			}
		}
		if (costlist.size() != 48) {
			throw new RuntimeException("list of costs did not have 48 values");
		}
		
		int sum = costlist.stream().mapToInt(Integer::intValue).sum();
		costFit = sum;
		return sum;
	}
	
	/**
	 * 
	 * @return
	 */
	public int distanceFitness() {
		List<Integer> distancelist = new ArrayList<Integer>(48);
		int[][] dists = set.getDists();
		for (int i=0; i<phenotype.size(); i++) {
			if (phenotype.size() != 48) {
				System.out.println("Wrong phenotype size");
			}
			//Distance from last to first city
			if (i == phenotype.size()-1) {
				if (phenotype.get(i) > phenotype.get(0)) {
					distancelist.add(dists[phenotype.get(i)-1][phenotype.get(0)-1]);
				} else {
					distancelist.add(dists[phenotype.get(0)-1][phenotype.get(i)-1]);
				}
			} else {
				//rest of cities
				if (phenotype.get(i) > phenotype.get(i+1)) {
					distancelist.add(dists[phenotype.get(i)-1][phenotype.get(i+1)-1]);
				} else {
					distancelist.add(dists[phenotype.get(i+1)-1][phenotype.get(i)-1]);
				}
			}
		}
		if (distancelist.size() != 48) {
			throw new RuntimeException("list of distances did not have 48 values");
		}
		
		int sum = distancelist.stream().mapToInt(Integer::intValue).sum();
		distFit = sum;
		return sum;
	}
	
	public int getCostFitness() {
		return costFit;
	}
	
	public int getDistanceFitness() {
		return distFit;
	}

	public List<Integer> getPhenotype() {
		if (phenotype == null) {
			System.err.println("Phenotype not created at getPhenotype");
		}
		return phenotype;
	}
	
	/*
	public List<Integer> getGenotype() {
		if (genotype == null) {
			System.err.println("Genotype not created at getGenotype");
		}
		return genotype;
	}
	*/
	
	public String getGenotype() {
		return genotype;
	}
	
	public double getSortVal() {
		return sortVal;
	}
	
	public void setSortVal(String type) {
		if (type == "dist") {
			sortVal = distFit;
		} else if (type == "cost") {
			sortVal = costFit;
		} else if (type == "crowdDist") { 
			sortVal = crowdDist;
		} else if (type == "rank") {
			sortVal = rank;
		} else {
			System.err.println("Incorrect string type setSortVal");
		}
	}
	
	public void setSortVal(int val) {
		sortVal = val;
	}
	
	public void setCrowdDist(double crowdDist) {
		this.crowdDist = crowdDist;
	}
	
	public double getCrowdDist() {
		return crowdDist;
	}
	

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	

	@Override
	public int compareTo(Individual ind) {
		if (sortVal > ind.getSortVal()) {
			return 1;
		} else if (sortVal == ind.getSortVal()) {
			return 0;
		} else {
			return -1;
		}
	}

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(id).
            toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof Individual))
            return false;
        if (obj == this)
            return true;

        Individual rhs = (Individual) obj;
        return new EqualsBuilder().
            // if deriving: appendSuper(super.equals(obj)).
            append(id, rhs.id).
            isEquals();
    }
    
    public int getId() {
    	return id;
    }


}
