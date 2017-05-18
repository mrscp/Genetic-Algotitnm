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
	
	public NQueen birth(NQueen parent1, NQueen parent2){
		NQueen child1 = new NQueen(parent1.n);
		NQueen child2 = new NQueen(parent1.n);
		//System.out.println(parent1);
		//System.out.println(parent2);
		int firstPoint = rand.nextInt(parent1.n);
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
			return child2;
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
	
	public NQueen selectParent(NQueen parent1){
		NQueen parent2 = selectParent();
		if(parent1.equals(parent2)){
			return selectParent(parent1);
		}
		return selectParent();
	}
	
	public void crossOver(){
		NQueen[] tempPopulation = new NQueen[n];
		for(int i = 0; i < n; i++){
			NQueen parent1 = selectParent();
			NQueen parent2 = selectParent(parent1);
			tempPopulation[i] = birth(parent1, parent2);
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
		for(int i = 0; i < n; i++){
			//System.out.println(population[i].fitness +"<"+ population[i].n/4);
			//System.out.println(population[i].n*0.04);
			if(population[i].fitness < population[i].n*0.1){
				//System.out.println("yes");
				population[i] = generateBestNeighbor(population[i]);
			}else{
				population[i] = probabilisticNeighbor(population[i]);
			}
			
			population[i].fitness = getQuality(population[i]);
			//System.out.println(population[i]);
			if(population[i].fitness == 0){
				System.out.println("Mutation");
				this.best = population[i];
				return;
			}
		}
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
		//System.out.println("new best selection");
		for(int i = 0; i < temp.n; i++){
			for(int j = 0; j < temp.n; j++){
				temp = new NQueen(other);
				temp.queens[i]=j;
				temp.fitness = getQuality(temp);
				bestp.add(temp);
				//System.out.println("\t"+temp);
			}
		}
		//System.out.println("best: " + bestp.peek());
		ArrayList<NQueen> bests = new ArrayList<NQueen>();
		bests.add(bestp.poll());
		//System.out.println("bests");
		//System.out.println(bestp.peek().fitness + " " + bests.get(0).fitness);
		while(!bestp.isEmpty() && bestp.peek().fitness == bests.get(0).fitness){
			//System.out.println("\t" + bestp.peek());
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