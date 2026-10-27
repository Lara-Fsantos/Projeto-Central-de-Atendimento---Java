package estruturas;

public class PilhaEncadeada<T> {
    private static final int TAM_DEFAULT = 100;
    private int topoPilha;
    private T[] e;

    @SuppressWarnings("unchecked")
    public PilhaEncadeada(int tamanho) {
        this.e = (T[]) new Object[tamanho];
        this.topoPilha = -1;
    }

    public PilhaEncadeada() {
        this(TAM_DEFAULT);
    }

    public boolean isEmpty() {
        return this.topoPilha == -1;
    }

    public boolean isFull() {
        return this.topoPilha == this.e.length - 1;
    }

    public void push(T e) throws Exception {
        if (!this.isFull()) {
            this.e[++this.topoPilha] = e;
        } else {
            throw new Exception("overflow - Estouro de Pilha");
        }
    }

    public T pop() throws Exception {
        if (!this.isEmpty()) {
            return this.e[this.topoPilha--];
        }
        throw new Exception("underflow - Esvaziamento de Pilha");
    }

    public T topo() throws Exception {
        if (!this.isEmpty()) {
            return this.e[this.topoPilha];
        }
        throw new Exception("Underlow - Esvaziamento de Pilha");
    }

    public int sizeElements() {
        return topoPilha + 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Pilha] topoPilha: ")
            .append(topoPilha)
            .append(", capacidade: ")
            .append(e.length)
            .append(", quantidade de elementos: ")
            .append(sizeElements());
        if (topoPilha != -1) {
            sb.append(", valor do Topo: ")
                .append(this.e[this.topoPilha]);
        } else {
            sb.append(", valor do Topo: PILHA VAZIA");
        }

        sb.append("\nConteudo da Pilha': [ ");
        for (int i = 0; i <= topoPilha; ++i) {
            if (i != topoPilha) {
                sb.append(e[i]).append(", ");
            } else {
                sb.append(e[i]);
            }
        }
        sb.append(" ]");
        return sb.toString();
    }
}
