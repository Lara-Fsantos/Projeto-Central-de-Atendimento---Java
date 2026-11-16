package service;

import java.util.Scanner;

import estruturas.FilaEncadeada;
import estruturas.ListaDupla;
import estruturas.NoDuplo;
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
    private ListaDupla<Solicitacao> historicoAtendimento;

    public CentralAtendimento() {
        this.filaEsperaP1 = new FilaEncadeada<Solicitacao>();
        this.filaEsperaP2 = new FilaEncadeada<Solicitacao>();
        this.historicoOperacoes = new PilhaEncadeada<Operacao>();
        this.historicoAtendimento = new ListaDupla<Solicitacao>();
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
                adicionarAoHistoricoAtendimento(solicitacaoAtual);
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
            adicionarAoHistoricoAtendimento(solicitacaoAtual);
            historicoOperacoes.push(new Operacao("CONCLUIDO", solicitacaoAtual));
            solicitacaoAtual = null;
            System.out.println("Atendimento concluído com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao registrar histórico do atendimento concluído: " + e.getMessage());
        }
    }

    private void adicionarAoHistoricoAtendimento(Solicitacao solicitacao) {
        solicitacao.setStatus("CONCLUIDA");
        if (!historicoAtendimento.insertAscending(solicitacao)) {
            throw new IllegalStateException(
                "Não foi possível adicionar a solicitação ao histórico de atendimentos.");
        }
    }

    public void exibirHistoricoRecente() {
        PilhaEncadeada<Operacao> pilhaAux = new PilhaEncadeada<Operacao>();
        for (int i=0; i<10 && !historicoOperacoes.isEmpty();i++){
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

    public void exibirHistoricoAtendimentos(boolean ordemCrescente) {
        if (historicoAtendimento.isEmpty()) {
            System.out.println("Não há atendimentos concluídos no histórico.");
            return;
        }

        if (ordemCrescente) {
            System.out.println("--- ATENDIMENTOS CONCLUÍDOS: PRIMEIRO AO ÚLTIMO ---");
            historicoAtendimento.showAscending();
        } else {
            System.out.println("--- ATENDIMENTOS CONCLUÍDOS: ÚLTIMO AO PRIMEIRO ---");
            historicoAtendimento.showDescending();
        }
        System.out.println();
    }
    public void navegarHistoricoAtendimentos(Scanner scanner) {
        if (historicoAtendimento.isEmpty()) {
            System.out.println("Não há atendimentos concluídos no histórico.");
            return;
        }

        NoDuplo<Solicitacao> atual = historicoAtendimento.getHead();
        while (true) {
            Solicitacao solicitacao = atual.getData();
            System.out.println("\nAtendimento atual: " + solicitacao.getCodigo());
            System.out.println(solicitacao);
            System.out.println("[A] Anterior");
            System.out.println("[P] Próximo");
            System.out.println("[S] Sair");
            System.out.print("Escolha uma opção: ");

            if (!scanner.hasNextLine()) {
                System.out.println();
                return;
            }

            String opcao = scanner.nextLine().trim();
            if (opcao.equalsIgnoreCase("S")) {
                return;
            } else if (opcao.equalsIgnoreCase("A")) {
                if (atual.getLeft() == null) {
                    System.out.println("Este é o primeiro atendimento do histórico.");
                } else {
                    atual = atual.getLeft();
                }
            } else if (opcao.equalsIgnoreCase("P")) {
                if (atual.getRight() == null) {
                    System.out.println("Este é o último atendimento do histórico.");
                } else {
                    atual = atual.getRight();
                }
            } else {
                System.out.println("Opção inválida. Digite A, P ou S.");
            }
        }
    }

    public void pesquisarAtendimento(int codigo) {
        if (historicoAtendimento.isEmpty()) {
            System.out.println("Não há atendimentos concluídos no histórico.");
            return;
        }

        Solicitacao busca = new Solicitacao();
        busca.setCodigo(codigo);
        int posicao = historicoAtendimento.searchAscending(busca);
        if (posicao == -1) {
            System.out.println("Atendimento #" + codigo + " não encontrado no histórico.");
            return;
        }

        System.out.println("--- ATENDIMENTO ENCONTRADO ---");
        System.out.println(historicoAtendimento.get(posicao).getData());
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
        Solicitacao sol = ultimaOp.getSolicitacao();
        FilaEncadeada<Solicitacao> filaPrioridade;
        try {
            if (quemAtender()) {
                filaPrioridade = filaEsperaP1;
            } else {
                filaPrioridade = filaEsperaP2;
            }
        } catch (Exception e)  {
            System.out.println("Não foi possível desfazer: prioridade da solicitação inválida.");
            return;
        }
        
        try {
            int elementosExistentes = filaPrioridade.totalElementos();
            sol.setStatus("AGUARDANDO");
            filaPrioridade.enqueue(sol);
            for (int i = 0; i < elementosExistentes; i++) {
                filaPrioridade.enqueue(filaPrioridade.dequeue());
            }
        } catch (Exception e) {
            System.out.println("Erro ao recolocar a solicitação na fila: " + e.getMessage());
            return;
        }

        try {
            historicoOperacoes.pop();
        } catch (Exception e) {
            System.out.println("Erro ao remover a última operação do histórico: " + e.getMessage());
            return;
        }

        if (solicitacaoAtual == sol) {
            solicitacaoAtual = null;
        }
        contadorAtendimentos = (contadorAtendimentos + 2) % 3;
        System.out.println("Sucesso: Atendimento da solicitação #" + sol.getCodigo() + " foi desfeito e ela retornou ao início da fila!");
    }

    public void gerarDadosExemplo(Menu menu) {
        String[] nomes = {
            "João", "Maria", "Pedro", "Ana",
            "Lucas", "Beatriz", "Rafael", "Camila"
        };
        String[] descricoes = {
            "Problema de internet", "Problema de hardware",
            "Falha na impressora", "Erro no sistema",
            "Acesso bloqueado", "Computador lento",
            "Configuração de rede", "Solicitação de manutenção"
        };
        String[] categorias = {
            "HelpDesk", "Suporte", "HelpDesk", "Suporte",
            "Manutenção", "HelpDesk", "Suporte", "Manutenção"
        };
        int[] prioridades = {1, 2, 1, 2, 1, 2, 1, 2};

        for (int i = 0; i < nomes.length; i++) {
            Solicitacao solicitacao = new Solicitacao(
                menu.proximoCodigoGerador(),
                nomes[i],
                descricoes[i],
                categorias[i],
                prioridades[i]);
            cadastrarSolicitacao(solicitacao);
        }

        for (int i = 0; i < 4; i++) {
            String responsavel = "Responsável de teste " + (i + 1);
            atenderProxima(responsavel);
            if (solicitacaoAtual != null) {
                solicitacaoAtual.setStatus("EM_ATENDIMENTO");
                solicitacaoAtual.setResponsavel(responsavel);
            }
        }
        System.out.println(
            "Dados de exemplo gerados: filas preenchidas, operações registradas, "
            + "atendimentos concluídos e um atendimento em andamento.");
    }
}