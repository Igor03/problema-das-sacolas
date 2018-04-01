package AlgoritmoGenetico;

import java.util.LinkedList;

public class Cromossomo {

	public int capacidade;
	public LinkedList<Gene> genes = new LinkedList<>();

	public Cromossomo(int capacidade) {
		super();
		this.capacidade = capacidade;
	}

	// Verifica se a capacidade da sacola foi excedida
	// Returna -1 se o peso da sacola for excedido.
	public int verificaValidade() {
		int peso_atual = 0;
		for (int i = 0; i < genes.size(); i++) {
			peso_atual += genes.get(i).peso;
			if (peso_atual > capacidade)
				return -1;
		}
		return peso_atual;
	}

}
