/*1. cadastrar nova solicitação; e jogar na pilha a acao
2. inserir a solicitação na Fila;
3. consultar a próxima solicitação;
4. remover a próxima solicitação para atendimento;e jogar na pilha a acao
5. verificar se a Fila está vazia;
6. mostrar a quantidade de solicitações aguardando;
7. exibir as solicitações presentes na Fila.
*/

public class Operacao {
    private String tipo; // "CADASTRO", "ATENDIMENTO", "CANCELAMENTO"
    private Solicitacao solicitacao;

    public Operacao(String tipo, Solicitacao solicitacao) {
        this.tipo = tipo;
        this.solicitacao = solicitacao;
    }

    public String getTipo() {
        return tipo;
    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    @Override
    public String toString() {
        return "Operacao [Tipo: " + tipo + " | Solicitação #" + solicitacao.getCodigo() + " - " + solicitacao.getSolicitante() + "]";
    }
}
