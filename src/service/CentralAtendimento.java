package service;

import estruturas.FilaEncadeada;
import estruturas.PilhaEncadeada;
import model.Operacao;
import model.Solicitacao;
import ui.Menu;

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
        if (filaEsperaP1.qIsEmpty()) {
            return false;
        }
         else if (contadorAtendimentos == 2) {
            return false;
        }
        return true;
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
            addAtender();
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
        PilhaEncadeada<Operacao> pilhaAux = new PilhaEncadeada<Operacao>();
        for (int i=0; i<=10 && !historicoOperacoes.isEmpty();i++){
            try {
                pilhaAux.push(historicoOperacoes.pop());
            } catch (Exception e) {
                break;
            }
        }
        System.out.println(pilhaAux);
        while (!pilhaAux.isEmpty()) {
            try {
                historicoOperacoes.push(pilhaAux.pop());
            } catch (Exception e) {
                break;
            }
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
        if (!ultimaOp.getTipo().equalsIgnoreCase("EM_ATENDIMENTO")) {
            System.out.println("Operação do topo (" + ultimaOp.getTipo() + ") não é do tipo 'EM_ATENDIMENTO'. Não é possível desfazer!");
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
        if (prioridade == 1) {
            filaAuxiliar = filaEsperaP1;
        } else if (prioridade == 2) {
            filaAuxiliar = filaEsperaP2;
        }
        try {
            while (!filaAuxiliar.qIsEmpty()) {
                if (prioridade == 1) {
                    filaEsperaP1.enqueue(filaAuxiliar.dequeue());
                } else {
                    filaEsperaP2.enqueue(filaAuxiliar.dequeue());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao transferir elementos para a fila auxiliar: " + e.getMessage());
            return;
        }
        System.out.println("Sucesso: Atendimento da solicitação #" + sol.getCodigo() + " foi desfeito e ela retornou ao início da fila!");
    }

    public void gerarDadosExemplo(Menu menu) {
        Solicitacao[] solicitacoes = {
           new Solicitacao(menu.getCodigoGerador(), "João", "Problema de internet", "HelpDesk", 1),
           new Solicitacao(menu.getCodigoGerador(), "Maria", "Problema de hardware", "Suporte", 2),
           new Solicitacao(menu.getCodigoGerador(), "Pedro", "Problema de manutenção", "Manutenção", 1),
           new Solicitacao(menu.getCodigoGerador(), "Ana", "Problema de software", "HelpDesk", 2)
        };
        for (Solicitacao solicitacao : solicitacoes) {
            cadastrarSolicitacao(solicitacao);

        }
        System.out.println("Dados de exemplo gerados com sucesso!");
    }
}
