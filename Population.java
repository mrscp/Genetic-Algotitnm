package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class Population {
	int n;
	NQueen [] population;
	NQueen best;
	Random rand = new Random();
	double mutationRate = 0.01;
	
	public Population(int n){
		this.n = n;
		this.population = new NQueen[n];
	}
	/*
	public Population(Population other){
		this.n = other.n;
		this.population = new NQueen[n];
		for(int i = 0; i < n; i++){
			this.population[i] = other.population[i];
		}
	}
	*/
	public NQueen generateRandomNQueen(int n){
		NQueen temp = new NQueen(n);
		for(int i = 0; i < n; i++){
			temp.queens[i] = rand.nextInt(n);
		}
		return temp;
	}
	
	public void generateRandomPopulation(int n){
		for(int i = 0; i < this.n; i++){
			population[i] = generateRandomNQueen(n);
		}
	}
	
	public int getQuality(NQueen temp)
	{
		int q = 0;
		for(int i=0;i<temp.n;i++)
		{
			for(int j=i+1;j<temp.n;j++)
			{
				if(temp.queens[i]==temp.queens[j])
					q++;
				else if (Math.abs(i-j) == Math.abs(temp.queens[i]-temp.queens[j]))
					q++;
			}
		}
		//int quality = temp.n-q;
		//return quality < 0?0:quality;
		return q > temp.n?temp.n:q;
	}
	
	public int[] getError(NQueen temp)
	{
		int [] q = new int[temp.n];
		for(int i=0;i<temp.n;i++)
		{
			for(int j=i+1;j<temp.n;j++)
			{
				if(temp.queens[i]==temp.queens[j])
					q[i] = 1;
				else if (Math.abs(i-j) == Math.abs(temp.queens[i]-temp.queens[j]))
					q[i] = 1;;
			}
		}
		//int quality = temp.n-q;
		//return quality < 0?0:quality;
		return q;
	}
	
	public NQueen calculateFitness(){
		if(best == null || best.fitness != 0.0){
			population[0].fitness = getQuality(population[0]);
			best = population[0];
		}else{
			return (best);
		}
		for(int i = 1; i < this.n; i++){
			population[i].fitness = getQuality(population[i]);
			if(population[i].fitness < best.fitness){
				best = population[i];
			}
		}
		return null;
	}
	
	public NQueen normalBirth(NQueen parent1, NQueen parent2){
		NQueen child1 = new NQueen(parent1.n);
		NQueen child2 = new NQueen(parent1.n);
		//System.out.println(parent1);
		//System.out.println(parent2);
		int firstPoint = rand.nextInt(parent1.n);
		
		if(parent1.fitness < parent1.n * 0.5 && parent1.fitness > parent1.n * 0.05){
			parent1.errors = getError(parent1);
			for(int j = 0; j < parent1.n; j++){
				if(parent1.errors[j] == 1){
					firstPoint = j;
					break;
				}
			}
			//System.out.println(Arrays.toString(parent1.errors));
			firstPoint = rand.nextInt(firstPoint+1);
		}
		
		int secondPoint = firstPoint + rand.nextInt(parent1.n-firstPoint);
		for(int j = 0; j < child1.n; j++){
			if(j <= firstPoint || j >= secondPoint){
				child1.queens[j] = parent1.queens[j];
				child2.queens[j] = parent2.queens[j];
			}else{
				child2.queens[j] = parent1.queens[j];
				child1.queens[j] = parent2.queens[j];
			}
		}
		child1.fitness = getQuality(child1);
		child2.fitness = getQuality(child2);

		if(child1.fitness > child2.fitness){
			//System.out.println("child2 " + parent1.fitness + " " + parent2.fitness + " " + child1.fitness + " " + child2.fitness);
			return child2;
		}else{
			//System.out.println("child1 " + parent1.fitness + " " + parent2.fitness + " " + child1.fitness + " " + child2.fitness);
		}
		return child1;
	}
	
	public NQueen hybridBirth(NQueen parent1, NQueen parent2){
		NQueen child1 = new NQueen(parent1.n);
		NQueen child2 = new NQueen(parent1.n);
		//System.out.println(parent1);
		//System.out.println(parent2);
		parent1.errors = getError(parent1);
		parent2.errors = getError(parent2);
		for(int j = 0; j < child1.n; j++){
			if(parent1.errors[j] == 1 && parent2.errors[j] == 0){
				child1.queens[j] = parent1.queens[j];
				child2.queens[j] = parent2.queens[j];
			}else{
				child2.queens[j] = parent1.queens[j];
				child1.queens[j] = parent2.queens[j];
			}
		}
		
		child1.fitness = getQuality(child1);
		child2.fitness = getQuality(child2);

		if(child1.fitness > child2.fitness){
			//System.out.println("child2 " + parent1.fitness + " " + parent2.fitness + " " + child1.fitness + " " + child2.fitness);
			return child2;
		}else{
			//System.out.println("child1 " + parent1.fitness + " " + parent2.fitness + " " + child1.fitness + " " + child2.fitness);
		}
		return child1;
	}
	
	public NQueen selectParent(){
		int index = rand.nextInt(this.n);
		int probability = rand.nextInt(population[index].n+1);
		
		if(probability > population[index].fitness-1){
			return population[index];
		}
		
		return selectParent();
	}
	
	int interval = 0;
	public NQueen selectParent(NQueen parent1){
		NQueen parent2 = selectParent();
		if(parent1.equals(parent2) && interval < 100){
			interval++;
			return selectParent(parent1);
		}
		interval = 0;
		return selectParent();
	}
	
	public void crossOver(){
		NQueen[] tempPopulation = new NQueen[n];
		for(int i = 0; i < n; i++){
			NQueen parent1 = selectParent();
			NQueen parent2 = selectParent(parent1);
			if(parent1.fitness < parent1.n * 0.5){
				tempPopulation[i] = hybridBirth(parent1, parent2);
			}else{
				//System.out.println("Hybrid");
				tempPopulation[i] = hybridBirth(parent1, parent2);
			}
			
			tempPopulation[i].fitness = getQuality(tempPopulation[i]);
			if(tempPopulation[i].fitness == 0){
				System.out.println("Cross");
				this.best = tempPopulation[i];
				return ;
			}
		}
		population = tempPopulation;
	}
	
	public void mutation(){
		boolean gotOne = false;
		for(int i = 0; i < n; i++){
			double probability = Math.random();
			if(probability < 0.3){
				population[i] = generateRandomNeighbor(population[i]);
			}else{
				if(population[i].fitness <= population[i].n*0.01 || this.n <= 10){
					gotOne = true;
					population[i] = generateBestNeighbor(population[i]);
				}else if(population[i].fitness < population[i].n*0.05 && probability < 0.5){
					gotOne = true;
					population[i] = generateErrorFixingBestNeighbor(population[i]);
				}else{
					population[i] = generateErrorFixingNeighbor(population[i]);
				}
			}
			//population[i] = generateErrorFixingNeighbor(population[i]);
			population[i].fitness = getQuality(population[i]);
			//System.out.println(population[i]);
			if(population[i].fitness == 0){
				System.out.println("Mutation");
				this.best = population[i];
				return;
			}
		}
		if(gotOne == true && this.n > 10){
			this.n = (int) (this.n * 0.90);
		}
	}
	
	public NQueen generateErrorFixingNeighbor(NQueen other)
	{
		NQueen temp = new NQueen(other);
		temp.errors = getError(temp);
		//System.out.println(Arrays.toString(temp.errors));
		for(int j = 0; j < temp.n; j++){
			if(temp.errors[j] == 1){
				int rv = rand.nextInt(temp.n);
				temp.queens[j] = rv;
			}
		}
		return temp;
	}
	
	public NQueen generateErrorFixingBestNeighbor(NQueen other)
	{
		NQueen temp = new NQueen(other);
		temp.errors = getError(temp);
		PriorityQueue<NQueen> bestp = new PriorityQueue<NQueen>();

		for(int i = 0; i < temp.n; i++){
			if(temp.errors[i] == 1){
				for(int j = 0; j < temp.n; j++){
					temp = new NQueen(other);
					temp.queens[i]=j;
					temp.fitness = getQuality(temp);
					temp.errors = getError(temp);
					bestp.add(temp);
				}
			}
			
		}

		ArrayList<NQueen> bests = new ArrayList<NQueen>();
		bests.add(bestp.poll());
		while(!bestp.isEmpty() && bestp.peek().fitness == bests.get(0).fitness){
			bests.add(bestp.poll());
		}
		
		return bests.get(rand.nextInt(bests.size()));
	}
	
	public NQueen probabilisticNeighbor(NQueen other){
		NQueen temp = new NQueen(other);
		for(int j = 0; j < temp.n; j++){
			double mutationProbability = Math.random();
			if(mutationProbability < mutationRate){
				int rq = rand.nextInt(temp.n);
				int rv = rand.nextInt(temp.n);
				temp.queens[rq] = rv;
			}
		}
		return temp;
	}
	
	public NQueen generateRandomNeighbor(NQueen other){
			NQueen temp = new NQueen(other);
			int rq=rand.nextInt(temp.n);
			int rv=rand.nextInt(temp.n);
			temp.queens[rq]=rv;
			
			return temp;
	}
	
	public NQueen generateBestNeighbor(NQueen other)
	{
		NQueen temp = new NQueen(other);
		PriorityQueue<NQueen> bestp = new PriorityQueue<NQueen>();

		for(int i = 0; i < temp.n; i++){
			for(int j = 0; j < temp.n; j++){
				temp = new NQueen(other);
				temp.queens[i]=j;
				temp.fitness = getQuality(temp);
				bestp.add(temp);
			}
		}

		ArrayList<NQueen> bests = new ArrayList<NQueen>();
		bests.add(bestp.poll());
		while(!bestp.isEmpty() && bestp.peek().fitness == bests.get(0).fitness){
			bests.add(bestp.poll());
		}
		
		return bests.get(rand.nextInt(bests.size()));
	}
	
	public void print(){
		for(int i = 0; i < n; i++){
			System.out.println(population[i].fitness + " " + Arrays.toString(population[i].queens));
		}
	}
}
