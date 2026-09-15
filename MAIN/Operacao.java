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
