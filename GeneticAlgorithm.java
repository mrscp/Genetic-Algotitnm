package algorithm;

import java.util.concurrent.TimeUnit;

public class GeneticAlgorithm {
	public GeneticAlgorithm(){
		int n = 100;
		int population = 200;
		long startTime = System.currentTimeMillis();
		Population pop = new Population(population);
		pop.generateRandomPopulation(n);
		int generation = 0;
		while(generation < 1000){
			NQueen sol = pop.calculateFitness();
			
			if(pop.best.fitness == 0 || sol != null){
				System.out.println("\nSolution: \n" + pop.best);
				break;
			}
			
			System.out.println(generation + ": " + pop.best);
			
			pop.crossOver();
			
			pop.mutation();
			
			generation++;
		}
		
		System.out.println(generation);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		if(totalTime > 100)
		System.out.println("Time: "+String.format("%d min, %d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(totalTime),
			    TimeUnit.MILLISECONDS.toSeconds(totalTime) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime))
			));
		else{
			System.out.println("Time: " + totalTime + " ms");
		}
	}
	
	public static void main(String args[]){
		new GeneticAlgorithm();
	}
}
