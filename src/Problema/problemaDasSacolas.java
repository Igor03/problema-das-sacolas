package Problema;

import java.util.LinkedList;
import java.util.Random;

import AlgoritmoGenetico.Cromossomo;
import AlgoritmoGenetico.Gene;

public class problemaDasSacolas {

	public int qtd_sacolas;
	public int qtd_itens;

	public int[] pesos_possiveis;
	public int[] capacidades;
	
	Random rand = new Random();

	public LinkedList<Cromossomo> sacolas = new LinkedList<>();
	public LinkedList<Gene> itens = new LinkedList<>();
	
	public problemaDasSacolas(int qtd_sacolas, int qtd_itens, int[] pesos_possiveis, int[] capacidades) {
		super();
		this.qtd_sacolas = qtd_sacolas;
		this.qtd_itens = qtd_itens;
		this.pesos_possiveis = pesos_possiveis;
		this.capacidades = capacidades;
		inicializar();
	}

	// Inicializa o problema no construtor
	private void inicializar() {
		LinkedList<Integer> capacidades_temp = new LinkedList<>();
		for (int i = 0; i < capacidades.length; i++)
			capacidades_temp.add(capacidades[i]);
		for (int i = 0; i < qtd_sacolas; i++) 
			sacolas.add(new Cromossomo(capacidades_temp.remove(rand.nextInt(capacidades.length - i))));
		for (int i = 0; i < qtd_itens; i++) {
			itens.add(new Gene(i, pesos_possiveis[rand.nextInt(pesos_possiveis.length)]));
		}
	}
	public static void main (String[] args) {
		
		int[] pesos_possiveis = { 1, 3, 5, 7, 11, 13, 17, 19 };
		int[] capacidades = {23, 29, 31, 37, 41, 43, 47 };
		int qtd_sacolas = 3;
		int qtd_itens = 30;
		
		Random rand = new Random();
		System.out.println(rand.nextInt(10));
		problemaDasSacolas problema = new problemaDasSacolas(qtd_sacolas, qtd_itens, pesos_possiveis, capacidades);
		System.out.println(problema.itens);
		
		for (int i = 0; i < problema.qtd_sacolas; i++) {
			System.out.println(problema.sacolas.get(i).capacidade);
		}
	}
}
