package AlgoritmoGenetico;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import Problema.problemaDasSacolas;

public class AG {

	public problemaDasSacolas problema;
	public int qtd_geracoes;
	public int qtd_individuos;
	public LinkedList<Individuo> populacao = new LinkedList<>();
	public LinkedList<Individuo> novaPopulacao = new LinkedList<>();
	Random rand = new Random();

	public AG(problemaDasSacolas problema, int qtd_geracoes, int qtd_individuos) {
		super();
		this.problema = problema;
		this.qtd_geracoes = qtd_geracoes;
		this.qtd_individuos = qtd_individuos;
	}

	public void gerarPopulacao() {
		for (int i = 0; i < qtd_individuos; i++) {
			populacao.add(gerarIndividuo());
		}
		Collections.sort(populacao);
	}

	public double aptidao(Individuo individuo) {
		int peso_atual = 0;
		int capacidade_maxima = 0;
		for (int i = 0; i < individuo.qtd_cromossomos; i++)
			capacidade_maxima += individuo.cromossomos.get(i).capacidade;
		for (int i = 0; i < individuo.qtd_cromossomos; i++)
			peso_atual += individuo.cromossomos.get(i).verificaValidade();
		return (double) peso_atual / capacidade_maxima;

	}

	public void elitismo(int elitismo) {
		for (int i = 0; i < elitismo; i++) {
			novaPopulacao.add(populacao.get(i));
		}
	}

	// Selecao baseada em torneio
	public Individuo selecao() {
		Individuo individuo1;
		Individuo individuo2;
		do {
			individuo1 = populacao.get(rand.nextInt(populacao.size()));
			individuo2 = populacao.get(rand.nextInt(populacao.size()));
		} while ((individuo1.equals(individuo2)));
		if (individuo1.aptidao <= individuo2.aptidao)
			return individuo2;
		else
			return individuo1;
	}

	// Crossover
	public void reroducao(int qtd_ind_resultantes) {
		Individuo individuo1;
		Individuo individuo2;
		Individuo filho1;
		Individuo filho2;
		for (int i = 0; i < qtd_ind_resultantes / 2; i++) {
			do {
				do {
					individuo1 = selecao();
					individuo2 = selecao();
				} while ((individuo1.equals(individuo2)));
				filho1 = new Individuo(problema.qtd_sacolas);
				// filho1.cromossomos.add(deepCopy3(individuo2.cromossomos.get(0)));
				// filho1.cromossomos.add(deepCopy3(individuo1.cromossomos.get(1)));
				// filho1.cromossomos.add(deepCopy3(individuo2.cromossomos.get(2)));
				// filho1.aptidao = aptidao(filho1);
				filho2 = new Individuo(problema.qtd_sacolas);
				// filho2.cromossomos.add(deepCopy3(individuo1.cromossomos.get(0)));
				// filho2.cromossomos.add(deepCopy3(individuo2.cromossomos.get(1)));
				// filho2.cromossomos.add(deepCopy3(individuo1.cromossomos.get(2)));
				// filho2.aptidao = aptidao(filho2);

				// Crossover mais geral
				for (int j = 0; j < problema.qtd_sacolas; j++) {
					if (j % 2 == 0)
						filho1.cromossomos.add(deepCopy3(individuo2.cromossomos.get(j)));
					else
						filho1.cromossomos.add(deepCopy3(individuo1.cromossomos.get(j)));
				}
				filho1.aptidao = aptidao(filho1);
				for (int j = 0; j < problema.qtd_sacolas; j++) {
					if (j % 2 == 0)
						filho2.cromossomos.add(deepCopy3(individuo1.cromossomos.get(j)));
					else
						filho2.cromossomos.add(deepCopy3(individuo2.cromossomos.get(j)));
				}
				filho2.aptidao = aptidao(filho2);

			} while (!verificarIndividuo(filho1) && !verificarIndividuo(filho2));
			validarIndividuo(filho1);
			validarIndividuo(filho2);
			novaPopulacao.add(filho1);
			novaPopulacao.add(filho2);
		}
	}

	public void mutacao(int qtd_ind_mutados) {
		Individuo individuo;
		int index;
		for (int i = 0; i < qtd_ind_mutados; i++) {
			individuo = selecao();
			index = rand.nextInt(individuo.qtd_cromossomos);
			individuo.genes.add(individuo.cromossomos.get(index).genes
					.remove(rand.nextInt(individuo.cromossomos.get(index).genes.size())));
			individuo.cromossomos.get(index).genes.add((individuo.genes.remove(rand.nextInt(individuo.genes.size()))));
			while (individuo.cromossomos.get(index).verificaValidade() == -1) {
				individuo.genes.add(individuo.cromossomos.get(index).genes
						.remove(rand.nextInt(individuo.cromossomos.get(index).genes.size())));
				individuo.cromossomos.get(index).genes
						.add((individuo.genes.remove(rand.nextInt(individuo.genes.size()))));
			}
			individuo.aptidao = aptidao(individuo);
			novaPopulacao.add(individuo);
		}
	}

	public Individuo[] executar(int qtd_individuos) {
		Individuo[] melhores_individuos = new Individuo[qtd_individuos];
		gerarPopulacao();
		for (int i = 0; i < qtd_geracoes; i++) {
			elitismo((int) (this.qtd_individuos * 0.1));
			reroducao((int) (this.qtd_individuos * 0.6));
			mutacao((int) (this.qtd_individuos * 0.3));
			populacao = deepCopy4(novaPopulacao);
			Collections.sort(populacao); // Forca os melhores individuos virem primeiro
			novaPopulacao.clear();
		}
		for (int i = 0; i < qtd_individuos; i++)
			melhores_individuos[i] = populacao.get(i);
		return melhores_individuos;
	}

	/* FUNCOES AUXILIARES */

	private Individuo gerarIndividuo() {
		Individuo individuo = new Individuo(problema.qtd_sacolas);
		individuo.genes = deepCopy1(problema.itens);
		individuo.cromossomos = deepCopy2(problema.sacolas);
		for (int i = 0; i < problema.qtd_sacolas; i++) {
			while (individuo.cromossomos.get(i).verificaValidade() != -1) {
				Collections.shuffle(individuo.genes);
				individuo.cromossomos.get(i).genes.add(individuo.genes.removeFirst());
			}
			individuo.genes.add(individuo.cromossomos.get(i).genes.removeLast());
			individuo.aptidao = aptidao(individuo);

		}
		return individuo;
	}

	private void validarIndividuo(Individuo individuo) {
		LinkedList<Gene> temp = new LinkedList<>();
		for (int i = 0; i < individuo.qtd_cromossomos; i++) {
			for (int j = 0; j < individuo.cromossomos.get(i).genes.size(); j++) {
				temp.add(individuo.cromossomos.get(i).genes.get(j));
			}
		}
		for (int i = 0; i < temp.size(); i++) {
			for (int j = 0; j < individuo.genes.size(); j++) {
				if (temp.get(i).id == individuo.genes.get(j).id)
					individuo.genes.remove(j);
			}
		}
	}

	private boolean verificarIndividuo(Individuo individuo) {
		LinkedList<Integer> ids = new LinkedList<>();
		for (int i = 0; i < individuo.qtd_cromossomos; i++) {
			for (int j = 0; j < individuo.cromossomos.get(i).genes.size(); j++) {
				ids.add(individuo.cromossomos.get(i).genes.get(j).id);
			}
		}
		Set<Integer> set = new HashSet<Integer>(ids);
		if (set.size() < ids.size())
			return false;
		return true;
	}

	// Copia genes (Itens)
	private LinkedList<Gene> deepCopy1(LinkedList<Gene> genes) {
		LinkedList<Gene> temp = new LinkedList<>();
		for (int i = 0; i < genes.size(); i++)
			temp.add(new Gene(new Integer(genes.get(i).id), new Integer(genes.get(i).peso)));
		return temp;
	}

	// Copia cromossomos (Sacolas)
	private LinkedList<Cromossomo> deepCopy2(LinkedList<Cromossomo> cromossomos) {
		LinkedList<Cromossomo> temp = new LinkedList<>();
		for (int i = 0; i < cromossomos.size(); i++) {
			temp.add(new Cromossomo(new Integer(cromossomos.get(i).capacidade)));
			temp.get(i).genes = deepCopy1(cromossomos.get(i).genes);
		}
		return temp;
	}

	// Copia um unico cromossomo
	private Cromossomo deepCopy3(Cromossomo cromossomo) {
		Cromossomo temp = new Cromossomo(new Integer(cromossomo.capacidade));
		temp.genes = deepCopy1(cromossomo.genes);
		return temp;
	}

	// Copia população
	private LinkedList<Individuo> deepCopy4(LinkedList<Individuo> populacao) {
		LinkedList<Individuo> temp = new LinkedList<>();
		for (int i = 0; i < populacao.size(); i++) {
			temp.add(new Individuo(new Integer(populacao.get(i).qtd_cromossomos)));
			temp.get(i).cromossomos = deepCopy2(populacao.get(i).cromossomos);
			temp.get(i).genes = deepCopy1(populacao.get(i).genes);
			temp.get(i).aptidao = populacao.get(i).aptidao;
		}
		return temp;
	}
	
	public void mostrarSolucao(int qtd_individuos) {
		// Collections.shuffle(populacao);
		for (int i = 0; i < qtd_individuos; i++) {
			System.out.println("APTIDAO DO INDIVIDUO = " + populacao.get(i).aptidao);
			for (int j = 0; j < problema.qtd_sacolas; j++) {
				System.out.println(populacao.get(i).cromossomos.get(j).capacidade + " - "
						+ populacao.get(i).cromossomos.get(j).verificaValidade() + " - "
						+ populacao.get(i).cromossomos.get(j).genes.size() + " - "
						+ populacao.get(i).cromossomos.get(j).genes);
			}
		}
	}

	public static void main(String[] args) {

		/* Definindo parametros do problema */
		int[] pesos_possiveis = { 1, 3, 5, 7, 11, 13, 17, 19 };
		int[] capacidades = { 23, 29, 31, 37, 41, 43, 47 };
		int qtd_sacolas = 3; // Observe que as solocas devem possuir capacidades
								// diferentes
		int qtd_itens = 30;
		int realizacoes = 10;

		// Instanciando o problema
		problemaDasSacolas problema;
		// Instanciando o AG
		AG ag;

		// Executando realizacoes
		for (int k = 0; k < realizacoes; k++) {
			problema = new problemaDasSacolas(qtd_sacolas, qtd_itens, pesos_possiveis, capacidades);
			// gerações, individuos
			ag = new AG(problema, 50, 100);
			System.out.println("########################### REALIZACAO " + (k + 1) + " ###########################");
			ag.executar(5); // Executa o AG e seleciona as 5 melhoes solucoes
			ag.mostrarSolucao(2);
			System.out.println("####################################################################");
		}
	}
}
