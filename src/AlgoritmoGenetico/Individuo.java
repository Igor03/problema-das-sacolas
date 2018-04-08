package AlgoritmoGenetico;

import java.util.LinkedList;

public class Individuo implements Comparable<Individuo>{
	
	public int qtd_cromossomos; // Quantidade de sacolas do problema 
	public double aptidao; // Aptidao do individuo
	public LinkedList<Gene> genes = new LinkedList<>(); // Itens disponiveis para o individuo
	public LinkedList<Cromossomo> cromossomos = new LinkedList<>(); // Sacolas do problema
		
	public Individuo(int qtd_cromossomos) {
		super();
		this.qtd_cromossomos = qtd_cromossomos;
	}
	
	// Para efetuar ordenacao de forma mais simples
	@Override
	public int compareTo(Individuo individuo) {
		// TODO Auto-generated method stub
		if (aptidao > individuo.aptidao)
			return -1;
		if (aptidao < individuo.aptidao)
			return 1;
		return 0;
	}
	
	public String toString(){
		return "Aptidão: " + aptidao;
	}
}