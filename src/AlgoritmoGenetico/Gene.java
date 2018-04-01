package AlgoritmoGenetico;

/*
 * Esta classe simula um item. Portanto ela deve possuir dois atributos essenciais que sao
 * o id, para que nao existam itens repetidos entre sacolas, e o peso deste item.
 * 
 * */

public class Gene {

	public int id;
	public int peso;

	public Gene(int id, int peso) {
		super();
		this.id = id;
		this.peso = peso;
	}

	@Override
	public String toString() {
		return "(Item: " + id + ", Peso: " + peso + ")";
	}

}
