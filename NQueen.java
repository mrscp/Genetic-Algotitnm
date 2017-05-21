package algorithm;

import java.util.Arrays;

public class NQueen implements Comparable<NQueen>{
	int n;
	int [] queens;
	int [] errors;
	double fitness;
	
	public NQueen(int n) {
		this.n = n;
		queens = new int[n];
	}
	
	public NQueen(NQueen other) {
		this.n = other.n;
		this.queens=new int[n];
		for(int i=0;i<n;i++)
			this.queens[i]=other.queens[i];
	}
	
	@Override
	public String toString() {
		return "NQueen [fitness=" + fitness + ", n="+this.n+", queens=" + Arrays.toString(queens) + "]";
	}
	
	@Override
	public boolean equals(Object o){
		NQueen nqueen = (NQueen)o;
		for(int i = 0; i < n; i++){
			if(queens[i] != nqueen.queens[i]){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int compareTo(NQueen o) {
		return Double.compare(this.fitness, o.fitness);
	}
}
