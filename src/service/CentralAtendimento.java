package service;

import estruturas.FilaEncadeada;
import estruturas.PilhaEncadeada;
import model.Operacao;
import model.Solicitacao;

public class CentralAtendimento {
    private FilaEncadeada<Solicitacao> filaEsperaP1;
    private FilaEncadeada<Solicitacao> filaEsperaP2;
    private PilhaEncadeada<Operacao> historicoOperacoes;
    private int contadorAtendimentos = 0;
    private Solicitacao solicitacaoAtual;

    public CentralAtendimento() {
        this.filaEsperaP1 = new FilaEncadeada<Solicitacao>();
        this.filaEsperaP2 = new FilaEncadeada<Solicitacao>();
        this.historicoOperacoes = new PilhaEncadeada<Operacao>();
        this.contadorAtendimentos = 0;

    }
    public boolean quemAtender() {
        boolean atenderP1 = true;
        if (contadorAtendimentos == 2) {
            atenderP1 = false;
        }
        return atenderP1;
    }
    public void addAtender() {
         contadorAtendimentos++;
         contadorAtendimentos = contadorAtendimentos % 3;
    }

    public void cadastrarSolicitacao(Solicitacao sol) {
        try {
            if (sol.getPrioridade() == 1){
                filaEsperaP1.enqueue(sol);
            } else if (sol.getPrioridade() == 2){
                filaEsperaP2.enqueue(sol);
            }
            historicoOperacoes.push(new Operacao("CADASTRO", sol));
            System.out.println("Solicitação cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("\nErro ao cadastrar solicitação: " + e.getMessage());
        }
    }

    public void consultarProxima() {
        try {
            if (quemAtender()) {
                Solicitacao proxima = filaEsperaP1.front();
                System.out.println("--- PRÓXIMA SOLICITAÇÃO PRIORIDADE 1 ---");
                System.out.println(proxima);
            } else {
                Solicitacao proxima = filaEsperaP2.front();
                System.out.println("--- PRÓXIMA SOLICITAÇÃO PRIORIDADE 2 ---");
                System.out.println(proxima);
            }
        } catch (Exception e) {
            System.out.println("Não há solicitações na fila.");
        }
    }

    public void atenderProxima(String responsavel) {
        Solicitacao atendi;
        try {
            if (quemAtender()) {
                atendi = filaEsperaP1.dequeue();
            } else {
                atendi = filaEsperaP2.dequeue();
            }
        } catch (Exception e) {
            System.out.println("Não há solicitações na fila.");
            return;
        }

        try {
            if (solicitacaoAtual != null) {
                historicoOperacoes.push(new Operacao("CONCLUIDO", solicitacaoAtual));
            }
            solicitacaoAtual = atendi;
            historicoOperacoes.push(new Operacao("EM_ATENDIMENTO", atendi));
        } catch (Exception e) {
            System.out.println("Erro ao registrar histórico do atendimento: " + e.getMessage());
        }
    }

    public void exibirFila() {
        System.out.println("--- FILA DE ESPERA PRIORIDADE 1 ---");
        System.out.println(filaEsperaP1);
        if (filaEsperaP1.qIsEmpty()) {
            System.out.println("Não há solicitações na fila de prioridade 1.");
        }

        System.out.println("--- FILA DE ESPERA PRIORIDADE 2 ---");
        System.out.println(filaEsperaP2);
        if (filaEsperaP2.qIsEmpty()) {
            System.out.println("Não há solicitações na fila de prioridade 2.");
        }
    }

    public void exibirQuantidadeFila() {    
        try {
        int total = filaEsperaP1.totalElementos() + filaEsperaP2.totalElementos();
        System.out.println("Quantidade total de solicitações aguardando na fila: " + total);
        System.out.println("Quantidade de solicitações na fila de prioridade 1: " + filaEsperaP1.totalElementos());
        System.out.println("Quantidade de solicitações na fila de prioridade 2: " + filaEsperaP2.totalElementos());
        } catch (Exception e) {
            System.out.println("Erro ao exibir quantidade de solicitações na fila: " + e.getMessage());
        }
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

    public void conluirAtendimento() {
        if (solicitacaoAtual == null) {
            System.out.println("Não há atendimento em andamento.");
            return;
        }
        try {
            historicoOperacoes.push(new Operacao("CONCLUIDO", solicitacaoAtual));
            solicitacaoAtual = null;
            System.out.println("Atendimento concluído com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao registrar histórico do atendimento concluído: " + e.getMessage());
        }
    }

    public void exibirHistoricoRecente() {
        //RESOLVERR DEPOIS MO PREGUIÇA AGORA
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
        FilaEncadeada<Solicitacao> filaAuxiliar = new FilaEncadeada<Solicitacao>();
        try {
            filaAuxiliar.enqueue(sol);
        } catch (Exception e) {
            System.out.println("Erro ao adicionar a solicitação à fila auxiliar: " + e.getMessage());
            return;
        }
        int prioridade = sol.getPrioridade();
        filaEspera = new FilaEncadeada<Solicitacao>();  
        if (prioridade == 1) {
            filaEspera = filaEsperaP1;
        } else if (prioridade == 2) {
            filaEspera = filaEsperaP2;
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
           
        };
        for (Solicitacao solicitacao : solicitacoes) {
            cadastrarSolicitacao(solicitacao);
        }
        System.out.println("Dados de exemplo gerados com sucesso!");
    }
}
