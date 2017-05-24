package algorithm;

import java.util.concurrent.TimeUnit;

public class GeneticAlgorithm {
	public String getTime(long totalTime){
		if(totalTime > 100)
			return ("Time: "+String.format("%d min, %d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(totalTime),
			    TimeUnit.MILLISECONDS.toSeconds(totalTime) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime))
			));
		else{
			return ("Time: " + totalTime + " ms");
		}
	}
	public GeneticAlgorithm(){
		int n = 200;
		int population = 400;
		long startTime = System.currentTimeMillis();
		Population pop = new Population(population);
		pop.generateRandomPopulation(n);
		int generation = 0;
		while(true){
			NQueen sol = pop.calculateFitness();
			//pop.print();
			if(pop.best.fitness == 0 || sol != null){
				System.out.println("\nSolution: \n" + pop.best);
				break;
			}
			
			System.out.println(generation + " " + pop.n + ": " + pop.best);
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println(getTime(totalTime));
			
			pop.crossOver();
			
			pop.mutation();
			
			generation++;
		}
		
		System.out.println(generation);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(getTime(totalTime));
	}
	
	public static void main(String args[]){
		new GeneticAlgorithm();
	}
}
