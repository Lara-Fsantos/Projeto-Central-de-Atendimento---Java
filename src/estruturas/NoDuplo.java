package estruturas;

// Classe NodeDLL<T>: Node de uma lista duplamente encadeada
// genérica
// Autor1: Ivan Carlos Alcântara de Oliveira.
// Data da Criação: 10/10/2026. 15h.
public class NoDuplo<T extends Comparable<T>>{
	private T data;
	private	NoDuplo<T> left, right;
	
	public	NoDuplo() {
		this(null, null, null);
	}
	
	public NoDuplo(T data, NoDuplo<T> left, NoDuplo<T> right){
			this.data = data;
			this.left = left;
			this.right = right;
	};

	public T getData() { return data;};
	public NoDuplo<T> getLeft(){ return left;};
	public NoDuplo<T> getRight(){ return right;};
	public void setData(T data) { this.data = data;};
	public void setLeft(NoDuplo<T> left){ this.left = left; };
	public void setRight(NoDuplo<T> right){ this.right = right; }; 
}
