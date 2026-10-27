package service;

import estruturas.FilaEncadeada;
import estruturas.PilhaEncadeada;
import model.Operacao;
import model.Solicitacao;

public class CentralAtendimento {
    private FilaEncadeada<Solicitacao> filaEspera;
    private PilhaEncadeada<Operacao> historicoOperacoes;

    public CentralAtendimento() {
        this.filaEspera = new FilaEncadeada<Solicitacao>(100);
        this.historicoOperacoes = new PilhaEncadeada<Operacao>();
    }

    public void cadastrarSolicitacao(Solicitacao sol) {
        if (filaEspera.qIsFull()) {
            System.out.println("\nErro: A fila de espera está cheia (capacidade máxima atingida)!");
            return;
        }
        if (historicoOperacoes.isFull()) {
            System.out.println("\nErro: O histórico de operações está cheio!");
            return;
        }
        try {
            filaEspera.enqueue(sol);
            historicoOperacoes.push(new Operacao("CADASTRO", sol));
            System.out.println("Solicitação cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("\nErro ao cadastrar solicitação: " + e.getMessage());
        }
    }

    public void consultarProxima() {
        try {
            Solicitacao proxima = filaEspera.front();
            System.out.println("--- PRÓXIMA SOLICITAÇÃO ---");
            System.out.println(proxima);
        } catch (Exception e) {
            System.out.println("Não há solicitações na fila.");
        }
    }

    public void atenderProxima(String responsavel) {
        Solicitacao atendi;
        try {
            atendi = filaEspera.dequeue();
        } catch (Exception e) {
            System.out.println("Não há solicitações na fila.");
            return;
        }

        atendi.setStatus("EM_ATENDIMENTO");
        atendi.setResponsavel(responsavel);
        try {
            historicoOperacoes.push(new Operacao("ATENDIMENTO", atendi));
        } catch (Exception e) {
            System.out.println("Erro ao registrar no histórico: " + e.getMessage());
        }

        System.out.println("--- SOLICITAÇÃO EM ATENDIMENTO ---");
        System.out.println(atendi);
        try {
            System.out.println("\nAtendimento em andamento...");
            Thread.sleep(2000);
            atendi.setStatus("CONCLUIDA");
            System.out.println("Atendimento concluído com sucesso!");
        } catch (InterruptedException e) {
            System.out.println("Erro ao simular atendimento: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void exibirFila() {
        System.out.println(filaEspera);
        if (filaEspera.qIsEmpty()) {
            System.out.println("Não há solicitações na fila.");
        }
    }

    public void exibirQuantidadeFila() {
        System.out.println("Quantidade de solicitações aguardando na fila: " + filaEspera.totalElementos());
    }

    public void consultarUltimaOperacao() {
        if (historicoOperacoes.isEmpty()) {
            System.out.println("Não há operações no histórico.");
            return;
        }
        try {
            Operacao ultima = historicoOperacoes.topo();
            System.out.println("--- ÚLTIMA OPERAÇÃO REALIZADA ---");
            System.out.println(ultima);
        } catch (Exception e) {
            System.out.println("Erro ao consultar histórico: " + e.getMessage());
        }
    }

    public void exibirHistorico() {
        System.out.println(historicoOperacoes);
        if (historicoOperacoes.isEmpty()) {
            System.out.println("Não há operações no histórico.");
        }
    }

    public void desfazerUltimaOperacao() {
        if (historicoOperacoes.isEmpty()) {
            System.out.println("Não há operações no histórico para desfazer.");
            return;
        }

        Operacao ultimaOp;
        try {
            ultimaOp = historicoOperacoes.topo();
        } catch (Exception e) {
            System.out.println("Não há operações no histórico para desfazer.");
            return;
        }
        if (!ultimaOp.getTipo().equalsIgnoreCase("ATENDIMENTO")) {
            System.out.println("Operação do topo (" + ultimaOp.getTipo() + ") não é do tipo 'ATENDIMENTO'. Não é possível desfazer!");
            return;
        }
        try {
            ultimaOp = historicoOperacoes.pop();
        } catch (Exception e) {
            System.out.println("Erro ao remover a última operação do histórico: " + e.getMessage());
            return;
        }

        Solicitacao sol = ultimaOp.getSolicitacao();
        sol.setStatus("AGUARDANDO");
        FilaEncadeada<Solicitacao> filaAuxiliar = new FilaEncadeada<Solicitacao>(100);
        try {
            filaAuxiliar.enqueue(sol);
        } catch (Exception e) {
            System.out.println("Erro ao adicionar a solicitação à fila auxiliar: " + e.getMessage());
            return;
        }
        try {
            while (!filaEspera.qIsEmpty()) {
                filaAuxiliar.enqueue(filaEspera.dequeue());
            }
        } catch (Exception e) {
            System.out.println("Erro ao transferir elementos para a fila auxiliar: " + e.getMessage());
            return;
        }
        this.filaEspera = filaAuxiliar;
        System.out.println("Sucesso: Atendimento da solicitação #" + sol.getCodigo() + " foi desfeito e ela retornou ao início da fila!");
    }

    public void gerarDadosExemplo() {
        Solicitacao[] solicitacoes = {
            new Solicitacao(1, "Alice", "Problema no computador", "HelpDesk"),
            new Solicitacao(2, "Bob", "Erro no software", "Suporte"),
            new Solicitacao(3, "Charlie", "Manutenção do servidor", "Manutenção"),
            new Solicitacao(4, "Diana", "Problema de rede", "HelpDesk"),
            new Solicitacao(5, "Eve", "Atualização de sistema", "Suporte"),
            new Solicitacao(6, "Frank", "Problema de hardware", "Manutenção"),
            new Solicitacao(7, "Grace", "Configuração de software", "Suporte"),
            new Solicitacao(8, "Hank", "Problema de impressão", "HelpDesk"),
            new Solicitacao(9, "Ivy", "Manutenção de rede", "Manutenção")
        };
        for (Solicitacao solicitacao : solicitacoes) {
            cadastrarSolicitacao(solicitacao);
        }
        System.out.println("Dados de exemplo gerados com sucesso!");
    }
}
